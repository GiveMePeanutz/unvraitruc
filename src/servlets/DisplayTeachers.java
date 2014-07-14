package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;

@WebServlet( "/displayTeachers" )
public class DisplayTeachers extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USER_REQUEST_ATT = "users";
    public static final String VIEW             = "/WEB-INF/displayTeachers.jsp";
    public static final String ACTIVITY_NAME    = "displayTeachers";

    private UserDao            userDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<User> listeUsers = userDao.listGroup( "teacher" );
        LinkedHashMap<String, User> mapUsers = new LinkedHashMap<String, User>();
        for ( User user : listeUsers ) {
            mapUsers.put( user.getUsername(), user );
        }

        request.setAttribute( USER_REQUEST_ATT, mapUsers );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<User> listeUsers = userDao.listGroup( "teacher" );
        Map<String, User> mapUsers = new HashMap<String, User>();
        for ( User user : listeUsers ) {
            mapUsers.put( user.getUsername(), user );
        }

        request.setAttribute( USER_REQUEST_ATT, mapUsers );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}
