package dao;

import java.util.List;

import beans.Course;
import beans.User;

public interface UserCourseDao {

	void create( String username, int courseID ) throws DAOException;
	
	void delete (int username, int courseID ) throws DAOException;
	
	void deleteByUsername (String username) throws DAOException;
	
	void deleteByCourseID (int courseID ) throws DAOException;
	
	List<Course> listUserCourses( String username ) throws DAOException;
	
	List<User> listCourseUsers( int courseID ) throws DAOException;
	
}
