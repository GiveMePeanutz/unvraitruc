package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CourseDao;
import dao.DAOException;
import dao.DAOFactory;
import dao.UserDao;

@WebServlet( "/inscriptionCourse" )
public class inscriptionCourse extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USERNAME_PARAM   = "username";

    public static final String VIEW             = "/displayCourses";

    private CourseDao          CourseDao;
    private UserDao            UserDao;

    public void init() throws ServletException {
        this.UserDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();

        this.CourseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        String courseName = getParameterValue( request, COURSENAME_PARAM );
        String username = getParameterValue( request, USERNAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( courseName != null && username != null ) {
            try {

                CourseDao.delete( courseName );
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