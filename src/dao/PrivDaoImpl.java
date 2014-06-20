package dao;

import java.util.List;

import beans.Priv;

public class PrivDaoImpl implements PrivDao {

	private DAOFactory daoFactory;

	PrivDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void create(Priv priv) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Priv find(long privID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Priv> list() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Priv priv) throws DAOException {
		// TODO Auto-generated method stub

	}

}
