package servlets;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Course;
import dao.CourseDao;
import dao.DAOFactory;
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

    private CourseDao          courseDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */

        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String modifiable = getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            String courseName = getParameterValue( request, COURSENAME_PARAM );
            Course course = courseDao.find( courseName );
            courseDao.delete( courseName );
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
        try {
            course = form.createCourse( request, path );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( COURSE_ATT, course );
        request.setAttribute( FORM_ATT, form );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() )
        {
            /* Affichage de la fiche récapitulative */
            response.sendRedirect( VUE_SUCCESS );

            // this.getServletContext().getRequestDispatcher( VUE_SUCCESS
            // ).forward( request, response );
        }
        else
        {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
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