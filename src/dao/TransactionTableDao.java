package dao;

public interface TransactionTableDao {

	void addTransaction ( String username , String pageName ) throws DAOException;
	
}
