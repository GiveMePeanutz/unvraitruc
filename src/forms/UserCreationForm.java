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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import beans.User;
import dao.DAOException;
import dao.UserDao;
import eu.medsea.mimeutil.MimeUtil;

public final class UserCreationForm {

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
    private static final String GROUP_FIELD     = "groups";
    private static final String PHOTO_FIELD     = "photoURL";

    private static final int    BUFFER_LENGTH   = 10240;                        // 10ko

    private String              result;
    private Map<String, String> errors          = new HashMap<String, String>();
    private UserDao             userDao;

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
        String username = getFieldValue( request, USERNAME_FIELD );
        String password = getFieldValue( request, PASSWORD_FIELD );
        String lastName = getFieldValue( request, NAME_FIELD );
        String firstName = getFieldValue( request, FIRSTNAME_FIELD );
        String sex = getFieldValue( request, SEX_FIELD );
        String address = getFieldValue( request, ADDRESS_FIELD );
        String phone = getFieldValue( request, PHONE_FIELD );
        String email = getFieldValue( request, EMAIL_FIELD );
        DateTime birthDate = getDateValue( request, BIRTH_FIELD );
        String promotion = getFieldValue( request, PROMOTION_FIELD );
        ArrayList<String> groups = getSelectedValues( request, GROUP_FIELD );

        User user = new User();
        handleUsername( username, user );
        handlePassword( password, user );
        handleLastName( lastName, user );
        handleFirstName( firstName, user );
        handleSex( sex, user );
        handleAddress( address, user );
        handlePhone( phone, user );
        handleEmail( email, user );
        handleBirthDate( birthDate, user );
        handlePromotion( promotion, user );
        handlePhoto( user, request, path );
        handleGroups( groups, user );

        DateTime today = new DateTime();
        user.setRegDate( today );

        try {
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

    private void handleGroups( ArrayList<String> groups, User user ) {
        try {
            groupValidation( groups );
        } catch ( FormValidationException e ) {
            setError( GROUP_FIELD, e.getMessage() );
        }
        user.setGroupNames( groups );

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
        if ( sex == "1" )
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

    private void handlePromotion( String promotion, User user ) {
        try {
            promotionValidation( promotion );
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

    private void groupValidation( ArrayList<String> groups ) throws FormValidationException {
        if ( groups == null )
        {
            throw new FormValidationException( "Please choose a group." );
        }
    }

    private void birthDateValidation( DateTime birthDate ) throws FormValidationException {
        if ( birthDate == null )
        {
            throw new FormValidationException( "Please enter a birth date." );
        }
    }

    private void usernameValidation( String username ) throws FormValidationException {
        if ( username != null ) {
            if ( username.length() < 2 ) {
                throw new FormValidationException( "Username must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a username." );
        }
    }

    private void passwordValidation( String password ) throws FormValidationException {
        if ( password != null ) {
            if ( password.length() < 5 ) {
                throw new FormValidationException( "Password must have at least 5 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a password." );
        }
    }

    private void lastNameValidation( String lastName ) throws FormValidationException {
        if ( lastName != null ) {
            if ( lastName.length() < 2 ) {
                throw new FormValidationException( "Last name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a name." );
        }
    }

    private void firstNameValidation( String firstName ) throws FormValidationException {
        if ( firstName != null ) {
            if ( firstName.length() < 2 ) {
                throw new FormValidationException( "First name must have at least 3 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter a firstname." );
        }

    }

    private void sexValidation( String sex ) throws FormValidationException {
        if ( sex == null ) {
            throw new FormValidationException( "Please check the right user sex." );
        }
    }

    private void addressValidation( String address ) throws FormValidationException {
        if ( address != null ) {
            if ( address.length() < 10 ) {
                throw new FormValidationException( "Address must have at least 10 characters" );
            }
        } else {
            throw new FormValidationException( "Please enter the address" );
        }
    }

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

    private void emailValidation( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {

                throw new FormValidationException( "Plese enter a right email address." );
            }
        } else {
            throw new FormValidationException( "Please enter a mail address." );
        }
    }

    private void promotionValidation( String promotion ) throws FormValidationException {
        if ( promotion != null ) {
            if ( !promotion.matches( "([^0-9]+)([0-9]+)" ) ) {
                throw new FormValidationException( "Promotion must be a name or an acronym followed by a number." );
            }
        } else {
            throw new FormValidationException( "Please enter a promotion." );
        }
    }

    private String photoValidation( HttpServletRequest request, String path ) throws FormValidationException {
        /*
         * Récupération du contenu du champ image du formulaire. Il faut ici
         * utiliser la méthode getPart().
         */
        String fileName = null;
        InputStream fileContent = null;
        try {
            Part part = request.getPart( PHOTO_FIELD );
            fileName = getFileName( part );

            /*
             * Si la méthode getNomFichier() a renvoyé quelque chose, il s'agit
             * donc d'un champ de type fichier (input type="file").
             */
            if ( fileName != null && !fileName.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une raison
                 * mystique le path du fichier local à la machine du user...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne sélectionner que le nom et
                 * l'extension du fichier, et de se débarrasser du superflu.
                 */
                fileName = fileName.substring( fileName.lastIndexOf( '/' ) + 1 )
                        .substring( fileName.lastIndexOf( '\\' ) + 1 );

                /* Récupération du contenu du fichier */
                fileContent = part.getInputStream();

                /* Extraction du type MIME du fichier depuis l'InputStream */
                MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
                Collection<?> mimeTypes = MimeUtil.getMimeTypes( fileContent );

                /*
                 * Si le fichier est bien une image, alors son en-tête MIME
                 * commence par la chaîne "image"
                 */
                if ( mimeTypes.toString().startsWith( "image" ) ) {
                    /* Écriture du fichier sur le disque */
                    fileWriting( fileContent, fileName, path );
                } else {
                    throw new FormValidationException( "Sent File must be a photo" );
                }
            }
        } catch ( IllegalStateException e ) {
            /*
             * Exception retournée si la taille des données dépasse les limites
             * définies dans la section <multipart-config> de la déclaration de
             * notre servlet d'upload dans le fichier web.xml
             */
            e.printStackTrace();
            throw new FormValidationException( "Sent file can't be heavier than 10 Mo" );
        } catch ( IOException e ) {
            /*
             * Exception retournée si une error au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             */
            e.printStackTrace();
            throw new FormValidationException( "Server configuration error" );
        } catch ( ServletException e ) {
            /*
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data.
             */
            e.printStackTrace();
            throw new FormValidationException(
                    "Impossible, please use this form to upload your photo." );
        }

        return fileName;
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
    private static DateTime getDateValue( HttpServletRequest request, String fieldName ) throws FormValidationException {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } /*
           * else if ( !value.matches(
           * "^(0?[1-9]|[12][0-9]|3[01])[-/]?(0?[1-9]|1[012])[-/]?(19[\\d]{2}|20[\\d]{2}|2100)$"
           * ) ) { throw new FormValidationException( "Incorrect date format."
           * ); }
           */
        else
        {
            DateTimeFormatter formatter = DateTimeFormat.forPattern( "MM/dd/yyyy" );
            DateTime dt = formatter.parseDateTime( value );
            return dt;
        }
    }

    private static String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

    private ArrayList<String> getSelectedValues( HttpServletRequest request, String fieldName ) {

        ArrayList<String> groups = new ArrayList<String>();

        String[] values = request.getParameterValues( fieldName );
        if ( values == null || values.length == 0 )
        {
            return null;
        }

        for ( int i = 0; i < values.length; i++ )
        {
            groups.add( values[i] );
        }
        return groups;
    }

    /*
     * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
     * "content-disposition", et de vérifier si le paramètre "filename" y est
     * présent. Si oui, alors le champ traité est de type File et la méthode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la méthode retourne null.
     */
    private static String getFileName( Part part ) {
        /* Boucle sur chacun des paramètres de l'en-tête "content-disposition". */
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            /* Recherche de l'éventuelle présence du paramètre "filename". */
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                /*
                 * Si "filename" est présent, alors renvoi de sa valeur,
                 * c'est-à-dire du nom de fichier sans guillemets.
                 */
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }

    /*
     * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     */
    private void fileWriting( InputStream fileContent, String fileName, String path )
            throws FormValidationException {
        /* Prépare les flux. */
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            /* Ouvre les flux. */
            in = new BufferedInputStream( fileContent, BUFFER_LENGTH );
            out = new BufferedOutputStream( new FileOutputStream( new File( path + fileName ) ),
                    BUFFER_LENGTH );

            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
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

    private static String getParameterValue( HttpServletRequest request,
            String nomChamp ) {
        String value = request.getParameter( nomChamp );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }
}