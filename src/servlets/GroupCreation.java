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
import javax.servlet.http.HttpSession;

import beans.Date;
import beans.Group;
import beans.Priv;
import beans.User;
import dao.DAOFactory;
import dao.DateDao;
import dao.FactTableDao;
import dao.GroupDao;
import dao.PrivDao;
import forms.GroupCreationForm;

@WebServlet( urlPatterns = "/groupCreation" )
public class GroupCreation extends HttpServlet {
    public static final String CONF_DAO_FACTORY  = "daofactory";
    public static final String PATH              = "path";
    public static final String GROUP_ATT         = "group";
    public static final String FORM_ATT          = "form";
    public static final String PRIV_REQUEST_ATT  = "privs";
    public static final String VUE_SUCCESS       = "/Project/displayGroups";
    public static final String VUE_FORM          = "/WEB-INF/createGroup.jsp";
    public static final String GROUP_REQUEST_ATT = "groups";
    public static final String GROUPNAME_PARAM   = "groupName";
    public static final String VERIFY_PARAM      = "modify";
    public static final String VERIFY_PARAM2     = "Create";
    public static final String ACTIVITY_NAME     = "groupCreation";
    public static final String USER_SESSION_ATT  = "userSession";


    private GroupDao           groupDao;
    private PrivDao            privDao;
    private FactTableDao       factTableDao;
    private DateDao 		   dateDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.dateDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getDateDao();
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        String modifiable = getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            String groupName = getParameterValue( request, GROUPNAME_PARAM );
            Group group = groupDao.find( groupName );
            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( GROUP_ATT, group );
        }

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

        Group group = null;
        String modify = request.getParameter( VERIFY_PARAM2 );
        if ( modify.equals( "Modify" ) )
        {
            try {
                group = form.modifyGroup( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else {

            try {
                group = form.createGroup( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        /* Ajout du bean et de l'objet métier à l'objet requête */
        request.setAttribute( GROUP_ATT, group );
        request.setAttribute( FORM_ATT, form );

        if ( request.getAttribute( PRIV_REQUEST_ATT ) == null ) {
            List<Priv> listePriv = privDao.list();
            Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
            for ( Priv priv : listePriv ) {
                mapPrivs.put( priv.getPrivName(), priv );
            }
            request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );
        }

        Date date = dateDao.create();
        HttpSession session = request.getSession();
		User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        
        /* Si aucune erreur */
        if ( form.getErrors().isEmpty() ) {
        	if ( modify.equals( "Modify" ) )
            {
        		factTableDao.addFact(userSession.getUsername(), "Group modified", date.getDateID());
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Group created", date.getDateID());
            }
            response.sendRedirect( VUE_SUCCESS );
        } else {

            if ( modify.equals( "Modify" ) )
            {
            	factTableDao.addFact(userSession.getUsername(), "Group modification errors", date.getDateID());
                request.setAttribute( VERIFY_PARAM, "true" );
            }else{
            	factTableDao.addFact(userSession.getUsername(), "Group creation errors", date.getDateID());
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
