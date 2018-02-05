package ca.ualberta.cs.chengze2_subbook;

/**
 * Created by chengze on 2018/2/4.
 *
 * Build a object for each Year-Month-Day
 * @author Chengze Li
 * @version 1.0
 * @see Subscription
 *
 */

public class MyDate {
    public int year;
    public int month;
    public int day;

    /**
     * Constructs a MyDate object for a subscription
     * @param year selected year
     * @param month selected month
     * @param day selected day
     */
    MyDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
}
