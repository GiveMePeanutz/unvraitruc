package dao;

import java.util.List;

import beans.Group;
import beans.User;

public interface UserGroupDao {

	void create( int groupID, String username ) throws DAOException;
	
	void delete (int groupID, String username ) throws DAOException;
	
	void deleteBygroupID (int groupID) throws DAOException;
	
	void deleteByPrivID (String username ) throws DAOException;
	
	List<User> listGroupUsers( int groupID ) throws DAOException;
	
	List<Group> listUserGroups( String username ) throws DAOException;
	
}
