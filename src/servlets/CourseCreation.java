package servlets;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Course;
import beans.Date;
import beans.User;
import dao.CourseDao;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;
import forms.CourseCreationForm;

@WebServlet( urlPatterns = "/courseCreation" )
public class CourseCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PATH              = "path";
    public static final String COURSE_ATT        = "course";
    public static final String FORM_ATT          = "form";
    public static final String VUE_SUCCESS       = "/Project/displayCourses";
    public static final String VUE_FORM          = "/WEB-INF/createCourse.jsp";
    public static final String GROUP_REQUEST_ATT = "courses";
    public static final String VERIFY_PARAM      = "modify";
    public static final String COURSENAME_PARAM  = "courseName";
    public static final String VERIFY_PARAM2     = "Create";
    public static final String USER_SESSION_ATT  = "userSession";

    public static final String ACTIVITY_NAME     = "courseCreation";

    private CourseDao          courseDao;
    private FactTableDao       factTableDao;
    private DateDao			   dateDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */

        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
        this.dateDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getDateDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String modifiable = getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            String courseName = getParameterValue( request, COURSENAME_PARAM );
            Course course = courseDao.find( courseName );
            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( COURSE_ATT, course );
        }

        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'path' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Préparation de l'objet formulaire */
        CourseCreationForm form = new CourseCreationForm( courseDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Course course = null;
        String modify = request.getParameter( VERIFY_PARAM2 );
        if ( modify.equals( "Modify" ) )
        {
            try {
                course = form.modifyCourse( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else {
            try {
                course = form.createCourse( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( COURSE_ATT, course );
        request.setAttribute( FORM_ATT, form );

        Date date = dateDao.create();
        HttpSession session = request.getSession();
		User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        
        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {
        	if ( modify.equals( "Modify" ) )
            {
        		factTableDao.addFact(userSession.getUsername(), "Course modified", date.getDateID());
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Course created", date.getDateID());
            }
            response.sendRedirect( VUE_SUCCESS );
        } else {

            if ( modify.equals( "Modify" ) )
            {
            	factTableDao.addFact(userSession.getUsername(), "Course modification errors", date.getDateID());
                request.setAttribute( VERIFY_PARAM, "true" );
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Course creation errors", date.getDateID());
            }

            /*
             * Sinon, ré-affichage du formulaire de création avec les erreurs
             */
            
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
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