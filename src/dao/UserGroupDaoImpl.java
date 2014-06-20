package dao;

public class UserGroupDaoImpl implements UserGroupDao {
	
	private DAOFactory daoFactory;

	UserGroupDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
}
