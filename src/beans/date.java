package beans;

import org.joda.time.DateTime;

public class Date {

    // Bean properties
    private int    dateID;
    private int    hour;
    private int    day;
    private String dayName;
    private int    week;
    private String monthName;
    private int    year;

    // Retrieves each part of a DateTime (hours, weeks ...) and saves the real
    // name of days and months according to their number.
    public Date()
    {

        DateTime dateTime = new DateTime();

        this.dateID = -1;

        this.hour = dateTime.getHourOfDay();
        this.day = dateTime.getDayOfMonth();
        this.week = dateTime.getWeekOfWeekyear();
        this.year = dateTime.getYear();

        switch ( dateTime.getMonthOfYear() ) {
        case 1:
            this.monthName = "January";
            break;
        case 2:
            this.monthName = "February";
            break;
        case 3:
            this.monthName = "March";
            break;
        case 4:
            this.monthName = "April";
            break;
        case 5:
            this.monthName = "May";
            break;
        case 6:
            this.monthName = "June";
            break;
        case 7:
            this.monthName = "July";
            break;
        case 8:
            this.monthName = "August";
            break;
        case 9:
            this.monthName = "September";
            break;
        case 10:
            this.monthName = "October";
            break;
        case 11:
            this.monthName = "November";
            break;
        case 12:
            this.monthName = "December";
            break;
        default:
            this.monthName = "Invalid month";
            break;
        }

        switch ( dateTime.getDayOfWeek() ) {
        case 1:
            this.dayName = "Monday";
            break;
        case 2:
            this.dayName = "Tuesday";
            break;
        case 3:
            this.dayName = "Wednesday";
            break;
        case 4:
            this.dayName = "Thursday";
            break;
        case 5:
            this.dayName = "Friday";
            break;
        case 6:
            this.dayName = "Saturday";
            break;
        case 7:
            this.dayName = "Sunday";
            break;

        default:
            this.dayName = "Invalid day";
            break;
        }

    }

    /*----------------------------------------GETTERS AND SETTERS ---------------------------------------------------------*/

    public int getDateID() {
        return dateID;
    }

    public void setDateID( int dateID ) {
        this.dateID = dateID;
    }

    public int getHour() {
        return hour;
    }

    public void setHour( int hour ) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay( int day ) {
        this.day = day;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName( String dayName ) {
        this.dayName = dayName;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek( int week ) {
        this.week = week;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName( String monthName ) {
        this.monthName = monthName;
    }

    public int getYear() {
        return year;
    }

    public void setYear( int year ) {
        this.year = year;
    }

}
