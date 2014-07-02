package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import dao.DAOFactory;
import dao.UserDao;

@WebServlet( "/profile" )
public class Profile extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USERNAME_PARAM   = "username";

    public static final String USER_REQUEST_ATT = "user";
    public static final String VIEW             = "/WEB-INF/profile.jsp";

    private UserDao            userDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String username = getParameterValue( request, USERNAME_PARAM );
        User user = userDao.find( username );
        request.setAttribute( USER_REQUEST_ATT, user );

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
