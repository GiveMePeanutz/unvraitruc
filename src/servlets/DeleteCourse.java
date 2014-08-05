package servlets;

//Controller of course deletion 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.User;
import dao.CourseDao;
import dao.DAOException;
import dao.DAOFactory;
import dao.FactTableDao;

@WebServlet( "/deleteCourse" )
public class DeleteCourse extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayCourses";
    public static final String ACTIVITY_NAME    = "deleteCourse";

    private CourseDao          CourseDao;
    private FactTableDao       factTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        /* Récupération d'une instance de notre DAO Utilisateur */
        this.CourseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        // Course name parameter retrieving from URL.
        String courseName = util.getParameterValue( request, COURSENAME_PARAM );

        // if this parameter exists
        if ( courseName != null )
        {
            try {

                // So we delete the corresponding course from database
                CourseDao.delete( courseName );

                /* Session retrieving from the request */
                HttpSession session = request.getSession();
                User userSession = new User();
                // userSession = user logged onn this session
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Course Deleted" );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the course list */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

}