package servlets;

//Controller of Naive Bayes algorithms

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import weka.core.Attribute;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
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
    public static final String USER_SESSION_ATT    = "userSession";

    public static final String STUDENT_FIELD       = "students";

    public static final String KMEANS_DATA_ATT     = "kmeansData";

    private UserDao            userDao;
    private NaiveBayesDao      naiveBayesDao;
    private FactTableDao       factTableDao;
    private UtilitiesForm      util                = new UtilitiesForm();

    public void init() throws ServletException {

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.naiveBayesDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getNaiveBayesDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Student list retrieving and saving in the request as a
        // LinkedHashMap(key = username)
        List<User> listeUser = userDao.listGroup( "student" );
        Map<String, User> mapStudents = new LinkedHashMap<String, User>();
        for ( User stu : listeUser ) {
            mapStudents.put( stu.getUsername(), stu );
        }
        request.setAttribute( STUDENT_REQUEST_ATT, mapStudents );

        // Naive Bayes page display
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Limits number of digits
        NumberFormat nf = new DecimalFormat( "0.##" );

        String result = "Error";

        // User name retrieving from the form
        String studentName = util.getFieldValue( request, STUDENT_FIELD );
        // User informations retrieving thanks to the username.
        User user = userDao.find( studentName );

        // Creates a naive bayes classifier (from dataMining package)
        NaiveBayesClass classifier = new NaiveBayesClass( user, userDao, naiveBayesDao );

        double[] likelihood = null;
        Attribute courseAttribute = null;

        try {
            // Runs the naive Bayes algorithm and save the result
            result = classifier.courseAdvice( user );
            // Runs the naive Bayes algorithm
            likelihood = classifier.getfDistribution();
            // Course list retrieving (attribute, vector of course names)
            courseAttribute = classifier.getCourse();

            /* Session retrieving from the request */
            HttpSession session = request.getSession();
            User userSession = new User();
            // userSession = user logged onn this session
            userSession = (User) session.getAttribute( USER_SESSION_ATT );
            // New action saved in database
            factTableDao.addFact( userSession.getUsername(), "Naive Bayes Algorithm" );

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        if ( likelihood != null || courseAttribute != null )// If the algorithm
                                                            // returns a result
        {
            HashMap<String, String> mapCourseLikelihood = new HashMap<String, String>();
            // Saving likelihoods in a map (key = course name, value =
            // likelihood)
            for ( int i = 0; i < likelihood.length; i++ )
            {
                mapCourseLikelihood.put( courseAttribute.value( i ), nf.format( likelihood[i] * 100 ) );
            }
            request.setAttribute( MAP_RESULT, mapCourseLikelihood );
        }

        request.setAttribute( USER, studentName );
        request.setAttribute( RESULT, result );

        // if mapStudents is not created yet
        if ( request.getAttribute( STUDENT_REQUEST_ATT ) == null ) {
            // Student list retrieving and saving in the request as a
            // LinkedHashMap(key = username)
            List<User> listeUser = userDao.listGroup( "student" );
            Map<String, User> mapStudents = new HashMap<String, User>();
            for ( User stu : listeUser ) {
                mapStudents.put( stu.getUsername(), stu );
            }
            request.setAttribute( STUDENT_REQUEST_ATT, mapStudents );
        }

        // Naive Bayes page loads again
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }
}
