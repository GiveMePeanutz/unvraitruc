package dao;

import java.util.List;

import beans.Priv;

public interface PrivDao {
	
	void create( Priv priv ) throws DAOException;
	
	Priv find (String privName )throws DAOException;
	
	List<Priv> list() throws DAOException;

	void delete( Priv priv ) throws DAOException;
	
}
