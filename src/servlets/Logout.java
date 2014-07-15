package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Date;
import beans.User;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;

@WebServlet( "/logout" )
public class Logout extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String REDIRECTION_URL  = "/Project/login";
    public static final String ACTIVITY_NAME    = "logout";
    public static final String USER_SESSION_ATT = "userSession";

    private FactTableDao       factTableDao;
    private DateDao            dateDao;

    public void init() throws ServletException {
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
        this.dateDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getDateDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Récupération et destruction de la session en cours */
        HttpSession session = request.getSession();
        session.invalidate();

        Date date = dateDao.create();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername(), "Logout", date.getDateID() );
        response.sendRedirect( REDIRECTION_URL );
    }
}