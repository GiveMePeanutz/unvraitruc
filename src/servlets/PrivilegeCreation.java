package servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import staticData.Menu;
import beans.Date;
import beans.Priv;
import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.PrivDao;
import forms.PrivilegeCreationForm;

@WebServlet( urlPatterns = "/privCreation" )
public class PrivilegeCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PATH             = "path";
    public static final String PRIV_ATT         = "priv";
    public static final String FORM_ATT         = "form";
    public static final String MENU_REQUEST_ATT = "menus";
    public static final String PRIVNAME_PARAM   = "privName";
    public static final String VERIFY_PARAM     = "modify";
    public static final String VERIFY_PARAM2    = "Create";
    public static final String USER_SESSION_ATT  = "userSession";


    public static final String VUE_SUCCESS      = "/Project/displayPrivs";
    public static final String VUE_FORM         = "/WEB-INF/createPrivilege.jsp";
    public static final String ACTIVITY_NAME    = "privCreation";

    private PrivDao            privDao;
    private FactTableDao       factTableDao;


    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String modifiable = getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            String privName = getParameterValue( request, PRIVNAME_PARAM );
            Priv priv = privDao.find( privName );
            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( PRIV_ATT, priv );

        }

        Map<Integer, String> mapMenus = Menu.list();

        request.setAttribute( MENU_REQUEST_ATT, mapMenus );
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
        String modify = request.getParameter( VERIFY_PARAM2 );
        if ( modify.equals( "Modify" ) )
        {
            try {
                priv = form.modifyPriv( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else {
            try {
                priv = form.createPriv( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( PRIV_ATT, priv );
        request.setAttribute( FORM_ATT, form );

        LinkedHashMap<Integer, String> mapMenus = Menu.list();

        request.setAttribute( MENU_REQUEST_ATT, mapMenus );

        HttpSession session = request.getSession();
		User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        
        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {
        	if ( modify.equals( "Modify" ) )
            {
        		factTableDao.addFact(userSession.getUsername(), "Privilege modified");
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Privilege created");
            }
            response.sendRedirect( VUE_SUCCESS );
        } else {

            if ( modify.equals( "Modify" ) )
            {
            	factTableDao.addFact(userSession.getUsername(), "Privilege modification errors");
                request.setAttribute( VERIFY_PARAM, "true" );
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Privilege creation errors");
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