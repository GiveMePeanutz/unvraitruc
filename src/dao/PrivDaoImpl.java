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
import beans.Priv;
import beans.Priv;

public class PrivDaoImpl implements PrivDao {

	private DAOFactory daoFactory;
	
	private static final String SQL_SELECT = "SELECT privID, privName, privDescription FROM Priv ORDER BY privName";
	private static final String SQL_SELECT_BY_PRIVID = "SELECT privID, privName, privDescription FROM Priv WHERE privID = ?";
	private static final String SQL_INSERT = "INSERT INTO Priv (privID, privName, privDescription) VALUES (?, ?, ?)";
	private static final String SQL_DELETE_BY_PRIVID = "DELETE FROM Priv WHERE privID = ?";

	PrivDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Priv priv) throws DAOException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;
		ResultSet autoGeneratedValues = null;

		try {
			connexion = daoFactory.getConnection();
			preparedStatement = initialisationRequetePreparee(connexion,
					SQL_INSERT, true, priv.getPrivID(), priv.getPrivName(), priv.getPrivDescription());
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new DAOException(
						"Failed to create privilege. No row added");
			}
			autoGeneratedValues = preparedStatement.getGeneratedKeys();
			if (autoGeneratedValues.next()) {
				priv.setPrivID(autoGeneratedValues.getInt(1));
			} else {
				throw new DAOException(
						"Failed to create privilege in DB, no auto-generated privID returned.");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		} finally {
			fermeturesSilencieuses(autoGeneratedValues, preparedStatement,
					connexion);
		}
	}

	@Override
	public Priv find(long privID) throws DAOException {
		return find(SQL_SELECT_BY_PRIVID, privID);
	}

	@Override
	public List<Priv> list() throws DAOException {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Priv> privs = new ArrayList<Priv>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement( SQL_SELECT );
            resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                privs.add( map( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return privs;
	}

	@Override
	public void delete(Priv priv) throws DAOException {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_DELETE_BY_PRIVID, true, priv.getPrivID() );
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Failed to delete privilege, no row deleted." );
            } else {
            	//priv.setIPrivID( null );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement, connexion );
        }

	}
	
	private Priv find( String sql, Object... objets ) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Priv priv = null;

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
                priv = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connection );
        }

        return priv;
    }
	
	
	private static Priv map(ResultSet resultSet) throws SQLException {
		
		Priv priv = new Priv();
		priv.setPrivID(resultSet.getInt("privID"));
		priv.setPrivName(resultSet.getString("privName"));
		priv.setPrivDescrition(resultSet.getString("privDescription"));
		return priv;
	}

}