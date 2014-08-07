package servlets;

//Controller of data warehouse calculation  

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.UtilitiesForm;
import beans.DataWarehouseLine;
import beans.User;
import dao.DAOFactory;
import dao.ExtractDataWarehouseDao;
import dao.FactTableDao;

@WebServlet( "/dataWarehouse" )
public class DataWarehouse extends HttpServlet {

    public static final String      CONF_DAO_FACTORY      = "daofactory";
    public static final String      LIST_RES_ATT          = "results";
    public static final String      USER_SESSION_ATT      = "userSession";
    public static final String      VERIFY_PARAM          = "Calculate";

    public static final String      LIST_SEX_ATT          = "sexValues";
    public static final String      LIST_GROUP_ATT        = "groups";
    public static final String      LIST_YEAR_ATT         = "years";
    public static final String      LIST_MONTH_ATT        = "months";
    public static final String      LIST_WEEK_ATT         = "weeks";
    public static final String      LIST_DAY_ATT          = "days";
    public static final String      LIST_DAYOFWEEK_ATT    = "daysOfWeek";
    public static final String      LIST_HOUR_ATT         = "hours";
    public static final String      LIST_ACTIVITY_ATT     = "activities";

    public static final String      SEX_FIELD             = "sexValue";
    public static final String      GROUP_FIELD           = "group";
    public static final String      YEAR_FIELD            = "year";
    public static final String      MONTH_FIELD           = "month";
    public static final String      WEEK_FIELD            = "week";
    public static final String      DAY_FIELD             = "day";
    public static final String      DAYOFWEEK_FIELD       = "dayOfWeek";
    public static final String      HOUR_FIELD            = "hour";
    public static final String      ACTIVITY_FIELD        = "activity";

    public static final String      SESSION_RESULTS_MONTH = "resultsMonth";
    public static final String      SESSION_RESULTS_WEEK  = "resultsWeek";

    public static final String      COUNT_ATT             = "dWLine";
    public static final String      FORM_ATT              = "form";

    public static final String      VIEW                  = "/WEB-INF/dataWarehouse.jsp";

    private ExtractDataWarehouseDao extractDataWarehouseDao;
    private FactTableDao            factTableDao;
    private UtilitiesForm           util                  = new UtilitiesForm();

    public void init() throws ServletException {

        this.extractDataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getExtractDataWarehouseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Retrieving of all groups, years, months and days of week values
        // available.
        List<String> groups = extractDataWarehouseDao.listGroup();
        List<String> years = extractDataWarehouseDao.listYear();
        List<String> months = extractDataWarehouseDao.getMonths();
        List<String> daysOfWeek = extractDataWarehouseDao.getDays();

        // Saving those parameters in request attributes
        request.setAttribute( LIST_GROUP_ATT, groups );
        request.setAttribute( LIST_YEAR_ATT, years );
        request.setAttribute( LIST_MONTH_ATT, months );
        request.setAttribute( LIST_DAYOFWEEK_ATT, daysOfWeek );

        // Data Warehouse Page is displayed, and these parameters are displayed
        // in the drop down lists.
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {

        // Load values again
        List<String> groups = extractDataWarehouseDao.listGroup();
        List<String> years = extractDataWarehouseDao.listYear();
        List<String> months = extractDataWarehouseDao.getMonths();
        List<String> daysOfWeek = extractDataWarehouseDao.getDays();

        request.setAttribute( LIST_GROUP_ATT, groups );
        request.setAttribute( LIST_YEAR_ATT, years );
        request.setAttribute( LIST_MONTH_ATT, months );
        request.setAttribute( LIST_DAYOFWEEK_ATT, daysOfWeek );
        // -----------------------------------------------------------------------------------
        DataWarehouseLine dWLine;
        int countResult = 0;

        // Retrieving parameters the user chose.
        int sex = util.getIntValue( request, SEX_FIELD );
        String group = util.getFieldValue( request, GROUP_FIELD );
        int year = util.getIntValue( request, YEAR_FIELD );
        int activity = util.getIntValue( request, ACTIVITY_FIELD );

        // This string will indicate if the user chose to calculate with the
        // first or the second time hierarchy
        String calculate = request.getParameter( VERIFY_PARAM );

        if ( calculate.equals( "Display by month" ) )
        {
            // For the first hierarchy, we have to retrieve month, day and hour
            // parameters too
            String month = util.getFieldValue( request, MONTH_FIELD );
            int day = util.getIntValue( request, DAY_FIELD );
            int hour = util.getIntValue( request, HOUR_FIELD );

            // Creates a new line (object which will be saved in session)
            dWLine = new DataWarehouseLine( sex, group, year, month, day, hour, activity );

            // And return the number of activity according to all the parameters
            // of the data warehouse line.
            if ( !extractDataWarehouseDao.countMonth( dWLine ).equals( "" ) )
                countResult = Integer.parseInt( extractDataWarehouseDao.countMonth( dWLine ) );
        }
        else
        {
            // For the second hierarchy, we have to retrieve week and day of
            // week parameters
            int week = util.getIntValue( request, WEEK_FIELD );
            String dayOfWeek = util.getFieldValue( request, DAYOFWEEK_FIELD );

            // Creates a new line (object which will be saved in session)
            dWLine = new DataWarehouseLine( sex, group, year, week, dayOfWeek, activity );

            // And return the number of activity according to all the parameters
            // of the data warehouse line.
            if ( !extractDataWarehouseDao.countWeek( dWLine ).equals( "" ) )
                countResult = Integer.parseInt( extractDataWarehouseDao.countWeek( dWLine ) );
        }

        // Adding the result to the data warehouse line object
        dWLine.setCount( countResult );

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged onn this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        // New action saved in database
        factTableDao.addFact( userSession.getUsername(), "Count something" );

        // For the first hierarchy, we add the data warehouse line to the first
        // table, so in the first ArrayList : resultsMonth, which we have to
        // create if doesn't exists
        if ( calculate.equals( "Calculate" ) )
        {
            ArrayList<DataWarehouseLine> resultsMonth = (ArrayList<DataWarehouseLine>) session
                    .getAttribute( SESSION_RESULTS_MONTH );

            if ( resultsMonth == null ) {
                resultsMonth = new ArrayList<DataWarehouseLine>();
            }
            resultsMonth.add( dWLine );

            session.setAttribute( SESSION_RESULTS_MONTH, resultsMonth );
        }

        // For the second hierarchy, we add the data warehouse line to the
        // second table, so in the second ArrayList : resultsWeek, which we have
        // to create if doesn't exists
        else
        {
            ArrayList<DataWarehouseLine> resultsWeek = (ArrayList<DataWarehouseLine>) session
                    .getAttribute( SESSION_RESULTS_WEEK );

            if ( resultsWeek == null ) {
                resultsWeek = new ArrayList<DataWarehouseLine>();
            }
            resultsWeek.add( dWLine );

            session.setAttribute( SESSION_RESULTS_WEEK, resultsWeek );
        }

        // Load again the Data Warehouse page
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );

    }
}
