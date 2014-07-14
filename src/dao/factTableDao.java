package dao;

public interface FactTableDao {

	void addFact ( String username , String pageName , int dateID) throws DAOException;
	
}
