package forms;

//customised exception for form errors
public class FormValidationException extends Exception {

    // Constructor
    public FormValidationException( String message ) {
        super( message );
    }
}