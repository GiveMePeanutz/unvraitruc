package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTableDaoImpl implements TransactionTableDao {

	private DAOFactory          daoFactory;
	
	private static final String SQL_INSERT = "INSERT INTO fact_table(username,pageName,factDate) VALUES (?,?,NOW())";
	
	TransactionTableDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

	/*
	 * Adds a line in the transaction table taking as parameter the username of the user that performed the action
	 * and the action/page name.
	 */
	@Override
	public void addTransaction(String username, String pageName)
			throws DAOException {
		
		Connection connexion = null;
        PreparedStatement preparedStatement1 = null;

        try {
            /* Retrieves a connection from the factory */
        	connexion = daoFactory.getConnection();
            
        	// Prepared statement of the query in charge of inserting a new transaction :
        	// the pageName ( accessed page or action ) and the username
        	// The date is inserted within the query with "NOW()"
        	preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, username, pageName );
            int statut1 = preparedStatement1.executeUpdate();
            
            // If the insertion failed
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create Transaction. No row added" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connexion );

        }
		
	}

	

}
