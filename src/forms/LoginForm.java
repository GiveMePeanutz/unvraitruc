package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import utilities.utilitiesForm;
import beans.User;
import dao.DAOException;
import dao.UserDao;

public final class LoginForm {
    // Fields Name, have to be identical than fields name in JSPs
    private static final String USERNAME_FIELD = "username";
    private static final String PWD_FIELD      = "password";

    private String              result;
    private Map<String, String> errors         = new HashMap<String, String>();
    private UserDao             userDao;
    private utilitiesForm       util           = new utilitiesForm();

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
        // Parameters Recovery
        String username = util.getFieldValue( request, USERNAME_FIELD );
        String password = util.getFieldValue( request, PWD_FIELD );

        User user = new User();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleUsername( username, user );
        handlePassword( password, user );
        // Verifies if it's the right password with the right username.
        handleSimilarity( username, password, user );

        try {
            // if no error, the user is logged in session, else
            // an error message is displayed
            if ( errors.isEmpty() ) {
                result = "Connection is a success.";
                user = userDao.find( username );
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

    // Username check : more than 3 characters
    private void usernameValidation( String username ) throws FormValidationException {
        if ( username != null ) {
            if ( username.length() < 3 ) {
                throw new FormValidationException( "Username must have more than 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a real username." );
        }
    }

    // Password check : more than 3 characters
    private void passwordValidation( String password ) throws FormValidationException {
        if ( password != null ) {
            if ( password.length() < 5 ) {
                throw new FormValidationException( "Password must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter your password." );
        }
    }

    // Verifies if it's the right password with the right username.
    private void similarityValidation( String username, String password ) throws FormValidationException {
        if ( !( userDao.getPassword( username ) != null && password != null && userDao.getPassword( username ).equals(
                password ) ) ) {
            throw new FormValidationException( "This password doesn't match with this username" );
        }

    }

    // Add a message corresponding to the specific field to the error map.
    private void setError( String field, String message ) {
        errors.put( field, message );
    }

}