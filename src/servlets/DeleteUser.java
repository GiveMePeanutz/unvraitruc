package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Encryption;
import beans.User;
import dao.DAOException;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;

@WebServlet( "/deleteUser" )
public class DeleteUser extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USERNAME_PARAM   = "username";
    public static final String USER_SESSION_ATT = "userSession";

    public static final String VIEW             = "/displayUsers";
    public static final String ACTIVITY_NAME    = "deleteUser";

    private UserDao            userDao;
    private FactTableDao       factTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();

    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        Encryption enc = new Encryption();
        /* Récupération du paramètre */
        String username = getParameterValue( request, USERNAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( username != null ) {
            try {
                /* Alors suppression du client de la BDD */
                userDao.delete( enc.decrypt( username ) );

                HttpSession session = request.getSession();
                User userSession = new User();
                userSession = (User) session.getAttribute( USER_SESSION_ATT );
                factTableDao.addFact( userSession.getUsername(), "User Deleted" );
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
