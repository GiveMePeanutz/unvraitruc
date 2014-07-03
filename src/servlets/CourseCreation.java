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
    public static final String GROUP_ATT         = "course";
    public static final String FORM_ATT          = "form";
    public static final String VUE_SUCCESS       = "/displayCourses";
    public static final String VUE_FORM          = "/WEB-INF/createCourse.jsp";
    public static final String GROUP_REQUEST_ATT = "courses";

    private CourseDao          courseDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */

        this.courseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getCourseDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

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
        request.setAttribute( GROUP_ATT, course );
        request.setAttribute( FORM_ATT, form );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() )
        {
            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCESS ).forward( request, response );
        }
        else
        {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}