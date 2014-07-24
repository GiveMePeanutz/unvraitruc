package utilities;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class DrawChart {

    public JFreeChart getChart() {

        DefaultPieDataset dataset = new DefaultPieDataset();
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;

        JFreeChart chart = ChartFactory.createPieChart( "Cars", dataset, legend, tooltips, urls );

        chart.setBorderPaint( Color.GREEN );
        chart.setBorderStroke( new BasicStroke( 5.0f ) );
        chart.setBorderVisible( true );

        return chart;
    }

}
