package servlets;

//Controller of profiles display 

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Encryption;
import utilities.UtilitiesForm;
import beans.User;
import dao.DAOFactory;
import dao.UserDao;

@WebServlet( urlPatterns = "/profile", initParams = @WebInitParam( name = "path", value = "/files/images/" ) )
public class Profile extends HttpServlet {

    public static final String CONF_DAO_FACTORY        = "daofactory";
    public static final String USERNAME_PARAM          = "username";
    public static final String USER_SESSION_ACCESS_ATT = "userSessionAccess";
    public static final String USER_SESSION_ATT        = "userSession";

    public static final String USER_REQUEST_ATT        = "user";
    public static final String USER_MODIFIABLE_ATT     = "userModifiable";
    public static final String VIEW                    = "/WEB-INF/profile.jsp";

    private UserDao            userDao;
    private UtilitiesForm      util                    = new UtilitiesForm();

    public void init() throws ServletException {

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Session retrieving from the request */
        HttpSession session = request.getSession();

        Encryption enc = new Encryption();

        String username = "";

        // If the user logged try to access to his own profile
        if ( util.getParameterValue( request, "personnal" ).equals( "true" ) )
        {
            User userSession = new User();
            // userSession = user logged onn this session
            userSession = (User) session.getAttribute( USER_SESSION_ATT );
            // Username of the user logged retrieving
            username = userSession.getUsername();
        }
        else // i.e, he try to see the profile of an other user (teacher ...)
        {
            // username = decryption of the URL parameter
            username = enc.decrypt( util.getParameterValue( request, USERNAME_PARAM ) );
        }

        // User informations retrieving thanks to "username" and saving in a
        // request attribute
        User user = userDao.find( username );
        request.setAttribute( USER_REQUEST_ATT, user );

        // User menus retrieving
        List<String> menus = ( (List<String>) session.getAttribute( USER_SESSION_ACCESS_ATT ) );

        // if the user could modify the users or if he could modify the teachers
        // and the user he wants to see the profile is a teacher or if he could
        // modify the students and the user he wants to see the profile is a
        // student
        if ( menus.contains( "Modify User" )
                || ( menus.contains( "Modify Teacher" ) &&
                ( user.getGroupNames().contains( "Teacher" ) || user.getGroupNames().contains( "TeacherOfficial" ) ) )
                || ( menus.contains( "Modify Student" ) &&
                ( user.getGroupNames().contains( "Student" ) || user.getGroupNames().contains( "StudentOfficial" ) ) ) )
        {
            // boolean request attribute became true
            request.setAttribute( USER_MODIFIABLE_ATT, true );
        }
        else // boolean request attribute became false
        {
            request.setAttribute( USER_MODIFIABLE_ATT, false );
        }
        // This attribute decides if the password could be displayed or not

        // Profile is Displayed
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}
