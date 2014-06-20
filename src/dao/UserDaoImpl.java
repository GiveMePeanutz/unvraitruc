package dao;

import java.util.List;

import beans.User;

public class UserDaoImpl implements UserDao {

	private DAOFactory daoFactory;

	UserDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(User user) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public User find(long userID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> list() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(User user) throws DAOException {
		// TODO Auto-generated method stub

	}

}
