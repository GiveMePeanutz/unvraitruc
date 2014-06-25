package dao;

import java.util.ArrayList;
import java.util.List;

import beans.Course;

public interface CourseDao {

	void create( Course course ) throws DAOException;
	
	Course find (String courseName )throws DAOException;
	
	List<Course> list() throws DAOException;

	void delete( Course course ) throws DAOException;
	
}
