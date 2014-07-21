package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.KmeansDataType;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.KmeansDataDao;
import dataMining.Kmeans;

@WebServlet( "/kMeans" )
public class KmeansServlet extends HttpServlet {

    public static final String  CONF_DAO_FACTORY   = "daofactory";
    public static final String  VIEW               = "/WEB-INF/kMeans.jsp";

    public static final String  KMEANS_DATA_ATT    = "kmeansData";
    public static final String  KMEANS_CLUSTER_ATT = "centers";
    private static final String INDIVIDUAL_FIELD   = "Individual";
    private static final String VARIABLE_FIELD     = "Variable";
    private static final String CLUSTER_FIELD      = "Clusters";
    public static final String  USER_SESSION_ATT   = "userSession";

    private KmeansDataDao       kmeansDataDao;
    private FactTableDao        factTableDao;

    public void init() throws ServletException {
        this.kmeansDataDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getKmeansDataDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        KmeansDataType[] dataArray = null;

        int nbClusters;
        if ( getFieldValue( request, INDIVIDUAL_FIELD ).equalsIgnoreCase( "User" ) ) {
            dataArray = kmeansDataDao.getUserActivity();
            if ( kmeansDataDao.countActiveUsers() < getIntValue( request, CLUSTER_FIELD ) ) {
                nbClusters = 1;
            } else {
                nbClusters = getIntValue( request, CLUSTER_FIELD );
            }
        } else { // getFieldValue( request , INDIVIDUAL_FIELD ) == "Group"
            dataArray = kmeansDataDao.getGroupActivity();
            if ( kmeansDataDao.countActiveGroups() < getIntValue( request, CLUSTER_FIELD ) ) {
                nbClusters = 1;
            } else {
                nbClusters = getIntValue( request, CLUSTER_FIELD );
            }
        }
        Kmeans kmeans = new Kmeans( dataArray, nbClusters );
        kmeans.calculateClusters();

        HttpSession session = request.getSession();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername(), "Kmeans Algorithm" );

        ArrayList<KmeansDataType>[] clusteredData = kmeans.getClusters();
        double[][] clusterCenters = kmeans.getClusterCenters();

        request.setAttribute( KMEANS_DATA_ATT, clusteredData );
        request.setAttribute( KMEANS_CLUSTER_ATT, clusterCenters );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    private static String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

    private static int getIntValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        int valueInt = Integer.parseInt( value );
        if ( valueInt == 0 ) {
            return 0;
        } else {
            return valueInt;
        }
    }

}
