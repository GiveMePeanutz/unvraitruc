package servlets;

//Controller of user creation

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.Group;
import beans.User;
import dao.DAOFactory;
import dao.TransactionTableDao;
import dao.GroupDao;
import dao.UserDao;
import forms.FormValidationException;
import forms.UserCreationForm;

@WebServlet( urlPatterns = "/userCreation", initParams = @WebInitParam( name = "path", value = "/files/images/" ) )
@MultipartConfig( location = "c:/files/images", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 5, fileSizeThreshold = 1024 * 1024 * 3 )
public class UserCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PATH              = "path";
    public static final String USER_ATT          = "user";
    public static final String FORM_ATT          = "form";
    public static final String GROUP_REQUEST_ATT = "groups";
    public static final String USERNAME_PARAM    = "username";
    public static final String VERIFY_PARAM      = "modify";
    public static final String VERIFY_PARAM2     = "Create";
    public static final String USER_SESSION_ATT  = "userSession";

    public static final String VUE_SUCCESS       = "/Project/displayUsers";
    public static final String VUE_FORM          = "/WEB-INF/createUser.jsp";

    private UserDao            userDao;
    private GroupDao           groupDao;
    private TransactionTableDao       transactionTableDao;
    private UtilitiesForm      util              = new UtilitiesForm();

    public void init() throws ServletException {
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Boolean parameter retrieving from URL : true if it's a modification
        String modifiable = util.getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            // User username retrieving from URL
            String userName = util.getParameterValue( request, USERNAME_PARAM );

            // Searching of the user corresponding to the username
            User user = userDao.find( userName );

            request.setAttribute( USER_ATT, user );
            request.setAttribute( VERIFY_PARAM, modifiable );

        }

        // Group list retrieving and saving in the request as a
        // LinkedHashMap(key = group name)
        List<Group> listeGroup = groupDao.list();
        Map<String, Group> mapGroups = new HashMap<String, Group>();
        for ( Group group : listeGroup ) {
            mapGroups.put( group.getGroupName(), group );
        }

        request.setAttribute( GROUP_REQUEST_ATT, mapGroups );

        // Creation form display
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( PATH );

        UserCreationForm form = new UserCreationForm( userDao );

        User user = null;

        // Value of Create Button retrieving
        String modify = request.getParameter( VERIFY_PARAM2 );

        if ( modify.equals( "Modify" ) )// Modification
        {
            try {

                user = form.modifyUser( request, path );

            } catch ( ParseException e ) {
                e.printStackTrace();
            } catch ( FormValidationException e ) {
                e.printStackTrace();
            }
        }
        else // Creation
        {
            try {
                user = form.createUser( request, path );
            } catch ( ParseException | FormValidationException e ) {
                e.printStackTrace();
            }
        }

        request.setAttribute( USER_ATT, user );
        request.setAttribute( FORM_ATT, form );

        // if mapGroups is not created yet
        if ( request.getAttribute( GROUP_REQUEST_ATT ) == null )
        {
            // Group list retrieving and saving in the request as a
            // LinkedHashMap(key = group name)
            List<Group> listGroup = groupDao.list();
            Map<String, Group> mapGroups = new HashMap<String, Group>();
            for ( Group group : listGroup ) {
                mapGroups.put( group.getGroupName(), group );
            }
            request.setAttribute( GROUP_REQUEST_ATT, mapGroups );
        }

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged onn this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );

        // if no error
        if ( form.getErrors().isEmpty() ) // if there is no error after the
                                          // verification...
        {
            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "User modified" );
            }
            else
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "User created" );
            }

            // Redirection toward the course list
            response.sendRedirect( VUE_SUCCESS );
        }
        else// if there is at least one error
        {

            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "User modification failed" );

                // Specifies that it's still a modification
                request.setAttribute( VERIFY_PARAM, "true" );
            }
            else
            {
                // New action saved in database
                transactionTableDao.addTransaction( userSession.getUsername(), "User creation failed" );
            }

            // else forwarding toward the creation form
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}