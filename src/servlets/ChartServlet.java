package servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import utilities.DrawChart;
import beans.User;
import dao.DAOFactory;
import dao.ExtractDataWarehouseDao;
import dao.FactTableDao;

@WebServlet( urlPatterns = "/chart" )
public class ChartServlet extends HttpServlet {

    public static final String      CONF_DAO_FACTORY = "daofactory";
    public static final String      USER_SESSION_ATT = "userSession";

    public static final String      TYPE_FIELD       = "type";
    public static final String      ACTION_FIELD     = "action";
    public static final String      YEAR_FIELD       = "year";

    private ExtractDataWarehouseDao extractDataWarehouseDao;
    private FactTableDao            factTableDao;

    public void init() throws ServletException {
        this.extractDataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getExtractDataWarehouseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getFactTableDao();
    }

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException {

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException {

        response.setContentType( "image/png" );
        String type = getFieldValue( request, TYPE_FIELD );
        int action = getIntValue( request, ACTION_FIELD );
        int year = getIntValue( request, YEAR_FIELD );

        OutputStream outputStream = response.getOutputStream();
        DrawChart drawChart = new DrawChart();
        JFreeChart chart = drawChart.getChart( type, action, year, extractDataWarehouseDao );
        int width = 500;
        int height = 350;
        ChartUtilities.writeChartAsPNG( outputStream, chart, width, height );

        // Save the action in database
        HttpSession session = request.getSession();
        User userSession = new User();
        userSession = (User) session.getAttribute( USER_SESSION_ATT );
        factTableDao.addFact( userSession.getUsername(), "Drawing" );
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
