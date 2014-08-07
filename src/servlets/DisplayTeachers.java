package servlets;

//Controller of teachers display 

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.Encryption;
import beans.User;
import dao.DAOFactory;
import dao.UserDao;

@WebServlet( "/displayTeachers" )
public class DisplayTeachers extends HttpServlet {

    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USER_REQUEST_ATT = "users";
    public static final String VIEW             = "/WEB-INF/displayTeachers.jsp";

    private UserDao            userDao;

    public void init() throws ServletException {
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        Encryption enc = new Encryption();

        // Teacher list retrieving and saving in the request as a
        // LinkedHashMap(key = encrypted username)
        List<User> listeUsers = userDao.listGroup( "teacher" );
        LinkedHashMap<String, User> mapUsers = new LinkedHashMap<String, User>();
        for ( User user : listeUsers )
        {
            mapUsers.put( enc.encrypt( user.getUsername() ), user );
        }
        request.setAttribute( USER_REQUEST_ATT, mapUsers );

        // Teacher list display
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

}
