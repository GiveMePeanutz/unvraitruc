package servlets;

//Controller of course subscription 

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
import dao.TransactionTableDao;
import dao.UserDao;

@WebServlet( "/inscriptionCourse" )
public class InscriptionCourse extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayCourses";

    private UserDao            userDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        // user = user logged onn this session
        User user = new User();
        user = (User) session.getAttribute( USER_SESSION_ATT );
        // Username of the user logged retrieving
        String username = user.getUsername();

        // Course name parameter retrieving from URL.
        String courseName = util.getParameterValue( request, COURSENAME_PARAM );

        // if the two parameters are not empty
        if ( courseName != null && username != null )
        {
            try {
                // Course add for this user in database
                userDao.addCourse( username, courseName );
                // Course add for this user in session
                user.addCourseName( courseName );

                session.setAttribute( USER_SESSION_ATT, user );

                // New action saved in database
                transactionTableDao.addTransaction( username, "Course Subscribe" );

            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the courses display */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

}