package dao;

import java.util.List;


import beans.Group;
import beans.Priv;

public interface PrivMenuDao {
	

	void create( int menuPath, int privID ) throws DAOException;
	
	void delete (int menuPath, int privID ) throws DAOException;
	
	void deleteByMenuPath (int menuPath) throws DAOException;
	
	void deleteByPrivID (int privID ) throws DAOException;
	
	List<Priv> listMenuPathPrivs( int menuPath ) throws DAOException;
	
	List<Integer> listPrivMenuPaths( int privID ) throws DAOException;
	
}
