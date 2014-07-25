package utilities;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import dao.ExtractDataWarehouseDao;

public class DrawChart {

    public JFreeChart getChart( String type, int action, int year, ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        boolean legend = true;
        boolean tooltips = false;
        boolean urls = false;
        String title = "";
        if ( action == -1 )
            title = "Number of all activities";
        else if ( action == 0 )
            title = "Number of visited pages";
        else
            title = "Number of performed actions";

        title = title + "\n according to " + type + " Attribute in " + year;
        dataset = fillDataset( type, action, year, extractDataWarehouseDao );

        JFreeChart chart = ChartFactory.createPieChart( title, dataset, legend, tooltips, urls );
        PiePlot piePlot = (PiePlot) chart.getPlot();
        piePlot.setLabelGenerator( new StandardPieSectionLabelGenerator( "{0}={1}" ) );
        return chart;
    }

    public DefaultPieDataset fillDataset( String type, int action, int year,
            ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        List<String> groups = extractDataWarehouseDao.listGroup();
        int nbMale;
        int nbFemale;
        int nbByGroup;
        if ( type.equals( "Sex" ) )
        {

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
        if ( type.equals( "Group" ) )
        {
            for ( String group : groups )
            {
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
