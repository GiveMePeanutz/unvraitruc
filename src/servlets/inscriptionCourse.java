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
import dao.DAOException;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;
import dao.UserDao;

@WebServlet( "/inscriptionCourse" )
public class InscriptionCourse extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayCourses";

    private UserDao            UserDao;
    private FactTableDao       FactTableDao;
    private DateDao            DateDao;

    public void init() throws ServletException {
        this.UserDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
        this.DateDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getDateDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        String courseName = getParameterValue( request, COURSENAME_PARAM );

        HttpSession session = request.getSession();
        User user = new User();
        user = (User) session.getAttribute( USER_SESSION_ATT );

        String username = user.getUsername();

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( courseName != null && username != null ) {
            try {

                UserDao.addCourse( username, courseName );

                user.addCourseName( courseName );
                session.setAttribute( USER_SESSION_ATT, user );
                Date date = new Date();
                date = DateDao.create();
                FactTableDao.addFact( username, "Course Subscribe", date.getDateID() );

            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }

        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

    /*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
     * contenu sinon.
     */
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