package ca.ualberta.cs.chengze2_subbook;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by chengze on 2018/2/4.
 *
 * Represents a subscription
 *
 * @author Chengze Li
 * @version 1.0
 * @see MyDate
 */

public class Subscription implements InterfaceSubscription {
    private String name;
    private MyDate date;
    private Double charge;
    private String comment;

    /**
     * Constructs a subscription object
     * @param name subscription name(0-20 characters)
     * @param date subscription date
     * @param charge subscription charge(0-30 characters)
     * @param comment subscription comment
     */
    Subscription(String name, MyDate date, Double charge, String comment){
        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }

    public String getName(){
        return name;
    }
    public MyDate getDate(){
        return date;
    }
    public Double getCharge(){
        return charge;
    }
    public String getComment(){
        return comment;
    }

    public String toString() {
        DecimalFormat twoDig = new DecimalFormat("#.00");
        if (comment == null){
            return name + ": "+ date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " | $ " + twoDig.format(charge);
        }
        else{
            return name + ": "+ date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " | $ " + twoDig.format(charge) + " | " + comment;
        }
    }
}
