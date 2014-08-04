package utilities;

//used to draw Chart

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import dao.ExtractDataWarehouseDao;

public class DrawChart {

    // Prepares a PieChart
    public JFreeChart getChart( String type, int action, int year, ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;

        // Creates the title according to what was chosen in fields.
        String title = "";
        if ( action == -1 )
            title = "Number of all activities";
        else if ( action == 0 )
            title = "Number of visited pages";
        else
            title = "Number of performed actions";

        title = title + "\n according to " + type + " Attribute in " + year;

        // creates the dataset
        dataset = fillDataset( type, action, year, extractDataWarehouseDao );

        // creates the chart with the right dataset
        JFreeChart chart = ChartFactory.createPieChart( title, dataset, legend, tooltips, urls );

        // writes numbers on the Chart
        PiePlot piePlot = (PiePlot) chart.getPlot();
        piePlot.setLabelGenerator( new StandardPieSectionLabelGenerator( "{0}={1}" ) );

        return chart;
    }

    // Fills the dataset with data from database.
    public DefaultPieDataset fillDataset( String type, int action, int year,
            ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultPieDataset dataset = new DefaultPieDataset();

        // retrieves all existing groups
        List<String> groups = extractDataWarehouseDao.listGroup();

        int nbMale;
        int nbFemale;
        int nbByGroup;

        // If user chose Sex
        if ( type.equals( "Sex" ) )
        {
            // counts number of action for each sex
            // If no action nothing is written
            if ( !extractDataWarehouseDao.countAllBySex( 0, action, year ).equals( "" ) )
            {
                nbMale = Integer.parseInt( extractDataWarehouseDao.countAllBySex( 0, action, year ) );
                dataset.setValue( "Male", nbMale );
            }

            if ( !extractDataWarehouseDao.countAllBySex( 1, action, year ).equals( "" ) )
            {
                nbFemale = Integer.parseInt( extractDataWarehouseDao.countAllBySex( 1, action, year ) );
                dataset.setValue( "Female", nbFemale );
            }
        }

        // If user chose Group
        if ( type.equals( "Group" ) )
        {
            for ( String group : groups )
            {
                // counts number of action for each group
                // If no action nothing is written, and we don't display the sum
                // of all groups.
                if ( !group.equals( "All" )
                        && !extractDataWarehouseDao.countAllByGroup( group, action, year ).equals( "" ) )
                {

                    nbByGroup = Integer.parseInt( extractDataWarehouseDao.countAllByGroup( group, action, year ) );
                    dataset.setValue( group, nbByGroup );
                }
            }
        }

        return dataset;
    }
}
