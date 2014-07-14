package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Group;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.GroupDao;
import dao.UserDao;
import forms.FormValidationException;
import forms.UserCreationForm;

@WebServlet( urlPatterns = "/teacherCreation", initParams = @WebInitParam( name = "path", value = "/files/images/" ) )
@MultipartConfig( location = "c:/files/images", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 5, fileSizeThreshold = 1024 * 1024 * 3 )
public class TeacherCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PATH              = "path";
    public static final String USER_ATT          = "user";
    public static final String FORM_ATT          = "form";
    public static final String GROUP_REQUEST_ATT = "groups";
    public static final String USERNAME_PARAM    = "username";
    public static final String VERIFY_PARAM      = "modify";
    public static final String VERIFY_PARAM2     = "Create";

    public static final String VUE_SUCCESS       = "/Project/displayTeachers";
    public static final String VUE_FORM          = "/WEB-INF/createTeacher.jsp";
    public static final String ACTIVITY_NAME     = "teacherCreation";

    private UserDao            userDao;
    private GroupDao           groupDao;
    private FactTableDao       FactTableDao;

    public void init() throws ServletException {
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String modifiable = getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            String userName = getParameterValue( request, USERNAME_PARAM );
            User user = userDao.find( userName );
            request.setAttribute( USER_ATT, user );
            request.setAttribute( VERIFY_PARAM, modifiable );
        }

        List<Group> listeGroup = groupDao.list();
        Map<String, Group> mapGroups = new HashMap<String, Group>();
        for ( Group group : listeGroup ) {
            mapGroups.put( group.getGroupName(), group );
        }

        request.setAttribute( GROUP_REQUEST_ATT, mapGroups );

        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'path' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Préparation de l'objet formulaire */
        UserCreationForm form = new UserCreationForm( userDao );

        /* Traitement de la requête et récupération du bean en résultant */
        User user = null;
        String modify = request.getParameter( VERIFY_PARAM2 );
        if ( modify.equals( "Modify" ) )
        {
            try {

                user = form.modifyUser( request, path );

            } catch ( ParseException e ) {
                e.printStackTrace();
            } catch ( FormValidationException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            try {
                user = form.createUser( request, path );
            } catch ( ParseException | FormValidationException e ) {
                e.printStackTrace();
            }
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( USER_ATT, user );
        request.setAttribute( FORM_ATT, form );

        if ( request.getAttribute( GROUP_REQUEST_ATT ) == null ) {
            List<Group> listGroup = groupDao.list();
            Map<String, Group> mapPrivs = new HashMap<String, Group>();
            for ( Group group : listGroup ) {
                mapPrivs.put( group.getGroupName(), group );
            }
            request.setAttribute( GROUP_REQUEST_ATT, mapPrivs );
        }

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {
            response.sendRedirect( VUE_SUCCESS );

            // this.getServletContext().getRequestDispatcher( VUE_SUCCESS
            // ).forward( request, response );
        } else {

            if ( modify.equals( "Modify" ) )
            {
                request.setAttribute( VERIFY_PARAM, "true" );
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