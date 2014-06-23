package dao;

import java.util.List;

import beans.Group;
import beans.Priv;

public class GroupPrivDaoImpl implements GroupPrivDao {

	private DAOFactory daoFactory;

	GroupPrivDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private static final String SQL_INSERT = "INSERT INTO Group_Priv (groupID , privID) VALUES (? , ?)";
	private static final String SQL_SELECT_GROUP_PRIVS = "SELECT privName, privDescription FROM Priv WHERE privID IN (SELECT privID FROM Group_Priv WHERE groupID = ?) ORDER BY privName";
	private static final String SQL_SELECT_PRIV_GROUPS = "SELECT groupName, groupDescription FROM web_app_db.Group WHERE groupID IN (SELECT groupID FROM Group_Priv WHERE privID = ?) ORDER BY groupName";
	private static final String SQL_DELETE_BY_GROUPID = "DELETE FROM Group_Priv WHERE groupID = ?";
	private static final String SQL_DELETE_BY_PRIVID = "DELETE FROM Group_Priv WHERE privID = ?";
	private static final String SQL_DELETE =  "DELETE FROM Group_Priv WHERE groupID = ? AND privID = ?";
	
	
	@Override
	public void create(int groupID, int privID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int groupID, int privID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Priv> listGroupPrivs(int groupID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> listPrivGroups(int privID) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByGroupID(int groupID) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByPrivID(int privID) throws DAOException {
		// TODO Auto-generated method stub
		
	}
	
}
