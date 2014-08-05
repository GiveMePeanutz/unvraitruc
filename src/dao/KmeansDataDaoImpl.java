package dao;

import static dao.DAOUtility.fermeturesSilencieuses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.KmeansDataType;

public class KmeansDataDaoImpl implements KmeansDataDao {

    private DAOFactory          daoFactory;

    private static final String SQL_USER_ACTIVITY_COUNT  = "SELECT username, count(pageName) FROM fact_table GROUP BY username";
    private static final String SQL_GROUP_ACTIVITY_COUNT = "SELECT groupName, count(pageName) FROM fact_table f, user_group ug WHERE f.username=ug.username GROUP BY groupName";

    private static final String SQL_USER_COUNT           = "SELECT count(distinct username) FROM fact_table";
    private static final String SQL_GROUP_COUNT          = "SELECT count(distinct groupName) FROM user_group WHERE username IN ( SELECT username FROM fact_table )";

    KmeansDataDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public KmeansDataType[] getUserActivity() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2;
        KmeansDataType[] result;

        try {

            connection = daoFactory.getConnection();
            preparedStatement2 = connection.prepareStatement( SQL_USER_COUNT );
            resultSet2 = preparedStatement2.executeQuery();
            int size = 0;
            if ( resultSet2.next() ) {
                size = resultSet2.getInt( "count(distinct username)" );
            }

            result = new KmeansDataType[size];

            preparedStatement1 = connection.prepareStatement( SQL_USER_ACTIVITY_COUNT );
            resultSet1 = preparedStatement1.executeQuery();
            int i = 0;
            while ( resultSet1.next() ) {
                KmeansDataType d = new KmeansDataType( resultSet1.getString( "username" ),
                        new double[] { resultSet1.getDouble( "count(pageName)" ) } );
                result[i] = d;
                i++;
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
            fermeturesSilencieuses( preparedStatement2,
                    connection );
        }

        return result;
    }

    @Override
    public KmeansDataType[] getGroupActivity() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2;
        KmeansDataType[] result;

        try {

            connection = daoFactory.getConnection();
            preparedStatement2 = connection.prepareStatement( SQL_GROUP_COUNT );
            resultSet2 = preparedStatement2.executeQuery();
            int size = 0;
            if ( resultSet2.next() ) {
                size = resultSet2.getInt( "count(distinct groupName)" );
            }

            result = new KmeansDataType[size];

            preparedStatement1 = connection.prepareStatement( SQL_GROUP_ACTIVITY_COUNT );
            resultSet1 = preparedStatement1.executeQuery();
            int i = 0;
            while ( resultSet1.next() ) {
                KmeansDataType d = new KmeansDataType( resultSet1.getString( "groupName" ),
                        new double[] { resultSet1.getDouble( "count(pageName)" ) } );
                result[i] = d;
                i++;
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
            fermeturesSilencieuses( preparedStatement2,
                    connection );
        }

        return result;
    }

    @Override
    public int countActiveUsers() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1;
        int count = 0;
        try {
            connection = daoFactory.getConnection();
            preparedStatement1 = connection.prepareStatement( SQL_USER_COUNT );
            resultSet1 = preparedStatement1.executeQuery();
            if ( resultSet1.next() ) {
                count = resultSet1.getInt( "count(distinct username)" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
        }
        return count;
    }

    @Override
    public int countActiveGroups() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1;
        int count = 0;
        try {
            connection = daoFactory.getConnection();
            preparedStatement1 = connection.prepareStatement( SQL_GROUP_COUNT );
            resultSet1 = preparedStatement1.executeQuery();
            if ( resultSet1.next() ) {
                count = resultSet1.getInt( "count(distinct groupName)" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connection );
        }
        return count;
    }

}
