package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DataWarehouseLine;
import beans.User;
import dao.DAOFactory;
import dao.DataWarehouseDao;
import dao.FactTableDao;

@WebServlet( "/dataWarehouse" )
public class DataWarehouse extends HttpServlet {

    public static final String CONF_DAO_FACTORY   = "daofactory";
    public static final String LIST_RES_ATT       = "results";
    public static final String USER_SESSION_ATT   = "userSession";

    public static final String LIST_SEX_ATT       = "sexValues";
    public static final String LIST_GROUP_ATT     = "groups";
    public static final String LIST_YEAR_ATT      = "years";
    public static final String LIST_MONTH_ATT     = "months";
    public static final String LIST_WEEK_ATT      = "weeks";
    public static final String LIST_DAY_ATT       = "days";
    public static final String LIST_DAYOFWEEK_ATT = "daysOfWeek";
    public static final String LIST_HOUR_ATT      = "hours";
    public static final String LIST_ACTIVITY_ATT  = "activities";

    public static final String SEX_FIELD          = "sexValue";
    public static final String GROUP_FIELD        = "group";
    public static final String YEAR_FIELD         = "year";
    public static final String MONTH_FIELD        = "month";
    public static final String WEEK_FIELD         = "week";
    public static final String DAY_FIELD          = "day";
    public static final String DAYOFWEEK_FIELD    = "dayOfWeek";
    public static final String HOUR_FIELD         = "hour";
    public static final String ACTIVITY_FIELD     = "activitie";

    public static final String SESSION_RESULTS    = "results";
    public static final String COUNT_ATT          = "dWLine";
    public static final String FORM_ATT           = "form";

    public static final String VIEW               = "/WEB-INF/dataWarehouse.jsp";

    private DataWarehouseDao   dataWarehouseDao;
    private FactTableDao       factTableDao;

    public void init() throws ServletException {
        this.dataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getDataWarehouseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getFactTableDao();
    }

    public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	/*
        List<String> groups = dataWarehouseDao.listGroup();
        List<String> years = dataWarehouseDao.listYear();
        List<String> months = dataWarehouseDao.getMonths();
        List<String> weeks = dataWarehouseDao.listWeek();
        List<String> days = dataWarehouseDao.getDay();
        List<String> daysOfWeek = dataWarehouseDao.listDayOfWeek();
        List<String> hours = dataWarehouseDao.listHour();
		
        request.setAttribute( LIST_GROUP_ATT, groups );
        request.setAttribute( LIST_YEAR_ATT, years );
        request.setAttribute( LIST_MONTH_ATT, months );
        request.setAttribute( LIST_WEEK_ATT, weeks );
        request.setAttribute( LIST_DAY_ATT, days );
        request.setAttribute( LIST_DAYOFWEEK_ATT, daysOfWeek );
        request.setAttribute( LIST_HOUR_ATT, hours );
		*/
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
		
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    	/*
        int sex = getIntValue( request, SEX_FIELD );
        String group = getFieldValue( request, GROUP_FIELD );
        int year = getIntValue( request, YEAR_FIELD );
        String month = getFieldValue( request, MONTH_FIELD );
        int week = getIntValue( request, WEEK_FIELD );
        int day = getIntValue( request, DAY_FIELD );
        String dayOfWeek = getFieldValue( request, DAYOFWEEK_FIELD );
        int hour = getIntValue( request, HOUR_FIELD );
        int activity = getIntValue( request, ACTIVITY_FIELD );

        DataWarehouseLine dWLine = new DataWarehouseLine( sex, group, year, month, week, day, dayOfWeek, hour, activity );
        int countResult = dataWarehouseDao.count( sex, group, year, month, week, day, dayOfWeek, hour, activity );
        dWLine.setCount( countResult );

        // inutile peut etre
        request.setAttribute( COUNT_ATT, dWLine );

        HttpSession session = request.getSession();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername(), "Count something" );

        ArrayList<DataWarehouseLine> results = (ArrayList<DataWarehouseLine>) session
                .getAttribute( SESSION_RESULTS );

        if ( results == null ) {
            results = new ArrayList<DataWarehouseLine>();
        }
        results.add( dWLine );

        session.setAttribute( SESSION_RESULTS, results );
		*/
        this.getServletContext().getRequestDispatcher( VIEW ).forward( request, response );
		
    }

    private static String getFieldValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        if ( value == null || value.trim().length() == 0 ) {
            return null;
        } else {
            return value;
        }
    }

    private static int getIntValue( HttpServletRequest request, String fieldName ) {
        String value = request.getParameter( fieldName );
        int valueInt = Integer.parseInt( value );
        if ( valueInt == 0 ) {
            return 0;
        } else {
            return valueInt;
        }
    }
}
