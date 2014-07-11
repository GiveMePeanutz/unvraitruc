package dao;

import static dao.DAOUtility.fermeturesSilencieuses;
import static dao.DAOUtility.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import beans.Date;

public class DateDaoImpl implements DateDao {

    private DAOFactory          daoFactory;

    private static final String SQL_INSERT = "INSERT INTO Date (dateId, minute, hour, day, dayName, week, monthName, quarter, year) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    DateDaoImpl( DAOFactory daoFactory ) {
        this.daoFactory = daoFactory;
    }

    public Date create( Date date ) throws DAOException
    {
        Connection connexion = null;
        PreparedStatement preparedStatement1 = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement1 = initialisationRequetePreparee( connexion,
                    SQL_INSERT, true, date.getDateID(), date.getMinute(), date.getHour(), date.getDay(),
                    date.getDayName(), date.getWeek(), date.getMonthName(), date.getQuarter(),
                    date.getYear() );
            int statut1 = preparedStatement1.executeUpdate();
            if ( statut1 == 0 ) {
                throw new DAOException(
                        "Failed to create date. No row added" );
            }

        } catch ( SQLException e ) {
            throw new DAOException( e );
        } finally {
            fermeturesSilencieuses( preparedStatement1,
                    connexion );

        }

        return date;

    }

}
