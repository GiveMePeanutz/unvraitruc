package dao;

import java.util.List;

import beans.Group;

public interface GroupDao {

	void create( Group group ) throws DAOException;
	
	Group find (String groupName )throws DAOException;
	
	List<Group> list() throws DAOException;

	void delete( Group group ) throws DAOException;
	
}
