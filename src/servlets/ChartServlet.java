package servlets;

//Controller of charts drawing 

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

import utilities.UtilitiesForm;
import beans.User;
import chart.DrawBarChart;
import chart.DrawPieChart;
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
    public static final String      CHARTTYPE_PARAM  = "Draw";

    private ExtractDataWarehouseDao extractDataWarehouseDao;
    private FactTableDao            factTableDao;
    private UtilitiesForm           util             = new UtilitiesForm();

    public void init() throws ServletException {
        this.extractDataWarehouseDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getExtractDataWarehouseDao();
        this.factTableDao = ( (DAOFactory) getServletContext().getAttribute( CONF_DAO_FACTORY ) )
                .getFactTableDao();
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException {

        /* Session retrieving from the request */
        HttpSession session = request.getSession();
        User userSession = new User();
        // userSession = user logged on this session
        userSession = (User) session.getAttribute( USER_SESSION_ATT );

        response.setContentType( "image/png" );

        // Chosen type parameter retrieving from the form.
        String type = util.getFieldValue( request, TYPE_FIELD );
        // Chosen action retrieving from the form.
        int action = util.getIntValue( request, ACTION_FIELD );
        // Chosen year retrieving from the form.
        int year = util.getIntValue( request, YEAR_FIELD );

        OutputStream outputStream = response.getOutputStream();
        // Dimension definition
        int width = 500;
        int height = 350;

        String chartType = request.getParameter( CHARTTYPE_PARAM );

        if ( chartType.equals( "Draw a PieChart" ) )
        {
            // Creation of a DrawPieChart Instance (from utilities package)
            DrawPieChart drawPieChart = new DrawPieChart();
            // Creation of the Piechart with the retrieved parameters
            JFreeChart chart = drawPieChart.getChart( type, action, year, extractDataWarehouseDao );

            // Displays the pieChart
            ChartUtilities.writeChartAsPNG( outputStream, chart, width, height );

            // New action saved in database
            factTableDao.addFact( userSession.getUsername(), "Drawing a PieChart" );
        }

        else if ( chartType.equals( "Draw a BarChart" ) )
        {
            // Creation of a DrawBarChart Instance (from utilities package)
            DrawBarChart drawBarChart = new DrawBarChart();
            // Creation of the Piechart with the retrieved parameters
            JFreeChart chart = drawBarChart.getChart( type, action, year, extractDataWarehouseDao );

            // Displays the pieChart
            ChartUtilities.writeChartAsPNG( outputStream, chart, width, height );

            // New action saved in database
            factTableDao.addFact( userSession.getUsername(), "Drawing a BarChart" );

        }

    }
}
