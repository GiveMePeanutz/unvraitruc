package dao;

import java.util.List;

import beans.User;

public interface UserDao {

	void create( User user ) throws DAOException;
	
	User find (String username )throws DAOException;
	
	List<User> list() throws DAOException;

	void delete( String username ) throws DAOException;
	
	String getPassword( String username) throws DAOException;
	
	List<String> listAccMenus( String username ) throws DAOException;
}
