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

    private DAOFactory          daoFactory;

    private static final String SQL_SELECT              = "SELECT  groupName, groupDescription FROM web_app_db.Group ORDER BY groupName";
    private static final String SQL_SELECT_BY_GROUPNAME = "SELECT groupName, groupDescription FROM web_app_db.Group WHERE groupName = ?";
    private static final String SQL_SELECT_GROUP_PRIVS  = "SELECT privName FROM Priv WHERE privName IN (SELECT privName FROM Group_Priv WHERE groupName = ?) ORDER BY privName";
    private static final String SQL_SELECT_GROUP_USERS  = "SELECT * FROM user WHERE username IN (SELECT username FROM user_group WHERE groupName = ?) ORDER BY username";

    private static final String SQL_INSERT              = "INSERT INTO web_app_db.Group ( groupName, groupDescription) VALUES ( ?, ?)";
    private static final String SQL_INSERT_GROUP_PRIV   = "INSERT INTO Group_Priv (groupName , privName) VALUES (? , ?)";

    private static final String SQL_DELETE_BY_GROUPNAME = "DELETE FROM web_app_db.Group WHERE groupName = ?";

    GroupDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void create( Group group ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, group.getGroupName(), group.getGroupDescription() );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create group. No row added" );
            }

            for ( String privName : group.getPrivNames() ) {
                preparedStatement2 = initialisationRequetePreparee( connexion,
                        SQL_INSERT_GROUP_PRIV, true, group.getGroupName(), privName );
                int statut2 = preparedStatement2.executeUpdate();
                if ( statut2 == 0 ) {
                    throw new DAOException(
                            "Failed to create group-privilege association. No row added" );
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
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, groupName );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, groupName );
            resultSet2 = preparedStatement2.executeQuery();
            preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, groupName );
            resultSet3 = preparedStatement3.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
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

        return group;
    }

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
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();

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

        return groups;
    }

    @Override
    public void delete( Group group ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_GROUPNAME, true,
                    group.getGroupName() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete group, no row deleted." );
            } else {
                // group.setIGroupID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }
    }

    private static Group map( ResultSet resultSet, ResultSet resultSet2, ResultSet resultSet3 ) throws SQLException {

        Group group = new Group();
        group.setGroupName( resultSet.getString( "groupName" ) );
        group.setGroupDescription( resultSet.getString( "groupDescription" ) );
        
        group.setPrivNames(new ArrayList<String>());
        group.setUsernames(new ArrayList<String>());

        resultSet2.beforeFirst();
        while ( resultSet2.next() ) {
            group.addPrivName( resultSet2.getString( "privName" ) );
        }

        resultSet3.beforeFirst();
        while ( resultSet2.next() ) {
            group.addUsername( resultSet3.getString( "username" ) );
        }

        return group;
    }

}
