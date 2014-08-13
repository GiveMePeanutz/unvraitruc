package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import beans.DataWarehouseLine;

//Interface of dataWarehouseDao
public interface DataWarehouseDao {

	/*
	 *  Method that updates the dataWarehouse
	 *  Uses updateDWDim() and updateDWFactTable()
	 */
    void updateDW() throws DAOException;
    
    /*
     *  Method that updates the dataWarehouse dimensions
     *  Should not be called separately from updateDWFactTable()
     *  Used for testing
     */
	void updateDWDim() throws DAOException;
	
	/*
	 *  Method that updates the dataWarehouse fact table
	 *  Should not be called separately from updateDWDim()
	 *  Used for testing
	 */
	void updateDWFactTable() throws DAOException;

}
