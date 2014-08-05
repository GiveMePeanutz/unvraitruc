package servlets;

//Controller of course cancellation 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.User;
import dao.DAOException;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;

@WebServlet( "/cancelCourse" )
public class CancelCourse extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayCourses";
    public static final String ACTIVITY_NAME    = "cancelCourse";

    private UserDao            UserDao;
    private FactTableDao       FactTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        this.UserDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User user = new User();
        // user = user logged on this session
        user = (User) session.getAttribute( USER_SESSION_ATT );

        // Course name parameter retrieving from URL.
        String courseName = util.getParameterValue( request, COURSENAME_PARAM );
        // Username of the user logged retrieving
        String username = user.getUsername();

        // if the two parameters are not empty
        if ( courseName != null && username != null )
        {
            try {
                // Course deletion for this user in database
                UserDao.deleteCourse( username, courseName );
                // Course deletion for this user in session
                user.removeCourse( courseName );

                session.setAttribute( USER_SESSION_ATT, user );

                // New action saved in database
                FactTableDao.addFact( username, "Course Unsubscribe" );

            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the courses display */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

}