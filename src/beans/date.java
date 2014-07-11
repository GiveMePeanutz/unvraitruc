package beans;

import org.joda.time.DateTime;

public class Date {
    private int    dateID;
    private int    minute;
    private int    hour;
    private int    day;
    private String dayName;
    private int    week;
    private String monthName;
    private int    quarter;
    private int    year;

    public Date( DateTime dateTime )
    {
        this.minute = dateTime.getMinuteOfHour();
        this.hour = dateTime.getHourOfDay();
        this.day = dateTime.getDayOfMonth();
        this.dayName = dateTime.getDayOfWeek();
        this.week=dateTime.getWeekOfWeekyear();
        this.monthName=dateTime.getMonthOfYear();
        this.quarter=;
        this.year=dateTime.getYear();

    }

    public int getDateID() {
        return dateID;
    }

    public void setDateID( int dateID ) {
        this.dateID = dateID;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute( int minute ) {
        this.minute = minute;
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

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter( int quarter ) {
        this.quarter = quarter;
    }

    public int getYear() {
        return year;
    }

    public void setYear( int year ) {
        this.year = year;
    }

}
