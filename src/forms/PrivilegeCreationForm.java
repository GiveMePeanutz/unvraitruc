package forms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.Priv;
import dao.DAOException;
import dao.PrivDao;

public final class PrivilegeCreationForm {

    private static final String NAME_FIELD        = "privName";
    private static final String DESCRIPTION_FIELD = "privDescription";
    private static final String MENU_FIELD        = "menus";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private PrivDao             privDao;

    public PrivilegeCreationForm( PrivDao privDao ) {
        this.privDao = privDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    public Priv createPriv( HttpServletRequest request, String path ) throws ParseException {

        String privName = getFieldValue( request, NAME_FIELD );
        String privDescription = getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<Integer> menus = getSelectedValues( request, MENU_FIELD );

        Priv priv = new Priv();
        handlePrivName( privName, priv );
        handlePrivDescription( privDescription, priv );
        handleMenus( menus, priv );

        try {
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

    private void privNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    private void privDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    private void menuValidation( ArrayList<Integer> menus ) throws FormValidationException {
        if ( menus == null )
        {
            throw new FormValidationException( "Please choose at least one menu." );
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des errors.
     */
    private void setError( String path, String message ) {
        errors.put( path, message );
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

    private static ArrayList<Integer> getSelectedValues( HttpServletRequest request, String fieldName ) {

        ArrayList<Integer> menus = new ArrayList<Integer>();
        String[] values = request.getParameterValues( fieldName );
        if ( values == null || values.length == 0 )
        {
            return null;
        }

        for ( int i = 0; i < values.length; i++ )
        {
            menus.add( Integer.parseInt( values[i] ) );
        }
        return menus;
    }

}