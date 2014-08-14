package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.Group;

public class GroupDaoImpl implements GroupDao {

    private DAOFactory          daoFactory;

    private static final String SQL_SELECT              = "SELECT  groupName, groupDescription FROM web_app_db.Group ORDER BY groupName";
    private static final String SQL_SELECT_BY_GROUPNAME = "SELECT groupName, groupDescription FROM web_app_db.Group WHERE groupName = ?";
    private static final String SQL_SELECT_GROUP_PRIVS  = "SELECT privName FROM Priv WHERE privName IN (SELECT privName FROM Group_Priv WHERE groupName = ?) ORDER BY privName";
    private static final String SQL_SELECT_GROUP_USERS  = "SELECT * FROM user WHERE username IN (SELECT username FROM user_group WHERE groupName = ?) ORDER BY username";

    private static final String SQL_MODIFY_GROUP		= "UPDATE web_app_db.group SET groupDescription=? WHERE groupName=?";
    
    private static final String SQL_INSERT              = "INSERT INTO web_app_db.Group ( groupName, groupDescription) VALUES ( ?, ?)";
    private static final String SQL_INSERT_GROUP_PRIV   = "INSERT INTO Group_Priv (groupName , privName) VALUES (? , ?)";

    private static final String SQL_DELETE_BY_GROUPNAME = "DELETE FROM web_app_db.Group WHERE groupName = ?";
    private static final String SQL_DELETE_GROUP_PRIVS	= "DELETE FROM group_priv WHERE groupName= ? ";

    GroupDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    /*
	 * Creates a group in the database using as parameter a group bean
	 */
	@Override
    public void create( Group group ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            // Retrieves a connection from the factory 
            connexion = daoFactory.getConnection();
            
            // prepared statement of the insert query. This query creates a new group in the table with the
        	// information from the group bean
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, group.getGroupName(), group.getGroupDescription() );
            int statut1 = preparedStatement1.executeUpdate();
            
            // status1 == 0 indicates the query failed
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create group. No row added" );
            }
            else{
	            for ( String privName : group.getPrivNames() ) {
	            	// prepared statement of the second insert query. This query inserts a new line in the group_priv
	            	// table : links the created group to a privilege
	            	preparedStatement2 = initialisationRequetePreparee( connexion,
	                        SQL_INSERT_GROUP_PRIV, true, group.getGroupName(), privName );
	                int statut2 = preparedStatement2.executeUpdate();
	                if ( statut2 == 0 ) {
	                    throw new DAOException(
	                            "Failed to create group-privilege association. No row added" );
	                }
	            }
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

	
	/*
	 *  Returns a group bean containing all the informatino of the group whose name is passed as parameter
	 */
    @Override
    public Group find( String groupName ) throws DAOException {
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet3 = null;

        Group group = null;
        String sql = SQL_SELECT_BY_GROUPNAME;
        String sql2 = SQL_SELECT_GROUP_PRIVS;
        String sql3 = SQL_SELECT_GROUP_USERS;

        try {
            /* Retrieves a connection from the factory */
            connection = daoFactory.getConnection();
            
           /* prepared statements of the queries that :
           * - 1 : retrieves group information from group table
           * - 2 : retrieves group privileges from group_priv table
           * - 3 : retrieves groups users from the user_group table
           */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, groupName );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, groupName );
            resultSet2 = preparedStatement2.executeQuery();
            preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, groupName );
            resultSet3 = preparedStatement3.executeQuery();

            // if a group is retrieved from the database, the resultsets are custom mapped into a group bean
            if ( resultSet.next() ) {
                group = map( resultSet, resultSet2, resultSet3 );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }
        
        // returns the groups bean filled with the queried information
        return group;
    }
    
    /*
	 * Returns all the groups of the database in the form of a list of group beans
	 */
    @Override
    public List<Group> list() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        ResultSet resultSet3 = null;
        List<Group> groups = new ArrayList<Group>();

        try {
            /* Retrieves a connection from the factory */
        	connection = daoFactory.getConnection();
        	
        	// prepared statement of the query that retrieves all the groups from the database
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            
            /* For every group retrieved with the first query and stored in ResutSet :
             * - we query the database to retrieve the associated privileges and users
             * - we store the retrieved information in a bean thanks to the custom map method
             * - add this bean to the list
             */
            while ( resultSet.next() ) {
                preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_GROUP_PRIVS, false,
                        resultSet.getString( "groupName" ) );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_GROUP_USERS, false,
                        resultSet.getString( "groupName" ) );
                resultSet3 = preparedStatement3.executeQuery();
                groups.add( map( resultSet, resultSet2, resultSet3 ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }
        
        //returns the list of group beans 
        return groups;
    }

    /*
	 * Deletes from the database the group whose groupName is passed as parameter
	 */
    @Override
    public void delete( String groupName ) throws DAOException {
    	Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            /* Retrieves a connection from the factory */
        	connexion = daoFactory.getConnection();
            
            // prepared statement of the deletion query
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_GROUPNAME, true,
                    groupName );
            int statut = preparedStatement.executeUpdate();
            // If the deletion failed
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete group, no row deleted." );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    
    /*
	 * Modifies the group in the database with the corresponding groupName extracted from the bean passed as argument
	 */
    @Override
	public void modify(Group group) throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;

		try {
            /* Retrieves a connection from the factory */
			connection = daoFactory.getConnection();
			
			// prepared statement of the modify group query. This query modifies the group whose groupName has been
         	// extracted from the bean passed as parameter. It modifies the remaining info from the bean.
			preparedStatement1 = initialisationRequetePreparee(connection,
					SQL_MODIFY_GROUP, true, group.getGroupDescription(), group.getGroupName());
			int statut1 = preparedStatement1.executeUpdate();
			if (statut1 == 0) {
				throw new DAOException(
						"Failed to modify group. No row modified");
			}
			
			// Here we delete all the group/priv associations
			preparedStatement2 = initialisationRequetePreparee(connection, SQL_DELETE_GROUP_PRIVS, true, group.getGroupName());
			int statut2 = preparedStatement2.executeUpdate();
			if (statut2 == 0) {
				throw new DAOException(
						"Failed to delete group privs. No rows deleted");
			}
			
			
			// And here we create the group/priv associations
			for (String privName : group.getPrivNames()) {
				preparedStatement3 = initialisationRequetePreparee( connection,
	                    SQL_INSERT_GROUP_PRIV, true, group.getGroupName(), privName );
	            int statut3 = preparedStatement3.executeUpdate();
	            if ( statut3 == 0 ) {
	                throw new DAOException(
	                        "Failed to create group_privilege association. No row added" );
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
    
    
    /*
	 * Static method taking as parameter 3 resultSets :
	 * - 1 : ResultSet of the select group query
	 * - 2 : ResultSet of the select group privileges query
	 * - 3 : ResultSet of the select group users query
	 */
    private static Group map( ResultSet resultSet, ResultSet resultSet2, ResultSet resultSet3 ) throws SQLException {

        Group group = new Group();
        group.setGroupName( resultSet.getString( "groupName" ) );
        group.setGroupDescription( resultSet.getString( "groupDescription" ) );
        
        group.setUsernames(new ArrayList<String>());
        
        ArrayList<String> privNames = new ArrayList<String>();
		resultSet2.beforeFirst();
		while(resultSet2.next()) {
			privNames.add(resultSet2.getString("privName"));
		}
		group.setPrivNames(privNames);

		ArrayList<String> usernames = new ArrayList<String>();
		resultSet3.beforeFirst();
		while(resultSet3.next()) {
			usernames.add(resultSet3.getString("username"));
		}
		group.setUsernames(usernames);

        return group;
    }

	

}
