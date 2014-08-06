package chart;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import dao.ExtractDataWarehouseDao;

public class DrawBarChart {

    // Prepares a PieChart
    public JFreeChart getChart( String type, int action, int year, ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        boolean legend = true;
        boolean tooltips = true;
        boolean urls = false;

        // Creates the title according to what was chosen in fields.
        String title = "";
        if ( action == -1 )
            title = "Number of all activities";
        else if ( action == 0 )
            title = "Number of visited pages";
        else
            title = "Number of performed actions";

        title = title + "\n according to " + type + " Attribute for each month in " + year;

        // creates the dataset
        dataset = fillDataset( type, action, year, extractDataWarehouseDao );

        // creates the chart with the right dataset
        JFreeChart chart = createChart( title, dataset, legend, tooltips, urls );

        return chart;
    }

    // Fills the dataset with data from database.
    public DefaultCategoryDataset fillDataset( String type, int action, int year,
            ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Months retrieving
        List<String> months = extractDataWarehouseDao.getMonths();

        // Retrieves all existing groups
        List<String> groups = extractDataWarehouseDao.listGroup();

        int nbMale;
        int nbFemale;
        int nbByGroup;
        for ( String month : months ) // for each month
        {
            if ( !month.equals( "All" ) )
            {
                // If user chose Sex
                if ( type.equals( "Sex" ) )
                {

                    // counts number of action for each sex
                    // If no action nothing is written
                    if ( !extractDataWarehouseDao.countAllBySexByMonth( 0, action, year, month ).equals( "" ) )
                    {
                        nbMale = Integer.parseInt( extractDataWarehouseDao
                                .countAllBySexByMonth( 0, action, year, month ) );
                        dataset.addValue( nbMale, "Male", month );
                    }

                    if ( !extractDataWarehouseDao.countAllBySexByMonth( 1, action, year, month ).equals( "" ) )
                    {
                        nbFemale = Integer
                                .parseInt( extractDataWarehouseDao.countAllBySexByMonth( 1, action, year, month ) );
                        dataset.addValue( nbFemale, "Female", month );
                    }
                }

                // If user chose Group
                if ( type.equals( "Group" ) )
                {

                    for ( String group : groups )
                    {
                        // counts number of action for each group
                        // If no action nothing is written, and we don't display
                        // the
                        // sum
                        // of all groups.
                        if ( !group.equals( "All" )
                                && !extractDataWarehouseDao.countAllByGroupByMonth( group, action, year, month )
                                        .equals( "" ) )
                        {

                            nbByGroup = Integer.parseInt( extractDataWarehouseDao.countAllByGroupByMonth( group,
                                    action,
                                    year, month ) );
                            dataset.addValue( nbByGroup, group, month );
                        }
                    }
                }
            }
        }

        return dataset;
    }

    private JFreeChart createChart( String title, final CategoryDataset dataset, boolean legend, boolean tooltips,
            boolean urls ) {

        // create the chart
        final JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Months", // domain axis label
                "Number of activity", // range axis label
                dataset, PlotOrientation.VERTICAL, legend, tooltips, urls );

        // set the background color for the chart
        chart.setBackgroundPaint( Color.white );

        // get a reference to the plot for further customisation
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint( Color.lightGray );
        plot.setDomainGridlinePaint( Color.white );
        plot.setRangeGridlinePaint( Color.white );

        // set the range axis to display integers only
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );

        // disable bar outlines
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline( false );

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions( Math.PI / 6.0 )
                );

        return chart;

    }

}