package dao;

import org.joda.time.DateTime;

import beans.Date;

public interface DateDao {

	Date createDate(DateTime dateTime) throws DAOException;
	
}
