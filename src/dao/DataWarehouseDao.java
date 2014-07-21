package dao;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataWarehouseDao {
	
	HashMap<String, ArrayList<String>> getUserFieldValues() throws DAOException;
	
	HashMap<String, ArrayList<String>> getTimeFieldValues() throws DAOException;

	HashMap<String, ArrayList<String>> getActivityFieldValues() throws DAOException;
	
	void updateLastDWModTime() throws DAOException;
	

}
