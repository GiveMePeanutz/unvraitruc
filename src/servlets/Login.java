package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import beans.User;
import forms.LoginForm;

@WebServlet( "/login" )
public class Login extends HttpServlet {
    public static final String USER_ATT           = "user";
    public static final String FORM_ATT           = "form";
    public static final String LOGIN_INTERVAL_ATT = "loginInterval";
    public static final String USER_SESSION_ATT   = "userSession";
    public static final String LAST_LOGIN_COOKIE  = "lastLogin";
    public static final String DATE_FORMAT        = "dd/MM/yyyy HH:mm:ss";
    public static final String VIEW               = "/WEB-INF/login.jsp";
    public static final String MEMORY_FIELD       = "memory";
    public static final int    MAX_AGE_COOKIE     = 60 * 60 * 24 * 365;   // 1
                                                                           // year

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Tentative de récupération du cookie depuis la requête */
        String lastLogin = getCookieValue( request, LAST_LOGIN_COOKIE );
        /* Si le cookie existe, alors calcul de la durée */
        if ( lastLogin != null ) {
            /* Récupération de la date courante */
            DateTime currentDate = new DateTime();
            /* Récupération de la date présente dans le cookie */
            DateTimeFormatter formatter = DateTimeFormat.forPattern( DATE_FORMAT );
            DateTime lastLoginDate = formatter.parseDateTime( lastLogin );
            /* Calcul de la durée de l'intervalle */
            Period periode = new Period( lastLoginDate, currentDate );
            /* Formatage de la durée de l'intervalle */
            PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                    .appendYears().appendSuffix( " year ", " years " )
                    .appendMonths().appendSuffix( " month ", "months" )
                    .appendDays().appendSuffix( " day ", " days " )
                    .appendHours().appendSuffix( " hour ", " hours " )
                    .appendMinutes().appendSuffix( " minute ", " minutes " )
                    .appendSeparator( "and " )
                    .appendSeconds().appendSuffix( " second", " seconds" )
                    .toFormatter();
            String loginInterval = periodFormatter.print( periode );
            /* Ajout de l'intervalle en tant qu'attribut de la requête */
            request.setAttribute( LOGIN_INTERVAL_ATT, loginInterval );
        }
        /* Affichage de la page de connexion */
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        LoginForm form = new LoginForm();
        /* Traitement de la requête et récupération du bean en résultant */
        User user = form.connectUser( request );
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();

        /*
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if ( form.getErrors().isEmpty() ) {
            session.setAttribute( USER_SESSION_ATT, user );
        } else {
            session.setAttribute( USER_SESSION_ATT, null );
        }

        /* Si et seulement si la case du formulaire est cochée */
        if ( request.getParameter( MEMORY_FIELD ) != null ) {
            /* Récupération de la date courante */
            DateTime dt = new DateTime();
            /* Formatage de la date et conversion en texte */
            DateTimeFormatter formatter = DateTimeFormat.forPattern( DATE_FORMAT );
            String lastLoginDate = dt.toString( formatter );
            /* Création du cookie, et ajout à la réponse HTTP */
            setCookie( response, LAST_LOGIN_COOKIE, lastLoginDate, MAX_AGE_COOKIE );
        } else {
            /* Demande de suppression du cookie du navigateur */
            setCookie( response, LAST_LOGIN_COOKIE, "", 0 );
        }

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute( FORM_ATT, form );
        request.setAttribute( USER_ATT, user );

        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
    }

    /*
     * Méthode utilitaire gérant la création d'un cookie et son ajout à la
     * réponse HTTP.
     */
    private static void setCookie( HttpServletResponse response, String name, String value, int maxAge ) {
        Cookie cookie = new Cookie( name, value );
        cookie.setMaxAge( maxAge );
        response.addCookie( cookie );
    }

    /**
     * Méthode utilitaire gérant la récupération de la valeur d'un cookie donné
     * depuis la requête HTTP.
     */
    private static String getCookieValue( HttpServletRequest request, String name ) {
        Cookie[] cookies = request.getCookies();
        if ( cookies != null ) {
            for ( Cookie cookie : cookies ) {
                if ( cookie != null && name.equals( cookie.getName() ) ) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
