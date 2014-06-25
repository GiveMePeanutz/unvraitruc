package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Group;

public class GroupDaoImpl implements GroupDao {

	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT = "SELECT groupName, groupName, groupDescription FROM web_app_db.Group ORDER BY groupName";
	private static final String SQL_SELECT_BY_GROUPNAME = "SELECT groupName, groupDescription FROM web_app_db.Group WHERE groupName = ?";
	private static final String SQL_INSERT = "INSERT INTO web_app_db.Group ( groupName, groupDescription) VALUES ( ?, ?)";
	private static final String SQL_DELETE_BY_GROUPNAME = "DELETE FROM web_app_db.Group WHERE groupName = ?";

	GroupDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Group group) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion,
					SQL_INSERT, true, group.getGroupName(), group.getGroupDescription());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException(
						"Failed to create group. No row added");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement,
					connexion);
		}	
	}

	@Override
	public Group find(String groupName) throws DAOException {
		return find(SQL_SELECT_BY_GROUPID, groupID);
	}

	@Override
	public List<Group> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Group> groups = new ArrayList<Group>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                groups.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return groups;
	}

	@Override
	public void delete(Group group) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_GROUPID, true, group.getGroupID() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete group, no row deleted." );
            } else {
            	//group.setIGroupID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
	}
	
	private Group find( String sql, Object... objets ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Group group = null;

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
                group = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return group;
    }
	
	
	private static Group map(ResultSet resultSet) throws SQLException {
		
		Group group = new Group();
		group.setGroupName(resultSet.getString("groupName"));
		group.setGroupDescription(resultSet.getString("groupDescription"));
		return group;
	}

}
