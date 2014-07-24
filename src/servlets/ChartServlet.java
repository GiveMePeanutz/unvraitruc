package servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import utilities.DrawChart;

@WebServlet( urlPatterns = "/chart" )
public class ChartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException {

        response.setContentType( "image/png" );

        OutputStream outputStream = response.getOutputStream();
        DrawChart drawChart = new DrawChart();
        JFreeChart chart = drawChart.getChart();
        int width = 500;
        int height = 350;
        ChartUtilities.writeChartAsPNG( outputStream, chart, width, height );

    }

}
