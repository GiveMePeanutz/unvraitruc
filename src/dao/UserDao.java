package dao;

import java.util.List;

import beans.User;

public interface UserDao {

	void create( User user ) throws DAOException;
	
	User find (long userID )throws DAOException;
	
	List<User> list() throws DAOException;

	void delete( User user ) throws DAOException;
	
	
}
