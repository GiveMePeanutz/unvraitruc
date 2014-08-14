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
	private static final String SQL_SELECT_COURSE_USERS  = "SELECT username FROM user WHERE username IN (SELECT username FROM user_course WHERE courseName = ?) AND username IN (SELECT username FROM user_group WHERE groupName='Student') ORDER BY username";
	private static final String SQL_SELECT_COURSE_TEACHER  = "SELECT username FROM user WHERE username IN (SELECT username FROM user_course WHERE courseName = ?) AND username IN (SELECT username FROM user_group WHERE groupName='Teacher') ORDER BY username";


	private static final String SQL_MODIFY_COURSE        = "UPDATE course SET courseDescription= ?, courseYear = ? WHERE courseName = ?";

	private static final String SQL_INSERT               = "INSERT INTO Course ( courseName, courseYear, courseDescription, courseschedule) VALUES ( ?, ?, ?, ?)";
	private static final String SQL_INSERT_COURSE_USER   = "INSERT INTO user_course (username , courseName) VALUES (? , ?)";

	private static final String SQL_DELETE_BY_COURSENAME = "DELETE FROM Course WHERE courseName = ?";


	CourseDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

	/*
	 * Creates a course in the database using as parameter a course bean and a teacher name 
	 */
	@Override
	public void create( Course course , String teacherName) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		try {
			// Opens connection 
			connexion = daoFactory.getConnection();

			// prepared statement of the insert query. This query creates a new course in the table with the
			// information from the course bean
			preparedStatement1 = initialisationRequetePreparee( connexion,
					SQL_INSERT, true, course.getCourseName(), course.getCourseYear(), course.getCourseDescription(),
					course.getSchedule() );
			int statut1 = preparedStatement1.executeUpdate();

			// status1 == 0 indicates the query failed
			if ( statut1 == 0 ) {
				throw new DAOException(
						"Failed to create course. No row added" );
			}
			// If the insert succeeded we proceed to the second part of course creation : 
			//associate a course to a teacher
			else{

				// prepared statement of the second insert query. This query inserts a new line in the user_course
				// table : links the created course to the teacher in charge of it
				preparedStatement2 = initialisationRequetePreparee( connexion,
						SQL_INSERT_COURSE_USER, true,  teacherName, course.getCourseName() );
				int statut2 = preparedStatement2.executeUpdate();
				// verifies if the insert succeeded
				if ( statut2 == 0 ) {
					throw new DAOException(
							"Failed to create course_user association. No row added" );
				}
			}

		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement1,
					connexion );
		}
	}

	/*
	 * Returns a course bean containing all the information of the course whose courseName is passed as parameter
	 */
	@Override
	public Course find( String courseName ) throws DAOException {
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet3 = null;

		Course course = null;
		String sql = SQL_SELECT_BY_COURSENAME;
		String sql2 = SQL_SELECT_COURSE_USERS;
		String sql3 = SQL_SELECT_COURSE_TEACHER;

		try {
			// Retrieves a connection from the factory 
			connection = daoFactory.getConnection();

			/* Here the prepared statements are initialized, executed and their results are stored in the resultSets
			 * The queries here retrieve for the course whose courseName is passed as parameter its:
			 * - 1: basic information from the course table 
			 * - 2: subscribed students
			 * - 3: teacher in charge
			 */
			preparedStatement = initialisationRequetePreparee( connection, sql, false, courseName );
			resultSet = preparedStatement.executeQuery();
			preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, courseName );
			resultSet2 = preparedStatement2.executeQuery();
			preparedStatement3 = initialisationRequetePreparee( connection, sql3 , false, courseName );
			resultSet3 = preparedStatement3.executeQuery();
			/* If a course has been retrieved */
			if ( resultSet.next() ) {
				// Custom maps the results into a course bean
				course = map( resultSet, resultSet2, resultSet3 );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( resultSet, preparedStatement, connection );
			fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
		}

		// returns the bean filled with all the queried information
		return course;
	}

	/*
	 * Returns all the courses of the database in the form of a list of course beans
	 */
	@Override
	public List<Course> list() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		List<Course> courses = new ArrayList<Course>();

		try {
			// Retrieves a connection from the factory 
			connection = daoFactory.getConnection();

			// Prepared statement of the select query used to retrieve all courses from the database
			preparedStatement = connection.prepareStatement( SQL_SELECT );
			resultSet = preparedStatement.executeQuery();

			// here we loop on every course returned from the first query to retrieve its users and teachers
			while ( resultSet.next() ) {
				preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_COURSE_USERS, false,
						resultSet.getString( "courseName" ) );
				resultSet2 = preparedStatement2.executeQuery();
				preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_COURSE_TEACHER, false,
						resultSet.getString( "courseName" ) );
				resultSet3 = preparedStatement3.executeQuery();
				// adds to the list the course bean filled within the custom map method
				courses.add( map( resultSet, resultSet2, resultSet3) );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( resultSet, preparedStatement, connection );
			fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
		}

		return courses;
	}

	/*
	 * Deletes from the database the course whose courseName is passed as parameter
	 */
	@Override
	public void delete( String courseName ) throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// Retrieves a connection from the factory 
			connection = daoFactory.getConnection();

			// prepared statement of the delete query : deletes in from the database the course whose courseName is passed as parameter
			preparedStatement = initialisationRequetePreparee( connection, SQL_DELETE_BY_COURSENAME, true, courseName );
			int statut = preparedStatement.executeUpdate();

			// verifies if the update succeeded
			if ( statut == 0 ) {
				throw new DAOException( "Failed to delete course, no row deleted." );
			}

		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement, connection );
		}
	}


	/*
	 * Modifies the course in the database with the corresponding courseName extracted from the bean passed as argument
	 */
	@Override
	public void modify( Course course ) throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;

		try {
			// Retrieves a connection from the factory 
			connection = daoFactory.getConnection();

			// prepared statement of the modify course query. This query modifies the course whose courseName has been
			// extracted from the bean passed as parameter. It modifies the remaining info from the bean.
			preparedStatement1 = initialisationRequetePreparee( connection,
					SQL_MODIFY_COURSE, true, course.getCourseDescription(), course.getCourseYear(),
					course.getCourseName() );
			int statut1 = preparedStatement1.executeUpdate();
			// verifies if the modification succeeded
			if ( statut1 == 0 ) {
				throw new DAOException(
						"Failed to modify priv. No row modified" );
			}

		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement1,
					connection );
		}
	}


	/*
	 * Static method taking as parameter 3 resultSets :
	 * - 1 : ResultSet of the select course query
	 * - 2 : ResultSet of the select course users query
	 * - 3 : ResultSet of the select course teacher query
	 */
	private static Course map( ResultSet resultSet, ResultSet resultSet2 , ResultSet resultSet3) throws SQLException {

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
		if (resultSet3.next()){
			course.setTeacher( resultSet3.getString( "username" ));
		}

		return course;

	}



}
