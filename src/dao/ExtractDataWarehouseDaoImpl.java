package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.DataWarehouseLine;

public class ExtractDataWarehouseDaoImpl implements ExtractDataWarehouseDao {

    private DAOFactory          daoFactory;

    private static List<String> months                         = Arrays.asList( "All", "January", "February", "March",
                                                                       "April",
                                                                       "May",
                                                                       "June", "July", "August", "September",
                                                                       "October",
                                                                       "November", "December" );
    private static List<String> days                           = Arrays.asList( "All", "Monday", "Tuesday",
                                                                       "Wednesday",
                                                                       "Thursday",
                                                                       "Friday", "Saturday", "Sunday" );

    private static String       SELECT_DISTINCT_YEAR           = "SELECT DISTINCT year FROM timeDim";
    private static String       SELECT_DISTINCT_GROUP          = "SELECT DISTINCT groupName FROM groupDim";
    private static String       FIND_RESULT_MONTH              = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, timeDim td, activityDim ad WHERE dwft.userId=ud.userId AND dwft.timeId=td.timeID AND dwft.activityId=ad.activityId AND ud.groupID=gd.groupId AND ud.sex= ? AND gd.groupName = ? AND ad.isAction = ? AND td.hour = ? AND td.day = ? AND td.monthName = ? AND td.year = ?";
    private static String       FIND_RESULT_WEEK               = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, timeDim td, activityDim ad WHERE dwft.userId=ud.userId AND dwft.timeId=td.timeID AND dwft.activityId=ad.activityId AND ud.groupID=gd.groupId AND ud.sex= ? AND gd.groupName = ? AND ad.isAction = ?  AND td.dayName= ? AND td.week = ? AND td.year = ?";
    private static String       FIND_RESULT_BY_SEX             = "SELECT count FROM dwFactTable dwft, userDim ud, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.sex= ? AND ad.isAction = ? AND td.year = ? AND td.week =-1 AND dayName='All' AND ud.groupId=1 ";
    private static String       FIND_RESULT_BY_SEX_AND_MONTH   = "SELECT count FROM dwFactTable dwft, userDim ud, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.sex= ? AND ad.isAction = ? AND td.year = ? AND td.monthName =? AND day=-1 AND hour=-1 AND ud.groupId=1 ";
    private static String       FIND_RESULT_BY_GROUP           = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.groupID=gd.groupId AND gd.groupName = ? AND ad.isAction = ? AND td.year = ? AND td.week =-1 AND dayName='All' AND ud.sex=-1 ";
    private static String       FIND_RESULT_BY_GROUP_AND_MONTH = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.groupID=gd.groupId AND gd.groupName = ? AND ad.isAction = ? AND td.year = ? AND td.monthName =? AND day=-1 AND hour=-1 AND ud.sex=-1 ";

    ExtractDataWarehouseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /*
     * Returns all the months name in the form of a list of String
     */
    public List<String> getMonths() {
        return months;
    }

    /*
     * Returns all the days name in the form of a list of String
     */
    public List<String> getDays() {
        return days;
    }

    /*
     * Returns all the years of the database in the form of a list of String
     */
    public List<String> listYear() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> years = new ArrayList<String>();

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve all the
            // years from the database
            preparedStatement = connection.prepareStatement( SELECT_DISTINCT_YEAR );
            resultSet = preparedStatement.executeQuery();
            // here we loop on every year returned from the first query
            while ( resultSet.next() ) {
                // adds the year to the list
                years.add( resultSet.getString( "year" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return years;
    }

    /*
     * Returns all the groups of the database in the form of a list of String
     */
    public List<String> listGroup() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> groups = new ArrayList<String>();

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve all the
            // groups from the database
            preparedStatement = connection.prepareStatement( SELECT_DISTINCT_GROUP );
            resultSet = preparedStatement.executeQuery();
            // here we loop on every group returned from the first query
            while ( resultSet.next() ) {
                // adds the year to the list
                groups.add( resultSet.getString( "groupName" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return groups;
    }

    /*
     * Returns the number of activities done according to the parameters, with
     * the "month hierarchy"
     */
    public String countMonth( DataWarehouseLine dWL ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_MONTH, false, dWL.getSex(),
                    dWL.getGroup(), dWL.getActivity(), dWL.getHour(), dWL.getDay(), dWL.getMonth(), dWL.getYear() );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    /*
     * Returns the number of activities done according to the parameters, with
     * the "week hierarchy"
     */
    public String countWeek( DataWarehouseLine dWL ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_WEEK, false, dWL.getSex(),
                    dWL.getGroup(), dWL.getActivity(), dWL.getDayOfWeek(), dWL.getWeek(), dWL.getYear() );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    /*
     * Returns the number of activities done according to the sex and kind of
     * action passed as parameter, for the entire year also passed as parameter.
     */
    public String countAllBySex( int sex, int action, int year ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_SEX, false, sex, action, year );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    /*
     * Returns the number of activities done according to the sex and kind of
     * action passed as parameters, for each month of the year also passed as
     * parameter
     */
    public String countAllBySexByMonth( int sex, int action, int year, String month ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_SEX_AND_MONTH, false, sex,
                    action, year, month );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    /*
     * Returns the number of activities done according to the group and kind of
     * action passed as parameter, for the entire year also passed as parameter.
     */
    public String countAllByGroup( String group, int action, int year ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_GROUP, false, group,
                    action, year );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    /*
     * Returns the number of activities done according to the group and kind of
     * action passed as parameters, for each month of the year also passed as
     * parameter
     */
    public String countAllByGroupByMonth( String group, int action, int year, String month ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            // Prepared statement of the select query used to retrieve the right
            // number of activities from the database
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_GROUP_AND_MONTH, false,
                    group,
                    action, year, month );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result of the query
                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }
}
