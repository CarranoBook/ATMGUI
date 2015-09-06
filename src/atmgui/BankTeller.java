/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author NB <nb@fs.org>
 */
public class BankTeller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //PersonName test = new PersonName("Nathan", "Bleier", "Lewis", "Mr.");
        //BankAccount acc1 = new BankAccount(12345, 1234, 200.00, test);
        //BankAccount acc2 = new BankAccount(12346, 1234, 200.00, new PersonName());
        ATM myATM = new ATM("src\\\\BankTeller\\\\save.xml");
        //myATM.addAccount(acc1);
        //myATM.addAccount(acc2);
        //testXMLFileWrite(myATM);
        myATM.loadAccounts();

        testXMLFileWriteAndRead(myATM);
        testLogin(myATM, 12346, 1234);
        
        /*String s = "\t\t\t<bal>200.00</bal>";
        Pattern pat = Pattern.compile("\\d*\\.\\d\\d");
        Matcher mat = pat.matcher(s);
        if ( mat.find() ) {
            String h = mat.group();
            System.out.println(h);
        }*/
        
        
        
    }

    public static void testXMLFileWriteAndRead(ATM atm) {
        atm.saveToXML();
        ATM newATM  = new ATM(atm.getFilePath());
        newATM.loadAccounts();
        String tab;
        if ( atm.equals(newATM) )
            tab = "the same.";
        else
            tab = "not the same.  Error occured.";
        
        System.out.println("New ATM loaded from save file of old ATM.  ATMs are " + tab);
    }
    
    public static void testLogin(ATM atm, long acctNum, int pin) {
        System.out.println("Logging into account " + acctNum);
        try {
            atm.login(acctNum, pin);
            System.out.println("Login succesful? " + atm.loggedIn());
            atm.logout();
            if ( atm.loggedIn() ) {
                System.out.println("Logging out");
                System.out.println("Logged out succesful? " + !atm.loggedIn());
            }
        }
        catch ( IllegalArgumentException e ) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public static void testXMLString(BankAccount ba) {
        System.out.println(ba.toXMLString());
    }
    
    public static String testWithdraw(BankAccount ba) {
        String result = "Test withdraw:\n";
        result += "Balance before withdrawal: " + ba.getBalanceString();
        result += "\nWithdrawling $50.35\n";
        ba.withdraw(50.35);
        result += "Balance after withdrawal: " + ba.getBalanceString();
        return result;
    }
    
    public static String testDeposit(BankAccount ba) {
        String result = "Test Deposit:\n";
        result += "Balance before deposit: " + ba.getBalanceString();
        result += "\nDepositing $125.25\n";
        ba.deposit(125.25);
        result += "Balance after deposit: " + ba.getBalanceString();
        return result;
    }
    
}
