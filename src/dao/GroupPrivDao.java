package dao;

import java.util.List;

import beans.Group;
import beans.Priv;

public interface GroupPrivDao {

	void create( int groupID, int privID ) throws DAOException;
	
	void delete (int groupID, int privID ) throws DAOException;
	
	List<Priv> listGroupPrivs( int groupID ) throws DAOException;
	
	List<Group> listPrivGroups( int privID ) throws DAOException;
	
	
	
}
