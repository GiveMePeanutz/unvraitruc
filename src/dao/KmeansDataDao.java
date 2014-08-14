package dao;

import beans.KmeansDataType;

public interface KmeansDataDao {
	
	/*
	 * Retrieves from the database user activity in the form of an array of KmeansDataType
	 */
	KmeansDataType[] getUserActivity() throws DAOException;
	
	/*
	 * Retrieves from the database group activity in the form of an array of KmeansDataType
	 */
	KmeansDataType[] getGroupActivity() throws DAOException;
	
	/*
	 *  Returns the number of active users
	 */
	int countActiveUsers() throws DAOException;
	
	/*
	 *  Returns the number of active groups
	 */
	int countActiveGroups() throws DAOException;
}
