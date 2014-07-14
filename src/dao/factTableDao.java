package dao;

public interface FactTableDao {

	void addFact ( String username , String pageName , String dateID) throws DAOException;
	
}
