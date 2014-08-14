package dao;

import java.util.List;

import beans.DataWarehouseLine;

//Interface of courseDao
public interface ExtractDataWarehouseDao {

    /*
     * Returns all the days name in the form of a list of String
     */
    public List<String> getDays();

    /*
     * Returns all the months name in the form of a list of String
     */
    public List<String> getMonths();

    /*
     * Returns all the years of the database in the form of a list of String
     */
    public List<String> listYear() throws DAOException;

    /*
     * Returns all the groups of the database in the form of a list of String
     */
    public List<String> listGroup() throws DAOException;

    /*
     * Returns the number of activities done according to the parameters, with
     * the "month hierarchy"
     */
    public String countMonth( DataWarehouseLine dWL ) throws DAOException;

    /*
     * Returns the number of activities done according to the parameters, with
     * the "week hierarchy"
     */
    public String countWeek( DataWarehouseLine dWL ) throws DAOException;

    /*
     * Returns the number of activities done according to the sex and kind of
     * action passed as parameter, for the entire year also passed as parameter.
     */
    public String countAllBySex( int sex, int action, int year ) throws DAOException;

    /*
     * Returns the number of activities done according to the sex and kind of
     * action passed as parameters, for each month of the year also passed as
     * parameter
     */
    public String countAllBySexByMonth( int sex, int action, int year, String Month ) throws DAOException;

    /*
     * Returns the number of activities done according to the group and kind of
     * action passed as parameter, for the entire year also passed as parameter.
     */
    public String countAllByGroup( String group, int action, int year ) throws DAOException;

    /*
     * Returns the number of activities done according to the group and kind of
     * action passed as parameters, for each month of the year also passed as
     * parameter
     */
    public String countAllByGroupByMonth( String group, int action, int year, String month ) throws DAOException;

}
