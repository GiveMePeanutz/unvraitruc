package servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weka.core.Attribute;
import beans.User;
import dao.DAOFactory;
import dao.NaiveBayesDao;
import dao.UserDao;
import dataMining.NaiveBayesClass;

@WebServlet( "/naiveBayes" )
public class NaiveBayesServlet extends HttpServlet {

    public static final String CONF_DAO_FACTORY    = "daofactory";
    public static final String VIEW                = "/WEB-INF/naiveBayes.jsp";
    public static final String STUDENT_REQUEST_ATT = "students";
    public static final String RESULT              = "result";
    public static final String USER                = "selectedUser";
    public static final String MAP_RESULT          = "mapResult";

    public static final String STUDENT_FIELD       = "students";

    public static final String KMEANS_DATA_ATT     = "kmeansData";

    private UserDao            userDao;
    private NaiveBayesDao      naiveBayesDao;

    public void init() throws ServletException {
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.naiveBayesDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getNaiveBayesDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<User> listeUser = userDao.listGroup( "student" );
        Map<String, User> mapStudents = new HashMap<String, User>();
        for ( User stu : listeUser ) {
            mapStudents.put( stu.getUsername(), stu );
        }
        request.setAttribute( STUDENT_REQUEST_ATT, mapStudents );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        NumberFormat nf = new DecimalFormat( "0.##" );
        String result = "Error";
        String studentName = getFieldValue( request, STUDENT_FIELD );
        User user = userDao.find( studentName );
        NaiveBayesClass classifier = new NaiveBayesClass( userDao, naiveBayesDao );
        double[] likelihood = null;
        Attribute courseAttribute = null;

        try {
            result = classifier.courseAdvice( user );
            likelihood = classifier.getfDistribution();
            courseAttribute = classifier.getCourse();

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        if ( likelihood != null || courseAttribute != null )
        {
            HashMap<String, String> mapCourseLikelihood = new HashMap<String, String>();
            for ( int i = 0; i < likelihood.length; i++ )
            {
                mapCourseLikelihood.put( courseAttribute.value( i ), nf.format( likelihood[i] * 100 ) );
            }
            request.setAttribute( MAP_RESULT, mapCourseLikelihood );
        }
        request.setAttribute( USER, studentName );
        request.setAttribute( RESULT, result );

        if ( request.getAttribute( STUDENT_REQUEST_ATT ) == null ) {
            List<User> listeUser = userDao.listGroup( "student" );
            Map<String, User> mapStudents = new HashMap<String, User>();
            for ( User stu : listeUser ) {
                mapStudents.put( stu.getUsername(), stu );
            }
            request.setAttribute( STUDENT_REQUEST_ATT, mapStudents );
        }

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
}
