package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FactTableDaoImpl implements FactTableDao {

	private DAOFactory          daoFactory;
	
	private static final String SQL_INSERT = "INSERT INTO fact_table(username,pageName,dateID) VALUES (?,?,?)";
	
	FactTableDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
	
	@Override
	public void addFact(String username, String pageName, String dateID)
			throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement1 = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, username, pageName, dateID );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create fact. No row added" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connexion );

        }
		
	}

	

}
