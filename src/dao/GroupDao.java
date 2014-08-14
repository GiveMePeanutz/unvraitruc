package dao;

import java.util.List;

import beans.Group;

public interface GroupDao {
	
	/*
	 *  Creates a group in the database using as parameter a group bean
	 */
	void create( Group group ) throws DAOException;
	
	/*
	 *  Returns a group bean containing all the informatino of the group whose name is passed as parameter
	 */
	Group find (String groupName )throws DAOException;
	
	/*
	 * Returns all the groups of the database in the form of a list of group beans
	 */
	List<Group> list() throws DAOException;

	/*
	 * Deletes from the database the group whose groupName is passed as parameter
	 */
	void delete( String groupName ) throws DAOException;
	
	/*
	 * Modifies the group in the database with the corresponding groupName extracted from the bean passed as argument
	 */
	void modify (Group group) throws DAOException;
	
}
