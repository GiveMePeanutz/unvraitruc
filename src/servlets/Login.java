package servlets;

//Controller of registration  

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;
import forms.LoginForm;

@WebServlet( "/login" )
public class Login extends HttpServlet {

    public static final String  CONF_DAO_FACTORY        = "daofactory";
    public static final String  USER_ATT                = "user";
    public static final String  FORM_ATT                = "form";
    public static final String  USER_SESSION_ATT        = "userSession";
    public static final String  USER_SESSION_ACCESS_ATT = "userSessionAccess";
    public static final String  USER_GROUP_ATT          = "userGroup";
    public static final String  VIEW                    = "/WEB-INF/login.jsp";
    private static final String PATH                    = "path";
    public static final String  ACTIVITY_NAME           = "login";

    private UserDao             userDao;
    private FactTableDao        factTableDao;

    public void init() throws ServletException {

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Forwarding toward the login page
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( PATH );

        // Preparation of the form object
        LoginForm form = new LoginForm( userDao );
        // Password and Username verification
        User user = form.connectUser( request, path );

        // Session retrieving from the request
        HttpSession session = request.getSession();

        if ( form.getErrors().isEmpty() )// if no error during the check
        {
            session.setAttribute( USER_SESSION_ATT, user ); // saves the user in
                                                            // session
            // lists differents menus he can access and saves it in a session
            // attribute
            List<String> menus = userDao.listAccMenus( user.getUsername() );
            session.setAttribute( USER_SESSION_ACCESS_ATT, menus );

            User userSession = new User();
            // userSession = user logged onn this session
            userSession = (User) session.getAttribute( USER_SESSION_ATT );
            // New action saved in database
            factTableDao.addFact( userSession.getUsername(), "Login" );

        }
        else
        {
            // No user logged
            session.setAttribute( USER_SESSION_ATT, null );
        }

        request.setAttribute( FORM_ATT, form );
        request.setAttribute( "username", user.getUsername() );
        request.setAttribute( USER_ATT, user );

        // Displays again the login page
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }
}
