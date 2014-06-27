package forms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import beans.Group;
import dao.DAOException;
import dao.GroupDao;

public final class GroupCreationForm {

    private static final String NAME_FIELD        = "groupName";
    private static final String DESCRIPTION_FIELD = "groupDescription";
    private static final String PRIV_FIELD        = "privs";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private GroupDao            groupDao;

    public GroupCreationForm( GroupDao groupDao ) {
        this.groupDao = groupDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResultat() {
        return result;
    }

    public Group createGroup( HttpServletRequest request, String path ) throws ParseException {

        String groupName = getFieldValue( request, NAME_FIELD );
        String groupDescription = getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<String> privileges = getSelectedValues( request, PRIV_FIELD );

        Group group = new Group();
        handleGroupName( groupName, group );
        handleGroupDescription( groupDescription, group );
        handlePrivs( privileges, group );

        try {
            if ( errors.isEmpty() ) {
                groupDao.create( group );
                result = "Group creation succeed";
            } else {
                result = "Group creation failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Group creation failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return group;
    }

    private void handleGroupName( String groupName, Group group ) {
        try {
            groupNameValidation( groupName );
        } catch ( FormValidationException e ) {
            setError( NAME_FIELD, e.getMessage() );
        }
        group.setGroupName( groupName );
    }

    private void handleGroupDescription( String groupDescription, Group group ) {
        try {
            groupDescriptionValidation( groupDescription );
        } catch ( FormValidationException e ) {
            setError( DESCRIPTION_FIELD, e.getMessage() );
        }
        group.setGroupDescription( groupDescription );
    }

    private void handlePrivs( ArrayList<String> privileges, Group group ) {
        try {
            privValidation( privileges );
        } catch ( FormValidationException e ) {
            setError( PRIV_FIELD, e.getMessage() );
        }
        group.setPrivNames( privileges );

    }

    private void groupNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    private void groupDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    private void privValidation( ArrayList<String> privileges ) throws FormValidationException {
        if ( privileges.isEmpty() )
        {
            throw new FormValidationException( "Please choose at least one privilege." );
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

    private static ArrayList<String> getSelectedValues( HttpServletRequest request, String fieldName ) {

        ArrayList<String> privileges = new ArrayList<String>();
        String[] values = request.getParameterValues( fieldName );
        if ( values == null || values.length == 0 )
        {
            return null;
        }
        for ( int i = 0; i < values.length; i++ )
        {
            privileges.add( values[i] );
        }
        return privileges;
    }

}