package dao;

import beans.KmeansDataType;

public interface KmeansDataDao {

	KmeansDataType[] getUserActivity() throws DAOException;
	
	KmeansDataType[] getUserActivityRatio() throws DAOException;
	
	KmeansDataType[] getGroupActivity() throws DAOException;
	
	KmeansDataType[] getGroupActivityRatio() throws DAOException;
	
	
}
