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
import beans.Priv;

public class PrivDaoImpl implements PrivDao {

    private DAOFactory          daoFactory;

    private static final String SQL_SELECT           = "SELECT privName, privDescription FROM Priv ORDER BY privName";
    private static final String SQL_SELECT_BY_PRIVNAME = "SELECT privName, privDescription FROM Priv WHERE privName = ?";
    private static final String SQL_SELECT_PRIV_MENUPATHS = "SELECT menuPath FROM priv_menu WHERE privName = ? ORDER BY menuPath";
    private static final String SQL_SELECT_PRIV_GROUPS = "SELECT groupName FROM web_app_db.group WHERE groupName IN (SELECT groupName FROM user_group WHERE username = ?) ORDER BY groupName";

    private static final String SQL_INSERT           = "INSERT INTO Priv ( privName, privDescription) VALUES ( ?, ?)";
    private static final String SQL_INSERT_PRIV_MENU = "INSERT INTO priv_menu (menuPath , privName) VALUES (? , ?)";
    private static final String SQL_INSERT_PRIV_GROUP= "INSERT INTO Group_Priv (groupName , privName) VALUES (? , ?)";

    
    private static final String SQL_DELETE_BY_PRIVNAME = "DELETE FROM Priv WHERE privName = ?";

    PrivDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void create( Priv priv ) throws DAOException {
    	Connection connexion = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement1 = initialisationRequetePreparee(connexion,
					SQL_INSERT, true, priv.getPrivName(), priv.getPrivDescription());
			int statut1 = preparedStatement1.executeUpdate();
			if (statut1 == 0) {
				throw new DAOException(
						"Failed to create privilege. No row added");
			}
			
			
			
			
			for (int menuPath : priv.getMenuPaths()) {
				preparedStatement2 = initialisationRequetePreparee( connexion,
	                    SQL_INSERT_PRIV_MENU, true, priv.getPrivName(), menuPath );
	            int statut2 = preparedStatement2.executeUpdate();
	            if ( statut2 == 0 ) {
	                throw new DAOException(
	                        "Failed to create menuPath-privilege association. No row added" );
	            }
			}
			
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(preparedStatement1,
					connexion);
			fermeturesSilencieuses(preparedStatement2,
					connexion);
		}
    }

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
        String sql2= SQL_SELECT_PRIV_MENUPATHS;
        String sql3= SQL_SELECT_PRIV_GROUPS;

        try {
            /* Récupération d'une connection depuis la Factory */
            connection = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             * (ici, uniquement un id) et exécution.
             */
            preparedStatement = initialisationRequetePreparee( connection, sql, false, privName );
            resultSet = preparedStatement.executeQuery();
            preparedStatement2 = initialisationRequetePreparee( connection, sql2, false, privName );
            resultSet2 = preparedStatement2.executeQuery();
            preparedStatement3 = initialisationRequetePreparee( connection, sql3, false, privName );
            resultSet3 = preparedStatement3.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
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

        return priv;
    }

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
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            
            while ( resultSet.next() ) {
            	preparedStatement2 = initialisationRequetePreparee(connection, SQL_SELECT_PRIV_MENUPATHS, false, resultSet.getString("privName") );
                resultSet2 = preparedStatement2.executeQuery();
                preparedStatement3 = initialisationRequetePreparee(connection, SQL_SELECT_PRIV_GROUPS, false, resultSet.getString("privName") );
                resultSet3 = preparedStatement3.executeQuery();
                privs.add( map( resultSet , resultSet2, resultSet3) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
            fermeturesSilencieuses( resultSet2, preparedStatement2, connection );
            fermeturesSilencieuses( resultSet3, preparedStatement3, connection );
        }

        return privs;
    }

    @Override
    public void delete( String privName ) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_PRIVNAME, true, privName );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete privilege, no row deleted." );
            } else {
                // priv.setIPrivID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

    }



    private static Priv map( ResultSet resultSet,ResultSet resultSet2,ResultSet resultSet3 ) throws SQLException {

        Priv priv = new Priv();
        priv.setPrivName( resultSet.getString( "privName" ) );
        priv.setPrivDescription( resultSet.getString( "privDescription" ) );
        
        ArrayList<Integer> menuPaths = new ArrayList<Integer>();
        resultSet2.beforeFirst();
		while(resultSet2.next()) {
			menuPaths.add(resultSet2.getInt("menuPath"));
		}
		priv.setMenuPaths(menuPaths);
		
		ArrayList<String> groupNames = new ArrayList<String>();
		resultSet3.beforeFirst();
		while(resultSet2.next()) {
			groupNames.add(resultSet3.getString("groupName"));
		}
		priv.setGroupNames(groupNames);
        
        return priv;
    }

}
