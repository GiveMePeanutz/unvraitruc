package dao;

import static dao.DAOUtility.fermeturesSilencieuses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NaiveBayesDaoImpl implements NaiveBayesDao {

    private DAOFactory          daoFactory;

    private static final String SQL_USER_COUNT        = "SELECT count(distinct username) FROM user";
    private static final String SQL_SELECT_CLASSNAME  = "SELECT distinct className FROM user WHERE className IS NOT NULL ";
    private static final String SQL_SELECT_COURSE     = "SELECT distinct courseName FROM user_course";
    private static final String SQL_USER_COURSE_COUNT = "SELECT count(*) FROM web_app_db.User_course";

    NaiveBayesDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /*
     * Returns how many users are registered in database
     */
    public int getUserCount() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        int result = 0;

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_USER_COUNT );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result
                result = resultSet.getInt( "count(distinct username)" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement,
                    connection );
        }
        return result;
    }

    /*
     * Returns all the ClassName of the database in the form of a list of String
     */
    public List<String> listClassName() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> classNames = new ArrayList<String>();

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT_CLASSNAME );
            resultSet = preparedStatement.executeQuery();
            // here we loop on every ClassName returned from the first query
            while ( resultSet.next() ) {
                // adds to the list the course name
                classNames.add( resultSet.getString( "className" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return classNames;
    }

    /*
     * Returns all the Courses of the database in the form of a list of
     * String(the course name)
     */
    public List<String> listCourse() throws DAOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        ResultSet resultSet = null;

        List<String> courses = new ArrayList<String>();

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT_COURSE );
            resultSet = preparedStatement.executeQuery();

            // here we loop on every course returned from the first query to
            // retrieve its name
            while ( resultSet.next() ) {
                // adds to the list the course name
                courses.add( resultSet.getString( "courseName" ) );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return courses;
    }

    /*
     * Returns how many user_course line are registered in database (a line =
     * one course for one user)
     */
    public int getUserCourseCount() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        int result = 0;

        try {

            // Retrieves a connection from the factory
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_USER_COURSE_COUNT );
            resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() ) {
                // Saves the result
                result = resultSet.getInt( "count(*)" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement,
                    connection );
        }
        return result;
    }
}
