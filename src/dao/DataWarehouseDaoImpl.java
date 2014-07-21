package dao;

import java.util.ArrayList;
import java.util.HashMap;

public class DataWarehouseDaoImpl implements DataWarehouseDao {

	 private DAOFactory          daoFactory;
	 
	 private static final String UPDATE_LAST_MOD_TIME  = "INSERT INTO web_app_db.UpdateTime (updateTime) values (now())";
	
	 

    DataWarehouseDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }
	
	@Override
	public HashMap<String, ArrayList<String>> getUserFieldValues()
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, ArrayList<String>> getTimeFieldValues()
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, ArrayList<String>> getActivityFieldValues()
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLastDWModTime() throws DAOException {
		// TODO Auto-generated method stub
		
	}

}
