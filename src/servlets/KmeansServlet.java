package servlets;

//Controller for KMeans Algorithms 

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.KmeansDataType;
import beans.User;
import dao.DAOFactory;
import dao.TransactionTableDao;
import dao.KmeansDataDao;
import dataMining.Kmeans;

@WebServlet( "/kMeans" )
public class KmeansServlet extends HttpServlet {

    public static final String  CONF_DAO_FACTORY   = "daofactory";
    public static final String  VIEW               = "/WEB-INF/kMeans.jsp";

    public static final String  KMEANS_DATA_ATT    = "kmeansData";
    public static final String  KMEANS_CLUSTER_ATT = "centers";
    private static final String INDIVIDUAL_FIELD   = "Individual";
    private static final String CLUSTER_FIELD      = "Clusters";
    public static final String  USER_SESSION_ATT   = "userSession";

    private KmeansDataDao       kmeansDataDao;
    private TransactionTableDao        transactionTableDao;
    private UtilitiesForm       util               = new UtilitiesForm();

    public void init() throws ServletException {
        this.kmeansDataDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getKmeansDataDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        KmeansDataType[] dataArray = null;

        int nbClusters;
        if ( util.getFieldValue( request, INDIVIDUAL_FIELD ).equalsIgnoreCase( "User" ) ) {
            dataArray = kmeansDataDao.getUserActivity();
            if ( kmeansDataDao.countActiveUsers() < util.getIntValue( request, CLUSTER_FIELD ) ) {
                nbClusters = 1;
            } else {
                nbClusters = util.getIntValue( request, CLUSTER_FIELD );
            }
        } else {
            dataArray = kmeansDataDao.getGroupActivity();
            if ( kmeansDataDao.countActiveGroups() < util.getIntValue( request, CLUSTER_FIELD ) ) {
                nbClusters = 1;
            } else {
                nbClusters = util.getIntValue( request, CLUSTER_FIELD );
            }
        }
        Kmeans kmeans = new Kmeans( dataArray, nbClusters );
        kmeans.calculateClusters();

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged onn this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        // New action saved in database
        transactionTableDao.addTransaction( userSession.getUsername(), "Kmeans Algorithm" );

        ArrayList<KmeansDataType>[] clusteredData = kmeans.getClusters();
        double[][] clusterCenters = kmeans.getClusterCenters();

        request.setAttribute( KMEANS_DATA_ATT, clusteredData );
        request.setAttribute( KMEANS_CLUSTER_ATT, clusterCenters );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

}
