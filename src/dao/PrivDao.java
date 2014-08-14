package dao;

import java.util.List;

import beans.Priv;

public interface PrivDao {
	
	/*
	 * Creates a privilege in the database using as parameter a privilege bean
	 */
	void create( Priv priv ) throws DAOException;
	
	/*
	 * Returns a privlege bean containing all the information of the privilege whose privName is passed as parameter
	 */
	Priv find (String privName )throws DAOException;
	
	/*
	 * Returns all the privileges of the database in the form of a list of privilege beans
	 */
	List<Priv> list() throws DAOException;

	/*
	 * Deletes from the database the privilege whose privName is passed as parameter
	 */
	void delete( String privName ) throws DAOException;
	
	/*
	 * Modifies the privilege in the database with the corresponding privName extracted from the bean passed as argument
	 */
	void modify ( Priv priv ) throws DAOException;
	
}
