package dao;

import beans.KmeansDataType;

public interface KmeansDataDao {

	KmeansDataType[] getUserActivity() throws DAOException;
	
	KmeansDataType[] getGroupActivity() throws DAOException;
	
	int countActiveUsers() throws DAOException;
	
	int countActiveGroups() throws DAOException;
}
