package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import beans.Course;
import beans.User;
import beans.User;

public class UserDaoImpl implements UserDao {

	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, promotion, regDate, sex FROM User ORDER BY username";
	private static final String SQL_SELECT_BY_USERNAME = "SELECT username, address, birthDate, email, firstName, lastName, password, phone, photoURL, promotion, regDate, sex FROM User WHERE username = ?";
	private static final String SQL_SELECT_USER_COURSES = "SELECT courseName FROM course WHERE courseName IN (SELECT courseName FROM user_course WHERE username = ?) ORDER BY courseName";
	private static final String SQL_SELECT_USER_GROUPS = "SELECT groupName FROM group WHERE groupName IN (SELECT groupName FROM user_group WHERE username = ?) ORDER BY groupName";
	
	private static final String SQL_INSERT = "INSERT INTO User (username, address, birthDate, email, firstName, lastName, password, phone, photoURL, promotion, regDate, sex) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_USER_COURSE = "INSERT INTO user_course (username , courseName) VALUES (? , ?)";
	private static final String SQL_INSERT_USER_GROUP = "INSERT INTO user_group (username , groupName) VALUES (? , ?)";
	
	private static final String SQL_DELETE_BY_USERNAME = "DELETE FROM User WHERE username = ?";

	UserDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(User user) throws DAOException {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;

		try {
			connection = daoFactory.getConnection();
			preparedStatement1 = initialisationRequetePreparee(connection,
					SQL_INSERT, true, user.getUsername(), user.getAddress(), user.getBirthDate(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getPhone(), user.getPhone(), user.getPhotoURL(), user.getPromotion(), new Timestamp( user.getRegDate().getMillis() ) , user.getSex());
			int statut1 = preparedStatement1.executeUpdate();
			if (statut1 == 0) {
				throw new DAOException(
						"Failed to create client. No row added");
			}
			
			
			
			
			for (String courseName : user.getCourseNames()) {
				preparedStatement2 = initialisationRequetePreparee( connection,
	                    SQL_INSERT_USER_COURSE, true, user.getUsername(), courseName );
	            int statut2 = preparedStatement2.executeUpdate();
	            if ( statut2 == 0 ) {
	                throw new DAOException(
	                        "Failed to create user-course association. No row added" );
	            }
			}
			
			for (String groupName : user.getGroupNames()) {
				preparedStatement3 = initialisationRequetePreparee( connection,
	                    SQL_INSERT_USER_GROUP, true, user.getUsername(), groupName );
	            int statut3 = preparedStatement3.executeUpdate();
	            if ( statut3 == 0 ) {
	                throw new DAOException(
	                        "Failed to create user-group association. No row added" );
	            }
			}
			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement1,
					connection);
			fermeturesSilencieuses(preparedStatement2,
					connection);
			fermeturesSilencieuses(preparedStatement3,
					connection);
		}
	}

	@Override
	public User find(String username) throws DAOException {
		Connection connection = null;
		
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet3 = null;
        
        User user = null;
        String sql = SQL_SELECT_BY_USERNAME;
        String sql2= SQL_SELECT_USER_COURSES;
        String sql3= SQL_SELECT_USER_GROUPS;

        try {
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, username );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, username );
            resultSet2 = preparedStatement2.executeQuery();
            preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, username );
            resultSet3 = preparedStatement3.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
            	user = map( resultSet, resultSet2, resultSet3 );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        return user;
	}

	@Override
	public List<User> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        List<User> users = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            
            while ( resultSet.next() ) {
            	preparedStatement2 = initialisationRequetePreparee(connection, SQL_SELECT_USER_COURSES, false, resultSet.getString("username") );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee(connection, SQL_SELECT_USER_GROUPS, false, resultSet.getString("username") );
                resultSet3 = preparedStatement3.executeQuery();
                users.add( map( resultSet , resultSet2, resultSet3) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        return users;
	}

	@Override
	public void delete(User user) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_USERNAME, true, user.getUsername() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user, no row deleted." );
            } else {
            	//course.setICourseID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

	}

	
	
	
	private static User map(ResultSet resultSet1, ResultSet resultSet2, ResultSet resultSet3) throws SQLException {
		
		User user = new User();
		user.setUsername(resultSet1.getString("username"));
		user.setAddress(resultSet1.getString("address"));
		user.setBirthDate( new DateTime( resultSet1.getTimestamp("birthDate")));
		user.setEmail(resultSet1.getString("email"));
		user.setFirstName(resultSet1.getString("firstName"));
		user.setLastName(resultSet1.getString("lastName"));
		user.setPassword(resultSet1.getString("password"));
		user.setPhone(resultSet1.getString("phone"));
		user.setPhotoURL(resultSet1.getString("photoURL"));
		user.setPromotion(resultSet1.getString("promotion"));
		user.setRegDate( new DateTime( resultSet1.getTimestamp( "regDate" ) ));
		user.setSex(resultSet1.getInt("sex"));
		
		
		resultSet2.beforeFirst();
		while(resultSet2.next()) {
			user.addCourseName(resultSet2.getString("courseName"));
		}
		
		resultSet3.beforeFirst();
		while(resultSet2.next()) {
			user.addGroupName(resultSet3.getString("GroupName"));
		}
		
		return user;
	}

	@Override
	public String getPassword(String username) throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        String sql = SQL_SELECT_BY_USERNAME;
        ResultSet resultSet = null;
        String pw = null;

        try {
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, username );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
                pw = resultSet.getString("password");
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return pw;
	}
	
}
