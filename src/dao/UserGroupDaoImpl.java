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

import beans.Group;
import beans.User;



public class UserGroupDaoImpl implements UserGroupDao {

	
	private DAOFactory daoFactory;
	
	private static final String SQL_INSERT = "INSERT INTO user_group (username , groupID) VALUES (? , ?)";
	private static final String SQL_SELECT_USER_GROUPS = "SELECT * FROM group WHERE groupID IN (SELECT groupID FROM user_group WHERE username = ?) ORDER BY groupName";
	private static final String SQL_SELECT_GROUP_USERS	 = "SELECT * FROM user WHERE username IN (SELECT username FROM user_group WHERE groupID = ?) ORDER BY username";
	private static final String SQL_DELETE_BY_USERNAME = "DELETE FROM user_group WHERE username = ?";
	private static final String SQL_DELETE_BY_GROUPID = "DELETE FROM user_group WHERE groupID = ?";
	private static final String SQL_DELETE =  "DELETE FROM user_group WHERE username = ? AND groupID = ?";

	UserGroupDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void create(int groupID, String username) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedValues = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion,
					SQL_INSERT, true, username, groupID);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException(
						"Failed to create user-group association. No row added");
			}
			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(autoGeneratedValues, preparedStatement,
					connexion);
		}
	}

	@Override
	public void delete(int groupID, String username) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE, true, username, groupID );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user-group association, no row deleted." );
            } else {
            	//course.setICourseID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
	}

	@Override
	public void deleteBygroupID(int groupID) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_GROUPID, true, groupID );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user-group association by groupID, no row deleted." );
            } else {
            	//course.setICourseID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

	@Override
	public void deleteByUsername(String username) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_USERNAME, true, username );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete user-group association by username, no row deleted." );
            } else {
            	//course.setICourseID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
		
	}

	@Override
	public List<User> listGroupUsers(int groupID) throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee(connection, SQL_SELECT_GROUP_USERS,false, groupID );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                users.add( mapUser( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return users;
	}

	@Override
	public List<Group> listUserGroups(String username) throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Group> groups = new ArrayList<Group>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee(connection, SQL_SELECT_USER_GROUPS,false, username );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                groups.add( mapGroup( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return groups;
	}
	
	private static User mapUser(ResultSet resultSet) throws SQLException {
		
		User user = new User();
		user.setUsername(resultSet.getString("username"));
		user.setAddress(resultSet.getString("address"));
		user.setBirthDate( new DateTime( resultSet.getTimestamp("birthDate")));
		user.setEmail(resultSet.getString("email"));
		user.setFirstName(resultSet.getString("firstName"));
		user.setLastName(resultSet.getString("lastName"));
		user.setPassword(resultSet.getString("password"));
		user.setPhone(resultSet.getString("phone"));
		user.setPhotoURL(resultSet.getString("photoURL"));
		user.setPromotion(resultSet.getString("promotion"));
		user.setRegDate( new DateTime( resultSet.getTimestamp( "regDate" ) ));
		user.setSex(resultSet.getInt("sex"));
		return user;
	}
	
	private static Group mapGroup(ResultSet resultSet) throws SQLException {
		
		Group group = new Group();
		group.setGroupID(resultSet.getInt("groupID"));
		group.setGroupName(resultSet.getString("groupName"));
		group.setGroupDescription(resultSet.getString("groupDescription"));
		return group;
	}
	
}
