package dao;

import org.joda.time.DateTime;

public interface DateDao {

	void createDate(DateTime dateTime) throws DAOException;
	
}
