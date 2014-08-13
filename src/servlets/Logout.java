package servlets;

//Controller for logout

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.DAOFactory;
import dao.TransactionTableDao;

@WebServlet( "/logout" )
public class Logout extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String REDIRECTION_URL  = "/Project/login";
    public static final String ACTIVITY_NAME    = "logout";
    public static final String USER_SESSION_ATT = "userSession";

    private TransactionTableDao       transactionTableDao;

    public void init() throws ServletException {

        this.transactionTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getTransactionTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Session retrieving from the request
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged onn this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        // New action saved in database
        transactionTableDao.addTransaction( userSession.getUsername(), "Logout" );

        // turn off the session
        session.invalidate();

        // Redirection toward the login page
        response.sendRedirect( REDIRECTION_URL );
    }
}