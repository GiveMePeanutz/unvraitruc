package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Course;

public class CourseDaoImpl implements CourseDao {

    private DAOFactory          daoFactory;

    private static final String SQL_SELECT               = "SELECT courseName, courseYear, courseDescription, courseschedule FROM Course ORDER BY courseName";
    private static final String SQL_SELECT_BY_COURSENAME = "SELECT  courseName, courseYear, courseDescription, courseschedule FROM Course WHERE courseName = ?";
    private static final String SQL_SELECT_COURSE_USERS  = "SELECT username FROM user WHERE username IN (SELECT username FROM user_course WHERE courseName = ?) ORDER BY username";

    private static final String SQL_INSERT               = "INSERT INTO Course ( courseName, courseYear, courseDescription, courseschedule) VALUES ( ?, ?, ?, ?)";
    private static final String SQL_INSERT_COURSE_USER   = "INSERT INTO user_course (username , courseName) VALUES (? , ?)";

    private static final String SQL_DELETE_BY_COURSENAME = "DELETE FROM Course WHERE courseName = ?";

    // private static final String SQL_DELETE_FROM_USERCOURSE_BY_COURSENAME =
    // "DELETE FROM user_course WHERE courseName = ?";

    CourseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void create( Course course ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, course.getCourseName(), course.getCourseYear(), course.getCourseDescription(),
                    course.getSchedule() );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create client. No row added" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connexion );
            fermeturesSilencieuses( preparedStatement2,
                    connexion );
        }
    }

    @Override
    public Course find( String courseName ) throws DAOException {
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;

        Course course = null;
        String sql = SQL_SELECT_BY_COURSENAME;
        String sql2 = SQL_SELECT_COURSE_USERS;

        try {
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, courseName );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, courseName );
            resultSet2 = preparedStatement2.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
                course = map( resultSet, resultSet2 );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
        }

        return course;
    }

    @Override
    public List<Course> list() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        List<Course> courses = new ArrayList<Course>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ) {
                preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_COURSE_USERS, false,
                        resultSet.getString( "courseName" ) );
                resultSet2 = preparedStatement2.executeQuery();
                courses.add( map( resultSet, resultSet2 ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
        }

        return courses;
    }

    @Override
    public void delete( String courseName ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connection, SQL_DELETE_BY_COURSENAME, true, courseName );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete course, no row deleted." );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connection );
        }
    }

    private static Course map( ResultSet resultSet, ResultSet resultSet2 ) throws SQLException {

        Course course = new Course();
        course.setCourseName( resultSet.getString( "courseName" ) );
        course.setCourseYear( resultSet.getInt( "courseYear" ) );
        course.setCourseDescription( resultSet.getString( "courseDescription" ) );
        course.setSchedule( resultSet.getString( "courseSchedule" ) );
        ArrayList<String> usernames = new ArrayList<String>();
        resultSet2.beforeFirst();
        while ( resultSet2.next() ) {
            usernames.add( resultSet2.getString( "username" ) );
        }
        course.setUsernames( usernames );

        return course;

    }

}
