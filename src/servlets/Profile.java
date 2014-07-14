package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;

@WebServlet( urlPatterns = "/profile", initParams = @WebInitParam( name = "path", value = "/files/images/" ) )
public class Profile extends HttpServlet {

    public static final String CONF_DAO_FACTORY        = "daofactory";
    public static final String USERNAME_PARAM          = "username";
    public static final String USER_SESSION_ACCESS_ATT = "userSessionAccess";

    public static final String USER_REQUEST_ATT        = "user";
    public static final String USER_MODIFIABLE_ATT     = "userModifiable";
    public static final String VIEW                    = "/WEB-INF/profile.jsp";
    public static final String ACTIVITY_NAME           = "profile";

    private UserDao            userDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = getParameterValue( request, USERNAME_PARAM );
        User user = userDao.find( username );
        request.setAttribute( USER_REQUEST_ATT, user );

        List<String> menus = ( (List<String>) session.getAttribute( USER_SESSION_ACCESS_ATT ) );

        if ( menus.contains( "Modify User" )
                || ( menus.contains( "Modify Teacher" ) &&
                ( user.getGroupNames().contains( "Teacher" ) || user.getGroupNames().contains( "TeacherOfficial" ) ) )
                || ( menus.contains( "Modify Student" ) &&
                ( user.getGroupNames().contains( "Student" ) || user.getGroupNames().contains( "StudentOfficial" ) ) ) ) {
            request.setAttribute( USER_MODIFIABLE_ATT, true );
        } else {
            request.setAttribute( USER_MODIFIABLE_ATT, false );
        }

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

    }

    private static String getParameterValue( HttpServletRequest request,
            String nomChamp ) {
        String value = request.getParameter( nomChamp );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

}
