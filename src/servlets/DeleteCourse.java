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
import dao.CourseDao;
import dao.DAOException;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;

@WebServlet( "/deleteCourse" )
public class DeleteCourse extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String COURSENAME_PARAM = "courseName";
    public static final String USER_SESSION_ATT  = "userSession";

    public static final String VIEW             = "/displayCourses";
    public static final String ACTIVITY_NAME    = "deleteCourse";

    private CourseDao          CourseDao;
    private DateDao			   dateDao;
    private FactTableDao       factTableDao;
    
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.CourseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
        this.dateDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getDateDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        String courseName = getParameterValue( request, COURSENAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( courseName != null ) {
            try {
            	
                /* Alors suppression du client de la BDD */
                CourseDao.delete( courseName );
                
                Date date = dateDao.create();
                HttpSession session = request.getSession();
        		User userSession = new User();
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                factTableDao.addFact(userSession.getUsername(), "Course Deleted", date.getDateID());
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