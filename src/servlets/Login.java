package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;
import dao.DAOFactory;
import dao.FactTableDao;
import dao.UserDao;
import forms.LoginForm;

@WebServlet( "/login" )
public class Login extends HttpServlet {

    public static final String  CONF_DAO_FACTORY        = "daofactory";
    public static final String  USER_ATT                = "user";
    public static final String  FORM_ATT                = "form";
    public static final String  USER_SESSION_ATT        = "userSession";
    public static final String  USER_SESSION_ACCESS_ATT = "userSessionAccess";
    public static final String  VIEW                    = "/WEB-INF/login.jsp";
    private static final String PATH                    = "path";
    public static final String  ACTIVITY_NAME           = "login";

    private UserDao             userDao;
    private FactTableDao        FactTableDao;

    public void init() throws ServletException {
        /* Récupération d'une instance de notre DAO Utilisateur */
        this.userDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getUserDao();
        this.FactTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) ).getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        String path = this.getServletConfig().getInitParameter( PATH );

        /* Préparation de l'objet formulaire */
        LoginForm form = new LoginForm( userDao );
        /* Traitement de la requête et récupération du bean en résultant */
        User user = form.connectUser( request, path );

        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /*
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if ( form.getErrors().isEmpty() ) {
            session.setAttribute( USER_SESSION_ATT, user );
            List<String> menus = userDao.listAccMenus( user.getUsername() );
            session.setAttribute( USER_SESSION_ACCESS_ATT, menus );

        } else {
            session.setAttribute( USER_SESSION_ATT, null );
        }

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( FORM_ATT, form );
        request.setAttribute( USER_ATT, user );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

}
