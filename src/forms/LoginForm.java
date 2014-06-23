package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.User;
import dao.DAOException;
import dao.UserDao;

public final class LoginForm {
    private static final String USERNAME_FIELD = "username";
    private static final String PWD_FIELD      = "password";

    private String              result;
    private Map<String, String> errors         = new HashMap<String, String>();
    private UserDao             userDao;

    public LoginForm( UserDao userDao ) {
        this.userDao = userDao;
    }

    public String getResult() {
        return result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public User connectUser( HttpServletRequest request, String path ) {
        /* Récupération des champs du formulaire */
        String username = getFieldValue( request, USERNAME_FIELD );
        String password = getFieldValue( request, PWD_FIELD );

        User user = new User();

        handleUsername( username, user );
        handlePassword( password, user );
        similarityControl( username, password, user );

        try {
            if ( errors.isEmpty() ) {
                userDao.create( user );
                result = "Connection is a success.";
            } else {
                result = "Connection failed.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected error during the login" );
            result = "Connection failed : unexpected error, please try again later.";
            e.printStackTrace();
        }

        return user;
    }

    private void similarityControl( String username, String password, User user ) {

    }

    private void handleUsername( String username, User user ) {
        try {
            usernameValidation( username );
        } catch ( FormExceptionValidation e ) {
            setError( USERNAME_FIELD, e.getMessage() );
        }
        user.setUsername( username );
    }

    private void handlePassword( String password, User user ) {
        try {
            passwordValidation( password );
        } catch ( FormExceptionValidation e ) {
            setError( PWD_FIELD, e.getMessage() );
        }
        user.setPassword( password );
    }

    private void usernameValidation( String username ) throws FormExceptionValidation {
        if ( username != null ) {
            if ( username.length() < 3 ) {
                throw new FormExceptionValidation( "Username must have more than 2 characters" );
            }
        } else {
            throw new FormExceptionValidation( "Please enter a real username." );
        }
    }

    private void passwordValidation( String password ) throws FormExceptionValidation {
        if ( password != null ) {
            if ( password.length() < 3 ) {
                throw new FormExceptionValidation( "Password must have at least 3 characters" );
            }
        } else {
            throw new FormExceptionValidation( "Please enter your password." );
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