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
import dao.PrivDao;

@WebServlet( "/deletePriv" )
public class DeletePriv extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PRIVNAME_PARAM   = "privName";
    public static final String USER_SESSION_ATT  = "userSession";

    public static final String VIEW             = "/displayPrivs";
    public static final String ACTIVITY_NAME    = "deletePriv";

    private PrivDao            PrivDao;
    private FactTableDao       factTableDao;
    
    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.PrivDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getPrivDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        String privName = getParameterValue( request, PRIVNAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( privName != null ) {
            try {
                /* Alors suppression du client de la BDD */
                PrivDao.delete( privName );
                
                HttpSession session = request.getSession();
        		User userSession = new User();
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                factTableDao.addFact(userSession.getUsername(), "Privilege Deleted");
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