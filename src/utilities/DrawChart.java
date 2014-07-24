package utilities;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
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
            title = "Number of all activities according to ";
        else if ( action == 0 )
            title = "Number of visited pages according to ";
        else
            title = "Number of performed actions according to ";

        title = title + type + " Attribute in " + year;
        dataset = fillDataset( type, action, year, extractDataWarehouseDao );

        JFreeChart chart = ChartFactory.createPieChart( title, dataset, legend, tooltips, urls );

        return chart;
    }

    public DefaultPieDataset fillDataset( String type, int action, int year,
            ExtractDataWarehouseDao extractDataWarehouseDao ) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        List<String> groups = extractDataWarehouseDao.listGroup();

        if ( type == "Sex" )
        {
            dataset.setValue( "Male", Integer.parseInt( extractDataWarehouseDao.countAllBySex( 0, action, year ) ) );
            dataset.setValue( "Female", Integer.parseInt( extractDataWarehouseDao.countAllBySex( 1, action, year ) ) );
        }
        if ( type == "Group" )
        {
            for ( String group : groups )
            {
                int value = Integer.parseInt( extractDataWarehouseDao.countAllByGroup( group, action, year ) );
                dataset.setValue( group, value );
            }
        }

        return dataset;
    }

}
