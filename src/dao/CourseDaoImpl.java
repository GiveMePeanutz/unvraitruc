package dao;

import java.util.List;

import beans.Course;

public class CourseDaoImpl implements CourseDao {

	private DAOFactory daoFactory;

	CourseDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Course course) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Course find(long courseID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Course> list() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Course course) throws DAOException {
		// TODO Auto-generated method stub

	}

}
