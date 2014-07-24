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

    private static List<String> months                = Arrays.asList( "All", "January", "February", "March",
                                                              "April",
                                                              "May",
                                                              "June", "July", "August", "September", "October",
                                                              "November", "December" );
    private static List<String> days                  = Arrays.asList( "All", "Monday", "Tuesday", "Wednesday",
                                                              "Thrusday",
                                                              "Friday", "Saturday", "Sunday" );

    private static String       SELECT_DISTINCT_YEAR  = "SELECT DISTINCT year FROM timeDim";
    private static String       SELECT_DISTINCT_GROUP = "SELECT DISTINCT groupName FROM groupDim";
    private static String       FIND_RESULT_MONTH     = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, timeDim td, activityDim ad WHERE dwft.userId=ud.userId AND dwft.timeId=td.timeID AND dwft.activityId=ad.activityId AND ud.groupID=gd.groupId AND ud.sex= ? AND gd.groupName = ? AND ad.isAction = ? AND td.hour = ? AND td.day = ? AND td.monthName = ? AND td.year = ?";
    private static String       FIND_RESULT_WEEK      = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, timeDim td, activityDim ad WHERE dwft.userId=ud.userId AND dwft.timeId=td.timeID AND dwft.activityId=ad.activityId AND ud.groupID=gd.groupId AND ud.sex= ? AND gd.groupName = ? AND ad.isAction = ?  AND td.dayName= ? AND td.week = ? AND td.year = ?";
    private static String       FIND_RESULT_BY_SEX    = "SELECT count FROM dwFactTable dwft, userDim ud, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.sex= ? AND ad.isAction = ? AND td.year = ? ";
    private static String       FIND_RESULT_BY_GROUP  = "SELECT count FROM dwFactTable dwft, userDim ud, groupDim gd, activityDim ad, timeDim td WHERE dwft.userId=ud.userId AND dwft.activityId=ad.activityId AND dwft.timeId=td.timeID AND ud.groupID=gd.groupId AND gd.groupName = ? AND ad.isAction = ? AND td.year = ? ";

    ExtractDataWarehouseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    public List<String> getMonths() {
        return months;
    }

    public List<String> getDays() {
        return days;
    }

    public List<String> listYear() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> years = new ArrayList<String>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SELECT_DISTINCT_YEAR );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

                years.add( resultSet.getString( "year" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return years;
    }

    public List<String> listGroup() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> groups = new ArrayList<String>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SELECT_DISTINCT_GROUP );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

                groups.add( resultSet.getString( "groupName" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return groups;
    }

    @Override
    public String countMonth( DataWarehouseLine dWL ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_MONTH, false, dWL.getSex(),
                    dWL.getGroup(), dWL.getActivity(), dWL.getHour(), dWL.getDay(), dWL.getMonth(), dWL.getYear() );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    public String countWeek( DataWarehouseLine dWL ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_WEEK, false, dWL.getSex(),
                    dWL.getGroup(), dWL.getActivity(), dWL.getDayOfWeek(), dWL.getWeek(), dWL.getYear() );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    @Override
    public String countAllBySex( int sex, int action, int year ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_SEX, false, sex, action, year );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

                result = resultSet.getString( "count" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }
        return result;
    }

    @Override
    public String countAllByGroup( String group, int action, int year ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        String result = "";

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, FIND_RESULT_BY_GROUP, false, group,
                    action, year );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {

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
