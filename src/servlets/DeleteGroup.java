package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Date;
import beans.User;
import dao.DAOException;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.GroupDao;

@WebServlet( "/deleteGroup" )
public class DeleteGroup extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String GROUPNAME_PARAM  = "groupName";
    public static final String USER_SESSION_ATT  = "userSession";

    public static final String VIEW             = "/displayGroups";
    public static final String ACTIVITY_NAME    = "deleteGroup";

    private GroupDao           GroupDao;
    private FactTableDao       factTableDao;
    
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.GroupDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getGroupDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        String groupName = getParameterValue( request, GROUPNAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( groupName != null ) {
            try {
                /* Alors suppression du client de la BDD */
                GroupDao.delete( groupName );
                
                HttpSession session = request.getSession();
        		User userSession = new User();
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                factTableDao.addFact(userSession.getUsername(), "Group Deleted");
            } catch ( DAOException e ) {
                e.printStackTrace();
            }
        }
        /* Redirection vers la fiche récapitulative */
        response.sendRedirect( request.getContextPath() + VIEW );
    }

    /*
     * Méthode utilitaire qui retourne null si un paramètre est vide, et son
     * contenu sinon.
     */
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