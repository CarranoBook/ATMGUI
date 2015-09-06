/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

import java.util.Date;

/**
 *
 * @author NB <nb@fs.org>
 */
public class TransactionTuple {
    private final long date;
    private final double balance;
    
    TransactionTuple(double balance) {
        Date d = new Date();
        date = d.getTime();
        this.balance = balance;
    }
    
    TransactionTuple(double balance, long date) {
        this.balance = balance;
        this.date = date;
    }
    
    TransactionTuple() {
        this(100);
    }
    
    public Date getDate() {
        return new Date(date);
    }
    
    public double getBalance() {
        return balance;
    }
    
    public boolean equals(TransactionTuple a) {
        return ( date == a.date && balance == a.balance );
    }
}
