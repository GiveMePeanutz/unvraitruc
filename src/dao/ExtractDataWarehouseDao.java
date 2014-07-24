package dao;

import java.util.List;

import beans.DataWarehouseLine;

public interface ExtractDataWarehouseDao {

    public List<String> getDays();

    public List<String> getMonths();

    public List<String> listYear() throws DAOException;

    public List<String> listGroup() throws DAOException;

    public String countMonth( DataWarehouseLine dWL ) throws DAOException;

    public String countWeek( DataWarehouseLine dWL ) throws DAOException;

    public String countAllBySex( int sex, int action, int year ) throws DAOException;

    public String countAllByGroup( String group, int action, int year ) throws DAOException;

}
