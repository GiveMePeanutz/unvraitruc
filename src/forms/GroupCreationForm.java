package forms;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import utilities.utilitiesForm;
import beans.Group;
import dao.DAOException;
import dao.GroupDao;

public final class GroupCreationForm {

    // Fields Name, have to be identical than fields name in JSPs
    private static final String NAME_FIELD        = "groupName";
    private static final String DESCRIPTION_FIELD = "groupDescription";
    private static final String PRIV_FIELD        = "privileges";

    private String              result;
    private Map<String, String> errors            = new HashMap<String, String>();
    private GroupDao            groupDao;
    private utilitiesForm       util              = new utilitiesForm();

    public GroupCreationForm( GroupDao groupDao ) {
        this.groupDao = groupDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    public Group createGroup( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String groupName = util.getFieldValue( request, NAME_FIELD );
        String groupDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<String> privileges = util.getSelectedValues( request, PRIV_FIELD );

        Group group = new Group();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleGroupName( groupName, group );
        handleGroupDescription( groupDescription, group );
        handlePrivs( privileges, group );

        try {
            // if no error, the group is created and saved in database, else an
            // error message is displayed
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

    public Group modifyGroup( HttpServletRequest request, String path ) throws ParseException {

        // Parameters Recovery
        String groupName = util.getFieldValue( request, NAME_FIELD );
        String groupDescription = util.getFieldValue( request, DESCRIPTION_FIELD );
        ArrayList<String> privileges = util.getSelectedValues( request, PRIV_FIELD );

        Group group = new Group();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleGroupName( groupName, group );
        handleGroupDescription( groupDescription, group );
        handlePrivs( privileges, group );

        try {
            // if no error, the group is modified and saved in database, else an
            // error message is displayed
            if ( errors.isEmpty() ) {
                groupDao.modify( group );
                result = "Group modification succeed";
            } else {
                result = "Group modification failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "Group modification failed : Unexpected mistake, please retry later.";
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

    // Group Name check : more than 2 characters
    private void groupNameValidation( String name ) throws FormValidationException {
        if ( name != null ) {
            if ( name.length() < 2 ) {
                throw new FormValidationException( "Name must have at least 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    // Group Description check : more than 5 characters
    private void groupDescriptionValidation( String description ) throws FormValidationException {
        if ( description != null ) {
            if ( description.length() < 5 ) {
                throw new FormValidationException( "Description must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a description." );
        }
    }

    // Group Privileges check : one at least
    private void privValidation( ArrayList<String> privileges ) throws FormValidationException {
        if ( privileges == null )
        {
            throw new FormValidationException( "Please choose at least one privilege." );
        }
    }

    // Add a message corresponding to the specific field to the error map.
    private void setError( String path, String message ) {
        errors.put( path, message );
    }

}