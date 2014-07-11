package dao;

import beans.Date;

public interface DateDao {

    Date create( Date date ) throws DAOException;

}
