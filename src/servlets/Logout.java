package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Encryption;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;

@WebServlet( "/logout" )
public class Logout extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String REDIRECTION_URL  = "/Project/login";
    public static final String ACTIVITY_NAME    = "logout";
    public static final String USER_SESSION_ATT = "userSession";

    private FactTableDao       factTableDao;

    public void init() throws ServletException {
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        Encryption enc = new Encryption();

        /* Récupération et destruction de la session en cours */
        HttpSession session = request.getSession();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername() , "Logout" );

        session.invalidate();

        response.sendRedirect( REDIRECTION_URL );
    }
}