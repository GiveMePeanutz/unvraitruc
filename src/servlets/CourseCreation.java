package servlets;

//Controller of course creation 

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.Course;
import beans.User;
import dao.CourseDao;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;
import forms.CourseCreationForm;

@WebServlet( urlPatterns = "/courseCreation" )
public class CourseCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY    = "daofactory";
    public static final String PATH                = "path";
    public static final String COURSE_ATT          = "course";
    public static final String FORM_ATT            = "form";
    public static final String VUE_SUCCESS         = "/Project/displayCourses";
    public static final String VUE_FORM            = "/WEB-INF/createCourse.jsp";
    public static final String GROUP_REQUEST_ATT   = "courses";
    public static final String VERIFY_PARAM        = "modify";
    public static final String COURSENAME_PARAM    = "courseName";
    public static final String VERIFY_PARAM2       = "Create";
    public static final String USER_SESSION_ATT    = "userSession";
    public static final String USER_REQUEST_ATT    = "users";
    public static final String TEACHER_REQUEST_ATT = "teachers";

    public static final String ACTIVITY_NAME       = "courseCreation";

    private UserDao            userDao;
    private CourseDao          courseDao;
    private FactTableDao       factTableDao;
    private UtilitiesForm      util                = new UtilitiesForm();

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Boolean parameter retrieving from URL : true if it's a modification
        String modifiable = util.getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            // Course Name retrieving from URL
            String courseName = util.getParameterValue( request, COURSENAME_PARAM );

            // Searching of the course corresponding to the course name
            Course course = courseDao.find( courseName );

            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( COURSE_ATT, course );
        }

        // Teacher list retrieving and saving in the request as a
        // LinkedHashMap(key = username)
        List<User> listeTeachers = userDao.listGroup( "teacher" );
        LinkedHashMap<String, User> mapTeachers = new LinkedHashMap<String, User>();
        for ( User user : listeTeachers ) {
            mapTeachers.put( user.getUsername(), user );
        }

        request.setAttribute( TEACHER_REQUEST_ATT, mapTeachers );

        // Creation form display
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Preparation of the form object */
        CourseCreationForm form = new CourseCreationForm( courseDao );

        Course course = null;

        // Value of Create Button retrieving
        String modify = request.getParameter( VERIFY_PARAM2 );

        if ( modify.equals( "Modify" ) )// So it's a modification
        {
            try {
                course = form.modifyCourse( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else // So it's a creation
        {
            try {
                course = form.createCourse( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        request.setAttribute( COURSE_ATT, course );
        request.setAttribute( FORM_ATT, form );

        // if mapTeacher is not created yet
        if ( request.getAttribute( TEACHER_REQUEST_ATT ) == null )
        {

            // Teacher list retrieving and saving in the request as a
            // LinkedHashMap(key = username)
            List<User> listeTeachers = userDao.listGroup( "teacher" );
            LinkedHashMap<String, User> mapTeachers = new LinkedHashMap<String, User>();
            for ( User user : listeTeachers ) {
                mapTeachers.put( user.getUsername(), user );
            }

            request.setAttribute( TEACHER_REQUEST_ATT, mapTeachers );
        }

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged on this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) // if there is no error after the
                                          // verification...
        {
            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Course modified" );
            }
            else
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Course created" );
            }

            response.sendRedirect( VUE_SUCCESS );
        }
        else // if there is at least one error
        {

            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Course modification errors" );

                // Specifies that it's still a modification
                request.setAttribute( VERIFY_PARAM, "true" );

            }
            else
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Course creation errors" );
            }

            // else forwarding toward the creation form
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }

}