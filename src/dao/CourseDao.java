package dao;

import java.util.ArrayList;
import java.util.List;

import beans.Course;

public interface CourseDao {

	void create( Course course , String teacherName ) throws DAOException;
	
	Course find (String courseName )throws DAOException;
	
	List<Course> list() throws DAOException;

	void delete( String courseName ) throws DAOException;
	
	void modify ( Course course ) throws DAOException;
	
}
