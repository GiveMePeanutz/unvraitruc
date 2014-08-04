package utilities;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import forms.FormValidationException;

public class utilitiesForm {

    // Retrieves the value written in a textfield
    public String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

    // Retrieves the value written in a numberfield
    public int getIntValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        int valueInt = Integer.parseInt( value );
        if ( valueInt == 0 ) {
            return 0;
        } else {
            return valueInt;
        }
    }

    // Retrieves the values chosen in a list.
    public ArrayList<String> getSelectedValues( HttpServletRequest request, String fieldName ) {

        ArrayList<String> valuesList = new ArrayList<String>();
        String[] values = request.getParameterValues( fieldName );
        if ( values == null || values.length == 0 )
        {
            return null;
        }
        for ( int i = 0; i < values.length; i++ )
        {
            valuesList.add( values[i] );
        }
        return valuesList;
    }

    // Retrieves the int values chosen in a list(for menus).
    public ArrayList<Integer> getIntSelectedValues( HttpServletRequest request, String fieldName ) {

        ArrayList<Integer> valuesList = new ArrayList<Integer>();
        String[] values = request.getParameterValues( fieldName );
        if ( values == null || values.length == 0 )
        {
            return null;
        }

        for ( int i = 0; i < values.length; i++ )
        {
            valuesList.add( Integer.parseInt( values[i] ) );
        }
        return valuesList;
    }

    // Retrieves the date written in a date field.
    public DateTime getDateValue( HttpServletRequest request, String fieldName ) throws FormValidationException {
        String value = request.getParameter( fieldName );

        if ( value == null || value.trim().length() == 0 ) {
            return null;
        }

        else
        {
            DateTimeFormatter formatter = DateTimeFormat.forPattern( "MM/dd/yyyy" );
            DateTime dt = formatter.parseDateTime( value );
            return dt;
        }
    }

}
