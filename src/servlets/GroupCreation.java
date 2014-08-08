package servlets;

//Controller of group creation 

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

import utilities.UtilitiesForm;
import beans.Group;
import beans.Priv;
import beans.User;
import dao.DAOFactory;
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
    private UtilitiesForm      util              = new UtilitiesForm();

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.privDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.groupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Boolean parameter retrieving from URL : true if it's a modification
        String modifiable = util.getParameterValue( request, VERIFY_PARAM );
        if ( modifiable != null && modifiable.equals( "true" ) )
        {
            // Group Name retrieving from URL
            String groupName = util.getParameterValue( request, GROUPNAME_PARAM );

            // Searching of the group corresponding to the group name
            Group group = groupDao.find( groupName );

            request.setAttribute( VERIFY_PARAM, modifiable );
            request.setAttribute( GROUP_ATT, group );
        }

        // Privilege list retrieving and saving in the request as a
        // LinkedHashMap(key = privilege name)
        List<Priv> listePriv = privDao.list();
        Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
        for ( Priv priv : listePriv ) {
            mapPrivs.put( priv.getPrivName(), priv );
        }
        request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );

        // Creation form display
        this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // path = urlPatterns annotation parameter
        String path = this.getServletConfig().getInitParameter( PATH );

        /* Preparation of the form object */
        GroupCreationForm form = new GroupCreationForm( groupDao );

        Group group = null;

        // Value of Create Button retrieving
        String modify = request.getParameter( VERIFY_PARAM2 );

        if ( modify.equals( "Modify" ) )// modification
        {
            try {
                group = form.modifyGroup( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }
        else // creation
        {

            try {
                group = form.createGroup( request, path );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
        }

        request.setAttribute( GROUP_ATT, group );
        request.setAttribute( FORM_ATT, form );

        // if mapPrivs is not created yet
        if ( request.getAttribute( PRIV_REQUEST_ATT ) == null )
        {
            // Privilege list retrieving and saving in the request as a
            // LinkedHashMap(key = privilege name)
            List<Priv> listePriv = privDao.list();
            Map<String, Priv> mapPrivs = new HashMap<String, Priv>();
            for ( Priv priv : listePriv ) {
                mapPrivs.put( priv.getPrivName(), priv );
            }
            request.setAttribute( PRIV_REQUEST_ATT, mapPrivs );
        }

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        // userSession = user logged onn this session
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );

        // if no error
        if ( form.getErrors().isEmpty() ) // if there is no error after the
                                          // verification...
        {
            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Group modified" );
            }
            else
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Group created" );
            }

            // Redirection toward the group list
            response.sendRedirect( VUE_SUCCESS );
        }
        else // if there is at least one error
        {

            if ( modify.equals( "Modify" ) )// and if this is a modification
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Group modification errors" );

                // Specifies that it's still a modification
                request.setAttribute( VERIFY_PARAM, "true" );
            }
            else
            {
                // New action saved in database
                factTableDao.addFact( userSession.getUsername(), "Group creation errors" );
            }

            // else forwarding toward the creation form
            this.getServletContext().getRequestDispatcher( VUE_FORM ).forward( request, response );
        }
    }
}
