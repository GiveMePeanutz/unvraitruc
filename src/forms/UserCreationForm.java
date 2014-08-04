package forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.joda.time.DateTime;

import utilities.utilitiesForm;
import beans.User;
import dao.DAOException;
import dao.UserDao;
import eu.medsea.mimeutil.MimeUtil;

public final class UserCreationForm {

    // Fields Name, have to be identical than fields name in JSPs
    private static final String USERNAME_FIELD  = "username";
    private static final String PASSWORD_FIELD  = "password";
    private static final String NAME_FIELD      = "lastName";
    private static final String FIRSTNAME_FIELD = "firstName";
    private static final String SEX_FIELD       = "sex";
    private static final String ADDRESS_FIELD   = "address";
    private static final String PHONE_FIELD     = "phone";
    private static final String EMAIL_FIELD     = "email";
    private static final String BIRTH_FIELD     = "birthDate";
    private static final String PROMOTION_FIELD = "promotion";
    private static final String PHOTO_FIELD     = "photoURL";
    private static final String GROUP_FIELD     = "groups";

    private static final int    BUFFER_LENGTH   = 10240;                        // 10ko

    private String              result;
    private Map<String, String> errors          = new HashMap<String, String>();
    private UserDao             userDao;
    private utilitiesForm       util            = new utilitiesForm();

    public UserCreationForm( UserDao userDao ) {
        this.userDao = userDao;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getResult() {
        return result;
    }

    public User createUser( HttpServletRequest request, String path ) throws ParseException, FormValidationException {

        // Parameters Recovery
        String username = util.getFieldValue( request, USERNAME_FIELD );
        String password = util.getFieldValue( request, PASSWORD_FIELD );
        String lastName = util.getFieldValue( request, NAME_FIELD );
        String firstName = util.getFieldValue( request, FIRSTNAME_FIELD );
        String sex = util.getFieldValue( request, SEX_FIELD );
        String address = util.getFieldValue( request, ADDRESS_FIELD );
        String phone = util.getFieldValue( request, PHONE_FIELD );
        String email = util.getFieldValue( request, EMAIL_FIELD );
        DateTime birthDate = util.getDateValue( request, BIRTH_FIELD );
        ArrayList<String> groups = util.getSelectedValues( request, GROUP_FIELD );
        String promotion = util.getFieldValue( request, PROMOTION_FIELD );

        User user = new User();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleUsername( username, user );
        handlePassword( password, user );
        handleLastName( lastName, user );
        handleFirstName( firstName, user );
        handleSex( sex, user );
        handleAddress( address, user );
        handlePhone( phone, user );
        handleEmail( email, user );
        handleBirthDate( birthDate, user );
        handleGroups( groups, user );
        handlePhoto( user, request, path );
        handlePromotion( promotion, user );

        DateTime today = new DateTime();
        // Registration date is initialized to today date
        user.setRegDate( today );

        try {
            // if no error, the user is created and saved in database, else an
            // error message is displayed
            if ( errors.isEmpty() ) {
                userDao.create( user );
                result = "User creation succeed";
            } else {
                result = "User creation failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "User creation failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return user;
    }

    public User modifyUser( HttpServletRequest request, String path ) throws ParseException, FormValidationException {

        // Parameters Recovery
        String username = util.getFieldValue( request, USERNAME_FIELD );
        String password = util.getFieldValue( request, PASSWORD_FIELD );
        String lastName = util.getFieldValue( request, NAME_FIELD );
        String firstName = util.getFieldValue( request, FIRSTNAME_FIELD );
        String sex = util.getFieldValue( request, SEX_FIELD );
        String address = util.getFieldValue( request, ADDRESS_FIELD );
        String phone = util.getFieldValue( request, PHONE_FIELD );
        String email = util.getFieldValue( request, EMAIL_FIELD );
        DateTime birthDate = util.getDateValue( request, BIRTH_FIELD );
        ArrayList<String> groups = util.getSelectedValues( request, GROUP_FIELD );
        String promotion = util.getFieldValue( request, PROMOTION_FIELD );

        User user = new User();

        // Parameters checks : call the "validation" methods, add errors if
        // there are or set the parameter.
        handleUsername( username, user );
        handlePassword( password, user );
        handleLastName( lastName, user );
        handleFirstName( firstName, user );
        handleSex( sex, user );
        handleAddress( address, user );
        handlePhone( phone, user );
        handleEmail( email, user );
        handleBirthDate( birthDate, user );
        handlePhoto( user, request, path );
        handleGroups( groups, user );
        handlePromotion( promotion, user );

        // Registration date is initialized to today date
        DateTime today = new DateTime();
        user.setRegDate( today );

        try {

            // if no error, the user is modified and saved in database, else
            // an error message is displayed
            if ( errors.isEmpty() ) {
                userDao.modify( user );
                result = "User modification succeed";
            } else {
                result = "User modification failed !.";
            }
        } catch ( DAOException e ) {
            setError( "unexpected", "Unexpected mistake, please retry later. " );
            result = "User modification failed : Unexpected mistake, please retry later.";
            e.printStackTrace();
        }

        return user;
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
            setError( PASSWORD_FIELD, e.getMessage() );
        }
        user.setPassword( password );
    }

    private void handleLastName( String lastName, User user ) {
        try {
            lastNameValidation( lastName );
        } catch ( FormValidationException e ) {
            setError( NAME_FIELD, e.getMessage() );
        }
        user.setLastName( lastName );
    }

    private void handleFirstName( String firstName, User user ) {
        try {
            firstNameValidation( firstName );
        } catch ( FormValidationException e ) {
            setError( FIRSTNAME_FIELD, e.getMessage() );
        }
        user.setFirstName( firstName );
    }

    private void handleSex( String sex, User user ) {
        try {
            sexValidation( sex );
        } catch ( FormValidationException e ) {
            setError( SEX_FIELD, e.getMessage() );
        }
        if ( sex.equals( "1" ) )
        {
            user.setSex( 1 );
        }
        else
        {
            user.setSex( 0 );
        }
    }

    private void handleAddress( String address, User user ) {
        try {
            addressValidation( address );
        } catch ( FormValidationException e ) {
            setError( ADDRESS_FIELD, e.getMessage() );
        }
        user.setAddress( address );
    }

    private void handlePhone( String phone, User user ) {
        try {
            phoneValidation( phone );
        } catch ( FormValidationException e ) {
            setError( PHONE_FIELD, e.getMessage() );
        }
        user.setPhone( phone );
    }

    private void handleEmail( String email, User user ) {
        try {
            emailValidation( email );
        } catch ( FormValidationException e ) {
            setError( EMAIL_FIELD, e.getMessage() );
        }
        user.setEmail( email );
    }

    private void handleBirthDate( DateTime birthDate, User user ) {

        try {
            birthDateValidation( birthDate );
        } catch ( FormValidationException e ) {
            setError( BIRTH_FIELD, e.getMessage() );
        }
        user.setBirthDate( birthDate );
    }

    private void handleGroups( ArrayList<String> groups, User user ) {
        try {
            groupValidation( groups );
        } catch ( FormValidationException e ) {
            setError( GROUP_FIELD, e.getMessage() );
        }
        user.setGroupNames( groups );

    }

    private void handlePromotion( String promotion, User user ) {
        try {
            promotionValidation( promotion, user );
        } catch ( FormValidationException e ) {
            setError( PROMOTION_FIELD, e.getMessage() );
        }
        user.setPromotion( promotion );
    }

    private void handlePhoto( User user, HttpServletRequest request, String path ) {
        String photoURL = null;
        try {
            photoURL = photoValidation( request, path );
        } catch ( FormValidationException e ) {
            setError( PHOTO_FIELD, e.getMessage() );
        }
        user.setPhotoURL( photoURL );
    }

    // BirthDate check :can't be null
    private void birthDateValidation( DateTime birthDate ) throws FormValidationException {
        if ( birthDate == null )
        {
            throw new FormValidationException( "Please enter a birth date." );
        }
    }

    // Username check : more than 2 characters
    private void usernameValidation( String username ) throws FormValidationException {
        if ( username != null ) {
            if ( username.length() < 2 ) {
                throw new FormValidationException( "Username must have at least 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a username." );
        }
    }

    // Password check : more than 5 characters
    private void passwordValidation( String password ) throws FormValidationException {
        if ( password != null ) {
            if ( password.length() < 5 ) {
                throw new FormValidationException( "Password must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a password." );
        }
    }

    // Last Name check : more than 2 characters
    private void lastNameValidation( String lastName ) throws FormValidationException {
        if ( lastName != null ) {
            if ( lastName.length() < 2 ) {
                throw new FormValidationException( "Last name must have at least 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    // First Name check : more than 2 characters
    private void firstNameValidation( String firstName ) throws FormValidationException {
        if ( firstName != null ) {
            if ( firstName.length() < 2 ) {
                throw new FormValidationException( "First name must have at least 2 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a firstname." );
        }

    }

    // Username check : can't be empty
    private void sexValidation( String sex ) throws FormValidationException {
        if ( sex.isEmpty() ) {
            throw new FormValidationException( "Please check the right user sex." );
        }
    }

    // Address check : more than 10 characters
    private void addressValidation( String address ) throws FormValidationException {
        if ( address != null ) {
            if ( address.length() < 10 ) {
                throw new FormValidationException( "Address must have at least 10 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter the address" );
        }
    }

    // Phone number check : more than 4 characters and just numbers
    private void phoneValidation( String phone ) throws FormValidationException {
        if ( phone != null ) {
            if ( !phone.matches( "^\\d+$" ) ) {
                throw new FormValidationException( "Phone number must have numbers only" );
            } else if ( phone.length() < 4 ) {
                throw new FormValidationException( "Phone number must have at least 4 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a phone number." );
        }
    }

    // Email check : something@something.something
    private void emailValidation( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {

                throw new FormValidationException( "Plese enter a right email address." );
            }
        } else {
            throw new FormValidationException( "Please enter a mail address." );
        }
    }

    // User groups check :one at least
    private void groupValidation( ArrayList<String> groups ) throws FormValidationException {
        if ( groups == null )
        {
            throw new FormValidationException( "Please choose at least one group." );
        }
    }

    // User promotion check : has to be entered only if the user is a student.
    // Has to be an acronym followed by numbers like STE4
    private void promotionValidation( String promotion, User user ) throws FormValidationException {
        Boolean bool = false;
        if ( user.getGroupNames() != null )
        {
            List<String> listGroup = user.getGroupNames();

            for ( String group : listGroup )
            {
                if ( group.equals( "Student" ) )
                {
                    bool = true;
                }
            }
        }
        if ( bool == true )
        {
            if ( promotion != null ) {
                if ( !promotion.matches( "([^0-9]+)([0-9]+)" ) ) {
                    throw new FormValidationException( "Promotion must be a name or an acronym followed by a number." );
                }
            } else {
                throw new FormValidationException( "Please enter a promotion." );
            }
        }
        else
        {
            if ( promotion != null )
            {
                throw new FormValidationException( "Please do not enter a promotion for a user of this group." );
            }
        }
    }

    private String photoValidation( HttpServletRequest request, String path ) throws FormValidationException {
        /*
         * Retrieve the picture field.
         */
        String fileName = null;
        InputStream fileContent = null;
        try {
            Part part = request.getPart( PHOTO_FIELD );
            fileName = getFileName( part );

            /*
             * if getNomFichier() return something, input type="file".
             */
            if ( fileName != null && !fileName.isEmpty() ) {

                // The following code avoid Internet Explorer bug with file path
                fileName = fileName.substring( fileName.lastIndexOf( '/' ) + 1 )
                        .substring( fileName.lastIndexOf( '\\' ) + 1 );

                // Retrieving what is IN the file
                fileContent = part.getInputStream();

                // Using MIME librairy to know if it's a picture
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( fileContent );

                // If the file is a picture, so its MIME-headingstarts with
                // "image"
                if ( mimeTypes.toString().startsWith( "image" ) ) {
                    // Writing File on disk
                    fileWriting( fileContent, fileName, path );
                } else {
                    throw new FormValidationException( "Sent File must be a photo" );
                }
            }
        } catch ( IllegalStateException e ) {
            // Exception if datasize exceeds size defined in <multipart-config>
            // heading in UserCreation Servlet
            e.printStackTrace();
            throw new FormValidationException( "Sent file can't be heavier than 10 Mo" );
        } catch ( IOException e ) {
            // Exception if there is a repertory error ( not existing repertory,
            // not enough access rights etc.)
            e.printStackTrace();
            throw new FormValidationException( "Server configuration error" );
        } catch ( ServletException e ) {

            // Exception if request is not multipart/form-data.
            e.printStackTrace();
            throw new FormValidationException(
                    "Impossible, please use this form to upload your photo." );
        }

        return fileName;
    }

    // Add a message corresponding to the specific field to the error map.
    private void setError( String path, String message ) {
        errors.put( path, message );
    }

    private static String getFileName( Part part ) {
        // Loop on each "content-disposition" parameter.
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            // Searching a "filename" parameter if existing.
            if ( contentDisposition.trim().startsWith( "filename" ) ) {

                // if "filename" exists, the system returns its value, ie the
                // file name without inverted comas.

                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        // returns null if finds nothing
        return null;
    }

    // Writing parameter file on disk where we want with the name we want.
    private void fileWriting( InputStream fileContent, String fileName, String path )
            throws FormValidationException
    {
        // Prepares streams
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            // Opens streams
            in = new BufferedInputStream( fileContent, BUFFER_LENGTH );
            out = new BufferedOutputStream( new FileOutputStream( new File( path + fileName ) ),
                    BUFFER_LENGTH );

            // Reading received file et writing its content in a file on disk
            byte[] buffer = new byte[BUFFER_LENGTH];
            int length = 0;
            while ( ( length = in.read( buffer ) ) > 0 ) {
                out.write( buffer, 0, length );
            }
        } catch ( Exception e ) {
            throw new FormValidationException( "File-writed error (on disk) " );
        } finally {
            try {
                out.close();
            } catch ( IOException ignore ) {
            }
            try {
                out.close();
            } catch ( IOException ignore ) {
            }
        }
    }

}