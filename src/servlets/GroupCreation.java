package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Group;
import beans.Priv;
import dao.DAOFactory;
import dao.GroupDao;
import dao.PrivDao;
import forms.GroupCreationForm;

@WebServlet( urlPatterns = "/groupCreation" )
public class GroupCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PATH             = "path";
    public static final String GROUP_ATT        = "group";
    public static final String FORM_ATT         = "form";
    public static final String PRIV_REQUEST_ATT = "privs";
    public static final String VUE_SUCCESS      = "/WEB-INF/displayGroups.jsp";
    public static final String VUE_FORM         = "/WEB-INF/createGroup.jsp";

    private GroupDao           groupDao;
    private PrivDao            privDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();

        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        List<Priv> listePriv = privDao.list();
        Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
        for ( Priv priv : listePriv ) {
            mapPrivs.put( priv.getPrivName(), priv );
        }

        request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'path' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Préparation de l'objet formulaire */
        GroupCreationForm form = new GroupCreationForm( groupDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Group group = null;
        try {
            group = form.createGroup( request, path );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( GROUP_ATT, group );
        request.setAttribute( FORM_ATT, form );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCESS ).forward( request, response );
        } else {
            List<Priv> listePriv = privDao.list();
            Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
            for ( Priv priv : listePriv ) {
                mapPrivs.put( priv.getPrivName(), priv );
            }

            request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}