package dao;

import java.util.List;

import beans.Group;

public class GroupDaoImpl implements GroupDao {

	private DAOFactory daoFactory;

	GroupDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Group group) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Group find(long groupID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> list() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Group group) throws DAOException {
		// TODO Auto-generated method stub

	}

}
