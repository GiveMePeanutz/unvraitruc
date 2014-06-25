package servlets;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Priv;
import dao.DAOFactory;
import dao.PrivDao;
import forms.PrivilegeCreationForm;

@WebServlet( urlPatterns = "/privilegeCreation" )
public class PrivilegeCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PATH             = "path";
    public static final String PRIV_ATT         = "priv";
    public static final String FORM_ATT         = "form";
    public static final String VUE_SUCCESS      = "/WEB-INF/displayPrivileges.jsp";
    public static final String VUE_FORM         = "/WEB-INF/createPrivilege.jsp";

    private PrivDao            privDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* À la réception d'une requête GET, simple affichage du formulaire */
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /*
         * Lecture du paramètre 'path' passé à la servlet via la déclaration
         * dans le web.xml
         */
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Préparation de l'objet formulaire */
        PrivilegeCreationForm form = new PrivilegeCreationForm( privDao );

        /* Traitement de la requête et récupération du bean en résultant */
        Priv priv = null;
        try {
            priv = form.createPriv( request, path );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( PRIV_ATT, priv );
        request.setAttribute( FORM_ATT, form );

        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {

            /* Affichage de la fiche récapitulative */
            this.getServletContext().getRequestDispatcher( VUE_SUCCESS ).forward( request, response );
        } else {
            /* Sinon, ré-affichage du formulaire de création avec les erreurs */
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}