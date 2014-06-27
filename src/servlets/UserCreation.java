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
import dao.GroupDao;
import dao.UserDao;
import forms.FormValidationException;
import forms.UserCreationForm;

@WebServlet( urlPatterns = "/userCreation", initParams = @WebInitParam( name = "path", value = "/fichiers/images/" ) )
@MultipartConfig( location = "c:/fichiers/images", maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 5, fileSizeThreshold = 1024 * 1024 * 3 )
public class UserCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PATH              = "path";
    public static final String USER_ATT          = "user";
    public static final String FORM_ATT          = "form";
    public static final String GROUP_REQUEST_ATT = "groups";

    public static final String VUE_SUCCESS       = "/WEB-INF/displayUsers.jsp";
    public static final String VUE_FORM          = "/WEB-INF/createUser.jsp";

    private UserDao            userDao;
    private GroupDao           groupDao;

    public void init() throws ServletException {
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();

        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
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
        try {
            user = form.createUser( request, path );
        } catch ( ParseException | FormValidationException e ) {
            e.printStackTrace();
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( USER_ATT, user );
        request.setAttribute( FORM_ATT, form );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCESS ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
        	List<Group> listeGroup = groupDao.list();
            Map<String, Group> mapGroups = new HashMap<String, Group>();
            for ( Group group : listeGroup ) {
                mapGroups.put( group.getGroupName(), group );
            }

            request.setAttribute( GROUP_REQUEST_ATT, mapGroups );
        	this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}