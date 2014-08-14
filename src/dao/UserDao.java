package dao;

import java.util.List;

import beans.User;

public interface UserDao {

	/*
	 * Creates a user in the database using as parameter a user bean 
	 */
    void create( User user ) throws DAOException;

    /*
	 * Returns a course bean containing all the information of the course whose courseName is passed as parameter
     */
    User find( String username ) throws DAOException;

    /*
	 * Returns all the users of the database in the form of a list of user beans
     */
    List<User> list() throws DAOException;

    /*
	 * Returns all the students of the database in the form of a list of user beans
     */
    List<User> listStudent() throws DAOException;

    /*
	 * Returns all the teachers of the database in the form of a list of user beans
     */
    List<User> listGroup( String group ) throws DAOException;

    /*
	 * Deletes from the database the user whose username is passed as parameter
     */
    void delete( String username ) throws DAOException;

    /*
     *  Retrieves from the database the password of the user whose username is passed as parameter
     */
    String getPassword( String username ) throws DAOException;

    /*
     *  Retrieves as a List of Strings the names of the menues the user whose username is passed as 
     *  parameter is authorized to access
     */
    List<String> listAccMenus( String username ) throws DAOException;

    /*
	 * Modifies the user in the database with the corresponding userName extracted from the bean passed as argument
     */
    void modify( User user ) throws DAOException;

    /*
     * Add the course whose courseName is passed as parameter to the user whose username is
     * also passed as parameter
     */
    void addCourse( String username, String courseName ) throws DAOException;

    /*
     * Deletes from the database the user/course association between the course and user whose username and courseNames 
     * are passed as parameters
     */
    void deleteCourse( String username, String courseName ) throws DAOException;

}
