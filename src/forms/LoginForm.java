package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.User;

public final class LoginForm {
    private static final String USERNAME_FIELD = "username";
    private static final String PWD_FIELD      = "password";

    private String              result;
    private Map<String, String> errors         = new HashMap<String, String>();

    public String getResult() {
        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public User connectUser( HttpServletRequest request ) {
        /* Récupération des champs du formulaire */
        String username = getFieldValue( request, USERNAME_FIELD );
        String password = getFieldValue( request, PWD_FIELD );

        User user = new User();

        /* Validation du champ email. */
        try {
            usernameValidation( username );
        } catch ( Exception e ) {
            setError( USERNAME_FIELD, e.getMessage() );
        }
        user.setUsername( username );

        /* Validation du champ mot de passe. */
        try {
            passwordValidation( password );
        } catch ( Exception e ) {
            setError( PWD_FIELD, e.getMessage() );
        }
        user.setPassword( password );

        /* Initialisation du résultat global de la validation. */
        if ( errors.isEmpty() ) {
            result = "Connection is a success.";
        } else {
            result = "Connection failed.";
        }

        return user;
    }

    /**
     * Valide l'adresse email saisie.
     */
    private void usernameValidation( String username ) throws Exception {
        if ( username != null && username.length() < 3 ) {
            throw new Exception( "Merci de saisir une adresse mail valide." );
        }
    }

    /**
     * Valide le mot de passe saisi.
     */
    private void passwordValidation( String password ) throws Exception {
        if ( password != null ) {
            if ( password.length() < 3 ) {
                throw new Exception( "Password must have at least 3 characters" );
            }
        } else {
            throw new Exception( "Please enter your password." );
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setError( String field, String message ) {
        errors.put( field, message );
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }
}