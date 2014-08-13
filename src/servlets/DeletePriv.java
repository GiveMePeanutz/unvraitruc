package servlets;

//Controller of privilege deletion 

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
import dao.PrivDao;

@WebServlet( "/deletePriv" )
public class DeletePriv extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PRIVNAME_PARAM   = "privName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayPrivs";
    public static final String ACTIVITY_NAME    = "deletePriv";

    private PrivDao            PrivDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        /* Récupération d'une instance de notre DAO Utilisateur */
        this.PrivDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        // Course name parameter retrieving from URL.
        String privName = util.getParameterValue( request, PRIVNAME_PARAM );

        // if this parameter exists
        if ( privName != null )
        {
            try {
                // So we delete the corresponding course from database
                PrivDao.delete( privName );

                /* Session retrieving from the request */
                HttpSession session = request.getSession();
                User userSession = new User();
                // userSession = user logged onn this session
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Privilege Deleted" );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the course list */
        response.sendRedirect( request.getContextPath() + VIEW );
    }
}