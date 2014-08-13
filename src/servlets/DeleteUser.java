package servlets;

//Controller of user deletion 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Encryption;
import utilities.UtilitiesForm;
import beans.User;
import dao.DAOException;
import dao.DAOFactory;
import dao.TransactionTableDao;
import dao.UserDao;

@WebServlet( "/deleteUser" )
public class DeleteUser extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USERNAME_PARAM   = "username";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayUsers";
    public static final String ACTIVITY_NAME    = "deleteUser";

    private UserDao            userDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        Encryption enc = new Encryption();

        // Course name parameter retrieving from URL.
        String username = util.getParameterValue( request, USERNAME_PARAM );

        // if this parameter exists
        if ( username != null )
        {
            try {
                // So we delete the corresponding course from database
                userDao.delete( enc.decrypt( username ) );

                /* Session retrieving from the request */
                HttpSession session = request.getSession();
                User userSession = new User();
                // userSession = user logged onn this session
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "User Deleted" );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the course list */
        response.sendRedirect( request.getContextPath() + VIEW );
    }
}
