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
	private static final String SQL_INSERT = "INSERT INTO User (username, address, birthDate, email, firstName, lastName, password, phone, photoURL, promotion, regDate, sex) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE_BY_USERNAME = "DELETE FROM User WHERE username = ?";

	UserDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(User user) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedValues = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion,
					SQL_INSERT, true, user.getUsername(), user.getAddress(), user.getBirthDate(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getPhone(), user.getPhone(), user.getPhotoURL(), user.getPromotion(), new Timestamp( user.getRegDate().getMillis() ) , user.getSex());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException(
						"Failed to create user. No row added");
			}
			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(autoGeneratedValues, preparedStatement,
					connexion);
		}
	}

	@Override
	public User find(String username) throws DAOException {
		return find(SQL_SELECT_BY_USERNAME, username);
	}

	@Override
	public List<User> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                users.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
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

	private User find( String sql, Object... objets ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, objets );
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if ( resultSet.next() ) {
                user = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return user;
    }
	
	
	private static User map(ResultSet resultSet1) throws SQLException {
		
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
