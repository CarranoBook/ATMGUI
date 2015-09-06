/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author NB <nb@fs.org>
 */
public class BankAccount {
    private ArrayList<TransactionTuple> transactions;
    private final long acctNum;
    private int pin;
    private double balance;
    private ArrayList<PersonName> names = new ArrayList<>();
    
    BankAccount(long acctNum, int pin, double iBalance, PersonName name) {
        this.transactions = new ArrayList<>();
        this.acctNum = acctNum;
        this.pin = pin;
        balance = iBalance;
        names.add(name);
        transactions.add(new TransactionTuple(0));
        transactions.add(new TransactionTuple(iBalance));
    }
    
    BankAccount(long acctNum, int pin, double iBalance, 
            ArrayList<PersonName> names, 
            ArrayList<TransactionTuple> transactions) {
        this.transactions = new ArrayList<>();
        this.acctNum = acctNum;
        this.pin = pin;
        balance = iBalance;
        this.names = names;
        this.transactions = transactions;
    }
    
    BankAccount(long acctNum, int pin, double iBalance, ArrayList<PersonName> names) {
        this.transactions = new ArrayList<>();
        this.acctNum = acctNum;
        this.pin = pin;
        balance = iBalance;
        this.names = (ArrayList<PersonName>) names.clone();
        transactions.add(new TransactionTuple(0));
        transactions.add(new TransactionTuple(iBalance));
    }
    
    public double getBalance() {
        return balance;
    }
    
    public ArrayList<PersonName> getNames() {
        return names;
    }
    
    /**
     * Gets the balance of the account as a string in the format $1*.11
     * eg: balance of 2542.231 returns $2432.23
     * @return string of the balance formatted as cash would be
     */
    public String getBalanceString() {
        return doubleToCash(balance);
    }
    
    private String getBalanceXML() {
        return String.format("%.2f", balance);
    }
    
    private void setBalance(double newBalance) {
        balance = newBalance;
    }
    
    
    /**
     * This method deposits moneyn into the account, increasing the balance of the account.
     * A transaction record will also be posted to the transactions list.
     * The method throws an IllegalArgumentException if the amount of money deposited is
     * less than or equal to zero
     * @param amount amount of money deposited
     * @return transaction receipt of the deposit
     */
    public TransactionTuple deposit(double amount) {
        if ( amount <= 0 ) {
            String s = "Cannot deposit non-positive money";
            throw new IllegalArgumentException(s);
        }
        this.setBalance(balance + amount);
        TransactionTuple temp = new TransactionTuple(balance);
        transactions.add(temp);
        return temp;
    }
    
    public long getAccountNumber() {
        return acctNum;
    }
    
    private String getAccountNumberString() {
        Long temp = acctNum;
        return temp.toString();
    }
    
    public int getPin() {
        return pin;
    }
    
    private String getPinString() {
        Integer temp = pin;
        return temp.toString();
    }
    
    public void setPin(int pin) {
        this.pin = pin;
    }
    
    
    /**
     * Withdraws money from the account and lowers the balance of the account.
     * @param amount amount of money to withdraw
     * @return transaction receipt of withdraw
     * @throws IllegalArgumentException Throws IllegalArgumentException if 
     * withdraw amount <= 0 or if withdraw amount > current balance
     */
    public TransactionTuple withdraw(double amount) throws IllegalArgumentException {
        if ( amount <= 0 ) {
            String s = "Cannot withdrawal non-positive money";
            throw new IllegalArgumentException(s);
        }
        
        
        if ( amount > balance ) {
            String s = "Withdraw amount greater than account balance";
            IllegalArgumentException e = new IllegalArgumentException(s);
            throw e;
        }
        else {
            setBalance( balance - amount);
            TransactionTuple temp = new TransactionTuple(balance);
            transactions.add(temp);
            return temp;
        }
    }
    
    public Date getTransactionDate(int index) {
        if ( index < 0 || index >= transactions.size() ) {
            String s = "Array index out of bounds.  There are not that many transactions on this account";
            throw new IllegalArgumentException(s);
        } 
        return transactions.get(index).getDate();
    }
    
    public double getTransactionDouble(int index) {
        if ( index < 1 || index >= transactions.size() ) {
            String s = "Array index out of bounds.  There are not that many transactions on this account";
            throw new IllegalArgumentException(s);
        }
        double startBalance = transactions.get(index-1).getBalance();
        double endBalance = transactions.get(index).getBalance();
        
        return endBalance - startBalance;
    }
    
    public String getTransactionValue(int index) {
        return doubleToCash(getTransactionDouble(index));
    }
    
    public TransactionTuple getTransactionTuple(int index) {
        return transactions.get(index);
    }
    
    public ArrayList<TransactionTuple> getTransactionTuples(int n) {
        if ( n > transactions.size() )
            n = transactions.size();
        
        ArrayList<TransactionTuple> result = new ArrayList<>();
        for ( int i = n; i < transactions.size(); i++ ) {
            result.add(transactions.get(i));
        }
        return result;
    }
    
    public ArrayList<TransactionTuple> getTransactionTuples() {
        return transactions;
    }
    
    public String getTransaction(int index) {
        double trans = getTransactionDouble(index);
        String result;
        if ( trans >= 0 ) {
            result = "Deposit of " + doubleToCash(trans) + 
                    " on " + getTransactionDate(index).toString();
        }
        else {
            result = "Withdrawl of " + doubleToCash(0-trans) +
                    " on " + getTransactionDate(index).toString();
        }
        return result;
    }
    
    public static String doubleToCash(double val) {
        return "$" + String.format("%.2f", val);
    }
    
    public String toXMLString() {
        String xml = "<bankaccount>\n";
        xml += "\t<accountnumber>" + getAccountNumberString() + "</accountnumber>\n";
        xml += "\t<pin>" + getPinString() + "</pin>\n";
        xml += "\t<balance>" + getBalanceXML() + "</balance>\n";
        xml += transToXMLString();
        xml += namesToXMLString();
        xml += "</bankaccount>\n";
        return xml;
    }
       
    private String namesToXMLString() {
        String result = "\t<names>\n";
        for ( int i = 0; i < names.size(); i++ ) {
            result += "\t\t<name>\n";
            result += "\t\t\t<firstname>" + names.get(i).getFirstName() + "</firstname>\n";
            result += "\t\t\t<lastname>" + names.get(i).getLastName() + "</lastname>\n";
            result += "\t\t\t<midname>" + names.get(i).getMidName() + "</midname>\n";
            result += "\t\t\t<title>" + names.get(i).getTitle() + "</title>\n";
            result += "\t\t</name>\n";
        }
        result += "\t</names>\n";
        return result;
    }
    
    private String transToXMLString() {
        String result = "\t<transactions>\n";
        Double temp;
        for ( int i = 0; i < transactions.size(); i++ ) {
            temp = transactions.get(i).getBalance();
            result += "\t\t<transaction>\n\t\t\t<bal>" 
                    + String.format("%.2f", temp) + "</bal>\n"
                    + "\t\t\t<date>" + transactions.get(i).getDate().getTime()
                    + "</date>\n"
                    + "</transaction>\n";
        }
        
        result += "\t</transactions>\n";
        return result;
    }
        /*
    private ArrayList<TransactionTuple> transactions;
    private final long acctNum;
    private int pin;
    private double balance;
    private ArrayList<PersonName> names = new ArrayList<>();
    */
    
    public boolean equals(BankAccount a) {
        boolean result = true;
        if ( !transactionsEqual(a) )
            result = false;
        if ( acctNum != a.acctNum )
            result = false;
        if ( pin != a.pin )
            result = false;
        if ( balance != a.balance )
            result = false;
        if ( !namesEqual(a) )
            result = false;
        
        return result;
    }
    
    private boolean namesEqual(BankAccount a) {
        int t = this.names.size();
        int b = a.getNames().size();
        if ( t != b ) 
            return false;
        
        for ( int i = 0; i < t; i++ ) {
            if ( !names.get(i).equals(a.getNames().get(i)) )
                return false;
        }
        return true;
    }
    
    /**
     * Returns true if all transactions from two accounts are equal.
     * Used in public boolean equals(BankAccount a)
     * @param a
     * @return Returns true if all transactions from two accounts are equal.
     */
    private boolean transactionsEqual(BankAccount a) {
        if ( transactions.size() != a.transactions.size() ) 
            return false;
        
        for ( int i = 0; i < transactions.size(); i++ ) {
            if ( !transactions.get(i).equals(a.transactions.get(i)) )
                return false;
        }
        return true;
    }
}
