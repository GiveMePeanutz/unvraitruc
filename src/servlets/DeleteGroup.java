package servlets;

//Controller of group deletion 

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
import dao.GroupDao;

@WebServlet( "/deleteGroup" )
public class DeleteGroup extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String GROUPNAME_PARAM  = "groupName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayGroups";
    public static final String ACTIVITY_NAME    = "deleteGroup";

    private GroupDao           GroupDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {

        /* Récupération d'une instance de notre DAO Utilisateur */
        this.GroupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        // Group name parameter retrieving from URL.
        String groupName = util.getParameterValue( request, GROUPNAME_PARAM );

        // if this parameter exists
        if ( groupName != null )
        {
            try {
                // So we delete the corresponding course from database
                GroupDao.delete( groupName );

                /* Session retrieving from the request */
                HttpSession session = request.getSession();
                User userSession = new User();
                // userSession = user logged onn this session
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Group Deleted" );
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection toward the course list */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

}