package servlets;

//Controller of privileges creation 

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import staticData.Menu;
import utilities.UtilitiesForm;
import beans.Priv;
import beans.User;
import dao.DAOFactory;
import dao.TransactionTableDao;
import dao.PrivDao;
import forms.PrivilegeCreationForm;

@WebServlet( urlPatterns = "/privCreation" )
public class PrivilegeCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PATH             = "path";
    public static final String PRIV_ATT         = "priv";
    public static final String FORM_ATT         = "form";
    public static final String MENU_REQUEST_ATT = "menus";
    public static final String PRIVNAME_PARAM   = "privName";
    public static final String VERIFY_PARAM     = "modify";
    public static final String VERIFY_PARAM2    = "Create";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VUE_SUCCESS      = "/Project/displayPrivs";
    public static final String VUE_FORM         = "/WEB-INF/createPrivilege.jsp";
    public static final String ACTIVITY_NAME    = "privCreation";

    private PrivDao            privDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util             = new UtilitiesForm();

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Boolean parameter retrieving from URL : true if it's a modification
        String modifiable = util.getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            // Privilege Name retrieving from URL
            String privName = util.getParameterValue( request, PRIVNAME_PARAM );

            // Searching of the privilege corresponding to the privilege name
            Priv priv = privDao.find( privName );

            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( PRIV_ATT, priv );

        }

        // Menu list retrieving and saving in the request
        Map<Integer, String> mapMenus = Menu.list();
        request.setAttribute( MENU_REQUEST_ATT, mapMenus );

        // Creation form display
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Preparation of the form object */
        PrivilegeCreationForm form = new PrivilegeCreationForm( privDao );

        Priv priv = null;

        // Value of Create Button retrieving
        String modify = request.getParameter( VERIFY_PARAM2 );

        if ( modify.equals( "Modify" ) )// modification
        {
            try {
                priv = form.modifyPriv( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else // creation
        {
            try {
                priv = form.createPriv( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        request.setAttribute( PRIV_ATT, priv );
        request.setAttribute( FORM_ATT, form );

        // Menu list retrieving and saving in the request
        LinkedHashMap<Integer, String> mapMenus = Menu.list();

        request.setAttribute( MENU_REQUEST_ATT, mapMenus );

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        // userSession = user logged on this session
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );

        // if no error
        if ( form.getErrors().isEmpty() )// if there is no error after the
                                         // verification...
        {
            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Privilege modified" );
            }
            else
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Privilege created" );
            }

            // Redirection toward the privilege list
            response.sendRedirect( VUE_SUCCESS );
        }
        else // if there is at least one error
        {

            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Privilege modification errors" );

                // Specifies that it's still a modification
                request.setAttribute( VERIFY_PARAM, "true" );
            }
            else
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "Privilege creation errors" );
            }

            // else forwarding toward the creation form
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}