package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOException;
import dao.DAOFactory;
import dao.UserDao;

@WebServlet( "/deleteUser" )
public class DeleteUser extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String USERNAME_PARAM   = "username";

    public static final String VIEW             = "/displayUsers";

    private UserDao            userDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération du paramètre */
        String username = getParameterValue( request, USERNAME_PARAM );

        /* Si l'id du client et la Map des clients ne sont pas vides */
        if ( username != null ) {
            try {
                /* Alors suppression du client de la BDD */
                userDao.delete( username );
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
