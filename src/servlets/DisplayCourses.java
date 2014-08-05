package servlets;

//Controller of courses display 

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import weka.core.Attribute;
import beans.Course;
import beans.User;
import dao.CourseDao;
import dao.DAOFactory;
import dao.NaiveBayesDao;
import dao.UserDao;
import dataMining.NaiveBayesClass;

@WebServlet( "/displayCourses" )
public class DisplayCourses extends HttpServlet {

    public static final String CONF_DAO_FACTORY            = "daofactory";
    public static final String AVAILABLECOURSE_REQUEST_ATT = "availableCourses";
    public static final String USERCOURSE_REQUEST_ATT      = "userCourses";
    public static final String VIEW                        = "/WEB-INF/displayCourses.jsp";
    public static final String USER_SESSION_ATT            = "userSession";
    public static final String MAP_RESULT                  = "mapResult";

    private CourseDao          courseDao;
    private UserDao            userDao;
    private NaiveBayesDao      naiveBayesDao;

    public void init() throws ServletException {

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.naiveBayesDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getNaiveBayesDao();
        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User user = new User();
        // user = user logged onn this session
        user = (User) session.getAttribute( USER_SESSION_ATT );

        // Course list retrieving from database
        List<Course> listeCourses = courseDao.list();

        LinkedHashMap<String, Course> mapAvailableCourses = new LinkedHashMap<String, Course>();
        LinkedHashMap<String, Course> mapUserCourses = new LinkedHashMap<String, Course>();

        if ( user.getCourseNames().isEmpty() )// if the user didn't subscribe to
                                              // any courses yet
        {

            // Saving the course list in the availableCourse map (key = course
            // name)
            for ( Course course : listeCourses )
            {
                mapAvailableCourses.put( course.getCourseName(), course );
            }
        }
        else
        {
            // else user's courses retrieving
            List<String> userCourseList = user.getCourseNames();

            // and saving in UserCourses if he subscribed to this course yet, in
            // availableCourses else
            for ( Course course : listeCourses ) {
                if ( userCourseList.contains( course.getCourseName() ) )
                {
                    mapUserCourses.put( course.getCourseName(), course );

                }
                else
                {
                    mapAvailableCourses.put( course.getCourseName(), course );
                }
            }
        }

        request.setAttribute( USERCOURSE_REQUEST_ATT, mapUserCourses );
        request.setAttribute( AVAILABLECOURSE_REQUEST_ATT, mapAvailableCourses );

        /*------------------------Naive Bayes----------------------------------*/

        // Limits number of digits
        NumberFormat nf = new DecimalFormat( "0.##" );
        // Creates a naive bayes classifier (from dataMining package)
        NaiveBayesClass classifier = new NaiveBayesClass( user, userDao, naiveBayesDao );

        double[] likelihood = null;

        Attribute courseAttribute = null;

        try {
            // Runs the naive Bayes algorithm
            classifier.courseAdvice( user );
            // Save the likelihoods in a table
            likelihood = classifier.getfDistribution();
            // Course list retrieving (attribute, vector of course names)
            courseAttribute = classifier.getCourse();

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        if ( likelihood != null || courseAttribute != null ) // If the algorithm
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

        // Course list display
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}