package dao;

import java.util.ArrayList;
import java.util.List;

import beans.Course;


//Interface of courseDao
public interface CourseDao {
	
	/*
	 * Creates a course in the database using as parameter a course bean and a teacher name 
	 */
	void create( Course course , String teacherName ) throws DAOException;
	
	/*
	 * Returns a course bean containing all the information of the course whose courseName is passed as parameter
	 */
	Course find (String courseName )throws DAOException;
	
	/*
	 * Returns all the courses of the database in the form of a list of course beans
	 */
	List<Course> list() throws DAOException;
	
	/*
	 * Deletes from the database the course whose courseName is passed as parameter
	 */
	void delete( String courseName ) throws DAOException;
	
	/*
	 * Modifies the course in the database with the corresponding courseName extracted from the bean passed as argument
	 */
	void modify ( Course course ) throws DAOException;
	
}
