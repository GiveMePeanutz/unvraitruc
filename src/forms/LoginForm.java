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
        handleSimilarity( username, password, user );

        try {
            if ( errors.isEmpty() ) {
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

    private void handleSimilarity( String username, String password, User user )
    {
        try {
            similarityValidation( username, password );
        } catch ( FormValidationException e )
        {
            setError( PWD_FIELD, e.getMessage() );
        }

    }

    private void handleUsername( String username, User user ) {
        try {
            usernameValidation( username );
        } catch ( FormValidationException e ) {
            setError( USERNAME_FIELD, e.getMessage() );
        }
        user.setUsername( username );
    }

    private void handlePassword( String password, User user ) {
        try {
            passwordValidation( password );
        } catch ( FormValidationException e ) {
            setError( PWD_FIELD, e.getMessage() );
        }
        user.setPassword( password );
    }

    private void usernameValidation( String username ) throws FormValidationException {
        if ( username != null ) {
            if ( username.length() < 3 ) {
                throw new FormValidationException( "Username must have more than 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a real username." );
        }
    }

    private void passwordValidation( String password ) throws FormValidationException {
        if ( password != null ) {
            if ( password.length() < 3 ) {
                throw new FormValidationException( "Password must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter your password." );
        }
    }

    private void similarityValidation( String username, String password ) throws FormValidationException {
        if ( !( userDao.getPassword( username ) == password ) ) {
            throw new FormValidationException( "This password doesn't match with this username" );
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