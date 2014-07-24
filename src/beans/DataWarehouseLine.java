package beans;

import java.io.Serializable;

public class DataWarehouseLine implements Serializable {

    private int    sex;
    private String group;
    private int    year;
    private String month;
    private int    week;
    private int    day;
    private String dayOfWeek;
    private int    hour;
    private int    activity;
    private int    count;

    public DataWarehouseLine() {
        super();
    }

    public DataWarehouseLine( int sex, String group, int year, String month, int day, int hour, int activity ) {
        super();
        this.sex = sex;
        this.group = group;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.activity = activity;
    }

    public DataWarehouseLine( int sex, String group, int year, int week,
            String dayOfWeek, int activity ) {
        super();
        this.sex = sex;
        this.group = group;
        this.year = year;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.activity = activity;
    }

    public int getSex() {
        return sex;
    }

    public String getGroup() {
        return group;
    }

    public int getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public int getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public int getActivity() {
        return activity;
    }

    public int getCount() {
        return count;
    }

    public void setSex( int sex ) {
        this.sex = sex;
    }

    public void setGroup( String group ) {
        this.group = group;
    }

    public void setYear( int year ) {
        this.year = year;
    }

    public void setMonth( String month ) {
        this.month = month;
    }

    public void setWeek( int week ) {
        this.week = week;
    }

    public void setDay( int day ) {
        this.day = day;
    }

    public void setDayOfWeek( String dayOfWeek ) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setHour( int hour ) {
        this.hour = hour;
    }

    public void setActivity( int activity ) {
        this.activity = activity;
    }

    public void setCount( int count ) {
        this.count = count;
    }

}
