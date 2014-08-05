package forms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import utilities.UtilitiesForm;
import beans.Priv;
import dao.DAOException;
import dao.PrivDao;

public final class PrivilegeCreationForm {

    // Fields Name, have to be identical than fields name in JSPs
    private static final String NAME_FIELD        = "privName";
    private static final String DESCRIPTION_FIELD = "privDescription";
    private static final String MENU_FIELD        = "menus";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private PrivDao             privDao;
    private UtilitiesForm       util              = new UtilitiesForm();

    public PrivilegeCreationForm( PrivDao privDao ) {
        this.privDao = privDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    // Method to create a privilege
    public Priv createPriv( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String privName = util.getFieldValue( request, NAME_FIELD );
        String privDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<Integer> menus = util.getIntSelectedValues( request, MENU_FIELD );

        Priv priv = new Priv();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handlePrivName( privName, priv );
        handlePrivDescription( privDescription, priv );
        handleMenus( menus, priv );

        try {
            // if no error, the privilege is created and saved in database, else
            // an
            // error message is displayed
            if ( errors.isEmpty() ) {
                privDao.create( priv );
                result = "Priv creation succeed";
            } else {
                result = "Priv creation failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Priv creation failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return priv;
    }

    // Method to modify a privilege
    public Priv modifyPriv( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String privName = util.getFieldValue( request, NAME_FIELD );
        String privDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<Integer> menus = util.getIntSelectedValues( request, MENU_FIELD );

        Priv priv = new Priv();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handlePrivName( privName, priv );
        handlePrivDescription( privDescription, priv );
        handleMenus( menus, priv );

        try {
            // if no error, the privilege is modified and saved in database,
            // else an error message is displayed
            if ( errors.isEmpty() ) {
                privDao.modify( priv );
                result = "Priv modification succeed";
            } else {
                result = "Priv modification failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Priv modification failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return priv;
    }

    private void handlePrivName( String privName, Priv priv ) {
        try {
            privNameValidation( privName );
        } catch ( FormValidationException e ) {
            setError( NAME_FIELD, e.getMessage() );
        }
        priv.setPrivName( privName );
    }

    private void handlePrivDescription( String privDescription, Priv priv ) {
        try {
            privDescriptionValidation( privDescription );
        } catch ( FormValidationException e ) {
            setError( DESCRIPTION_FIELD, e.getMessage() );
        }
        priv.setPrivDescription( privDescription );
    }

    private void handleMenus( ArrayList<Integer> menus, Priv priv ) {
        try {
            menuValidation( menus );
        } catch ( FormValidationException e ) {
            setError( MENU_FIELD, e.getMessage() );
        }
        priv.setMenuPaths( menus );

    }

    // Privilege Name check : more than 2 characters
    private void privNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    // Privilege Description check : more than 5 characters
    private void privDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    // Privilege menus check : one at least
    private void menuValidation( ArrayList<Integer> menus ) throws FormValidationException {
        if ( menus == null )
        {
            throw new FormValidationException( "Please choose at least one menu." );
        }
    }

    // Add a message corresponding to the specific field to the error map.
    private void setError( String path, String message ) {
        errors.put( path, message );
    }

}