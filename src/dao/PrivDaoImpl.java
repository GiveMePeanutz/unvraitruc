package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Priv;

public class PrivDaoImpl implements PrivDao {

	private DAOFactory          daoFactory;

	private static final String SQL_SELECT                = "SELECT privName, privDescription FROM Priv ORDER BY privName";
	private static final String SQL_SELECT_BY_PRIVNAME    = "SELECT privName, privDescription FROM Priv WHERE privName = ?";
	private static final String SQL_SELECT_PRIV_MENUPATHS = "SELECT menuPath FROM priv_menu WHERE privName = ? ORDER BY menuPath";
	private static final String SQL_SELECT_PRIV_GROUPS    = "SELECT groupName FROM web_app_db.group WHERE groupName IN (SELECT groupName FROM user_group WHERE username = ?) ORDER BY groupName";

	private static final String SQL_MODIFY_PRIV           = "UPDATE priv SET privDescription = ? WHERE privName  = ?";

	private static final String SQL_INSERT                = "INSERT INTO Priv ( privName, privDescription) VALUES ( ?, ?)";
	private static final String SQL_INSERT_PRIV_MENU      = "INSERT INTO priv_menu (menuPath , privName) VALUES (? , ?)";
	private static final String SQL_DELETE_BY_PRIVNAME    = "DELETE FROM Priv WHERE privName = ?";
	private static final String SQL_DELETE_PRIV_MENUPATHS = "DELETE FROM priv_menu WHERE privName = ?";

	PrivDaoImpl( DAOFactory daoFactory ) {
		this.daoFactory = daoFactory;
	}

	/*
	 * Creates a privilege in the database using as parameter a privilege bean
	 */
	public void create( Priv priv ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		try {
			// Retrieves a connection from the factory 
			connexion = daoFactory.getConnection();

			// prepared statement of the insert query. This query creates a new privilege in the table with the
			// information from the priv bean
			preparedStatement1 = initialisationRequetePreparee( connexion,
					SQL_INSERT, true, priv.getPrivName(), priv.getPrivDescription() );
			int statut1 = preparedStatement1.executeUpdate();

			// if the update failed
			if ( statut1 == 0 ) {
				throw new DAOException(
						"Failed to create privilege. No row added" );
			}
			else{
				for ( int menuPath : priv.getMenuPaths() ) {
					// prepared statement of the second insert query. This query inserts a new line in the priv_menu
					// table : links the created priv to a menu path
					preparedStatement2 = initialisationRequetePreparee( connexion,
							SQL_INSERT_PRIV_MENU, true, menuPath, priv.getPrivName() );
					int statut2 = preparedStatement2.executeUpdate();
					if ( statut2 == 0 ) {
						throw new DAOException(
								"Failed to create menuPath-privilege association. No row added" );
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
	 * Returns a privlege bean containing all the information of the privilege whose privName is passed as parameter
	 */
	@Override
	public Priv find( String privName ) throws DAOException {
		Connection connection = null;

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet3 = null;

		Priv priv = null;
		String sql = SQL_SELECT_BY_PRIVNAME;
		String sql2 = SQL_SELECT_PRIV_MENUPATHS;
		String sql3 = SQL_SELECT_PRIV_GROUPS;

		try {
			/* Retrieves a connection from the factory */
			connection = daoFactory.getConnection();

			/* prepared statements of the queries that :
			 * - 1 : retrieves priv information from priv table
			 * - 2 : retrieves priv menuPaths from priv_menu table
			 * - 3 : retrieves priv groups from the gropu_priv table
			 */
			preparedStatement = initialisationRequetePreparee( connection, sql, false, privName );
			resultSet = preparedStatement.executeQuery();
			preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, privName );
			resultSet2 = preparedStatement2.executeQuery();
			preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, privName );
			resultSet3 = preparedStatement3.executeQuery();

			// if a priv is retrieved from the database, the resultsets are custom mapped into a priv bean
			if ( resultSet.next() ) {
				priv = map( resultSet, resultSet2, resultSet3 );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( resultSet, preparedStatement, connection );
			fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
			fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
		}

		// returns the priv bean filled with the queried information
		return priv;
	}

	/*
	 * Returns all the privileges of the database in the form of a list of privilege beans
	 */
	@Override
	public List<Priv> list() throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		ResultSet resultSet3 = null;
		List<Priv> privs = new ArrayList<Priv>();

		try {

			/* Retrieves a connection from the factory */
			connection = daoFactory.getConnection();

			// prepared statement of the query that retrieves all the privs from the database
			preparedStatement = connection.prepareStatement( SQL_SELECT );
			resultSet = preparedStatement.executeQuery();

			/* For every priv retrieved with the first query and stored in ResutSet :
			 * - we query the database to retrieve the associated menu paths and groups
			 * - we store the retrieved information in a bean thanks to the custom map method
			 * - add this bean to the list
			 */
			while ( resultSet.next() ) {
				preparedStatement2 = initialisationRequetePreparee( connection, SQL_SELECT_PRIV_MENUPATHS, false,
						resultSet.getString( "privName" ) );
				resultSet2 = preparedStatement2.executeQuery();
				preparedStatement3 = initialisationRequetePreparee( connection, SQL_SELECT_PRIV_GROUPS, false,
						resultSet.getString( "privName" ) );
				resultSet3 = preparedStatement3.executeQuery();
				privs.add( map( resultSet, resultSet2, resultSet3 ) );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( resultSet, preparedStatement, connection );
			fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
			fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
		}

		//returns the list of priv beans 
		return privs;
	}

	/*
	 * Deletes from the database the privilege whose privName is passed as parameter
	 */
	@Override
	public void delete( String privName ) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			/* Retrieves a connection from the factory */
			connexion = daoFactory.getConnection();

			//prepared statement o the deletion query
			preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_PRIVNAME, true, privName );
			int statut = preparedStatement.executeUpdate();

			// If the deletion failed
			if ( statut == 0 ) {
				throw new DAOException( "Failed to delete privilege, no row deleted." );
			}
		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement, connexion );
		}

	}

	/*
	 * Modifies the privilege in the database with the corresponding privName extracted from the bean passed as argument
	 */
	@Override
	public void modify( Priv priv ) throws DAOException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;

		try {
			/* Retrieves a connection from the factory */
			connection = daoFactory.getConnection();

			// prepared statement of the modify priv query. This query modifies the priv whose privName has been
			// extracted from the bean passed as parameter. It modifies the remaining info from the bean.
			preparedStatement1 = initialisationRequetePreparee( connection,
					SQL_MODIFY_PRIV, true, priv.getPrivDescription(), priv.getPrivName() );
			int statut1 = preparedStatement1.executeUpdate();
			if ( statut1 == 0 ) {
				throw new DAOException(
						"Failed to modify priv. No row modified" );
			}

			// Here we delete all the priv/menuPath associations
			preparedStatement2 = initialisationRequetePreparee( connection, SQL_DELETE_PRIV_MENUPATHS, true,
					priv.getPrivName() );
			int statut2 = preparedStatement2.executeUpdate();
			if ( statut2 == 0 ) {
				throw new DAOException(
						"Failed to delete privilege menus. No rows deleted" );
			}

			// And here we create the priv/menuPath associations
			for ( Integer menuPath : priv.getMenuPaths() ) {
				preparedStatement3 = initialisationRequetePreparee( connection,
						SQL_INSERT_PRIV_MENU, true, menuPath, priv.getPrivName() );
				int statut3 = preparedStatement3.executeUpdate();
				if ( statut3 == 0 ) {
					throw new DAOException(
							"Failed to create menu_privilege association. No row added" );
				}
			}

		} catch ( SQLException e ) {
			throw new DAOException( e );
		} finally {
			fermeturesSilencieuses( preparedStatement1,
					connection );
			fermeturesSilencieuses( preparedStatement2,
					connection );
			fermeturesSilencieuses( preparedStatement3,
					connection );
		}
	}


	/*
	 * Static method taking as parameter 3 resultSets :
	 * - 1 : ResultSet of the select privilege query
	 * - 2 : ResultSet of the select privilege menuPaths query
	 * - 3 : ResultSet of the select privilege groups query
	 */
	private static Priv map( ResultSet resultSet, ResultSet resultSet2, ResultSet resultSet3 ) throws SQLException {

		Priv priv = new Priv();
		priv.setPrivName( resultSet.getString( "privName" ) );
		priv.setPrivDescription( resultSet.getString( "privDescription" ) );

		ArrayList<Integer> menuPaths = new ArrayList<Integer>();
		resultSet2.beforeFirst();
		while ( resultSet2.next() ) {
			menuPaths.add( resultSet2.getInt( "menuPath" ) );
		}
		priv.setMenuPaths( menuPaths );

		ArrayList<String> groupNames = new ArrayList<String>();
		resultSet3.beforeFirst();
		while ( resultSet2.next() ) {
			groupNames.add( resultSet3.getString( "groupName" ) );
		}
		priv.setGroupNames( groupNames );

		return priv;
	}



}
