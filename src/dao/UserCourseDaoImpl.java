package dao;

import java.util.List;

import beans.Course;
import beans.User;

public class UserCourseDaoImpl implements UserCourseDao {

	private DAOFactory daoFactory;
	
	private static final String SQL_INSERT = "INSERT INTO user_course (username , courseID) VALUES (? , ?)";
	private static final String SQL_SELECT_USER_COURSES = "SELECT courseName, courseDescription FROM course WHERE courseID IN (SELECT courseID FROM user_course WHERE username = ?) ORDER BY courseName";
	private static final String SQL_SELECT_COURSE_USERS	 = "SELECT * FROM user WHERE username IN (SELECT username FROM user_course WHERE courseID = ?) ORDER BY username";
	private static final String SQL_DELETE_BY_USERNAME = "DELETE FROM user_course WHERE username = ?";
	private static final String SQL_DELETE_BY_COURSEID = "DELETE FROM user_course WHERE courseID = ?";
	private static final String SQL_DELETE =  "DELETE FROM user_course WHERE username = ? AND courseID = ?";

	UserCourseDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void create(String username, int courseID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int username, int courseID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByUsername(String username) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByCourseID(int courseID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Course> listUserCourses(String username) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> listCourseUsers(int courseID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
