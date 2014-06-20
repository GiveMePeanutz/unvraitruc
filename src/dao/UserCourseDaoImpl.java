package dao;

public class UserCourseDaoImpl implements UserCourseDao {

	private DAOFactory daoFactory;

	UserCourseDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
}
