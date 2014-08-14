package dao;

public interface TransactionTableDao {
	
	/*
	 * Adds a line in the transaction table taking as parameter the username of the user that performed the action
	 * and the action/page name.
	 */
	void addTransaction ( String username , String pageName ) throws DAOException;
	
}
