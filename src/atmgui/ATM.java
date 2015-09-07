/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author NB <nb@fs.org>
 */
public class ATM {
    private ArrayList<BankAccount> accounts = new ArrayList<>();
    private String filePath; // path is src\\BankTeller\\save.xml
    private int actIndex = -1;
    private static String admin = "admin";
    private String adminPassword;
    
    ATM(String path) {
        filePath = path;
        adminPassword = "password";
    }
    
    ATM(String path, ArrayList<BankAccount> accounts) {
        filePath = path;
        this.accounts = accounts;
        adminPassword = "password";
    }
    
    public void setAdminPassword(String pass) {
        adminPassword = pass;
    }
    
    public String getAdminPassword() {
        return this.adminPassword;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Checks to see if login credentials are correct.
     * @param acctNum account number of account to access
     * @param pin pin number of account to access
     * @return true if login is successful false if else
     */
    public boolean login(long acctNum, int pin) {
        if ( loggedIn() )
            throw new IllegalArgumentException("Already logged into an account.  Log out first before logging into a new account.");
        
        int index = acctIndex(acctNum);
        if ( this.acctIndex(acctNum) == -1 )
            throw new IllegalArgumentException("Account not found");
        
        boolean val = verifyPin(index, pin);
        if ( val )
            this.actIndex = index;
            
        return val;
    }
    
    /**
     * This method returns the bank account that the user is logged into
     * @return bank account which is logged in
     */
    public BankAccount getAccount() {
        if ( !loggedIn() )
            return null;
        
        return accounts.get(actIndex);
    }
    
    /**
     * Checks to see if an account has been logged into
     * @return true if an account is logged in, else false
     */
    public boolean loggedIn() {
        return actIndex != -1;
    }
    
    /**
     * Logs out of an account.
     */
    public void logout() {
        actIndex = -1;
    }
    
    /**
     * Checks to see if a pin is assosciated with an account
     * @param index index of account in the ArrayList
     * @param pin pin number of account
     * @return true if they are assosciated, false if not
     */
    private boolean verifyPin(int index, int pin) {     
        return pin == accounts.get(index).getPin();
    }
    
    /**
     * Checks if an account is loaded into the ATM by searching for
     * its account number and returns its location in the ArrayList
     * @param acctNum the account number to search for
     * @return index in the ArrayList if found, else -1
     */
    private int acctIndex(long acctNum) {
        for ( int i = 0; i < accounts.size(); i++ ) {
            if ( acctNum == accounts.get(i).getAccountNumber() )
                return i;
        }
        return -1;
    }
    
    /**
     * Adds a BankAccount to the ATM.  Throws an IllegalArgumentException
     * if account with a duplicate account number is added
     * @param a bank account to add to the ATM
     */
    public void addAccount(BankAccount a) {
        for ( int i = 0; accounts != null && i < accounts.size(); i++ ) {
            if (a.getAccountNumber() == accounts.get(i).getAccountNumber()) 
                throw new IllegalArgumentException("Accounts cannot have duplicate account number");
        }
        accounts.add(a);
    }
    
    /**
     * saves the accounts in the ATM to an xml file at the designated filepath
     */
    public void saveToXML() {
        saveToXML(filePath);
    }
    
    /**
     * saves the accounts in the ATM to an xml file at the path provided in the
     * paramaters
     * @param path filepath of the xml file
     */
    public void saveToXML(String path) {
        File xml = new File(path);
        String res = "";
        for ( int i = 0; i < accounts.size(); i++ ) {
            res += accounts.get(i).toXMLString();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new FileWriter(xml));
            writer.write(res);
        }
        catch ( IOException e ) {
            System.out.println(e);
        }
        finally  {
            try {
                if ( writer != null )
                    writer.close();
            }
            catch ( IOException e) {
                System.out.println(e);
            }
        }
        
    }
    
    /**
     * Loads accounts from xml file at the designated path
     * @return number of accounts loaded
     */
    public int loadAccounts() {
        return loadAccounts(filePath);
    }
    
    /**
     * Loads accounts from xml file at the path provided
     * @param path filepath of xml file to load accounts from
     * @return number of accounts loaded
     */
    public int loadAccounts(String path) {
        int acctCounter = 0;
        BufferedReader reader = null;
        ArrayList<String> xml = new ArrayList<>();
        try {
            reader = new BufferedReader( new FileReader(path));
            String line;
            while ( (line = reader.readLine()) != null ) 
                xml.add(line);
        }//end try
        catch ( IOException e ) {
            System.out.println(e);
        }//end catch
        //DOM has now been imported to ArrayList<String> xml
        for ( int i = 0; i < xml.size(); i++ ) {
            if ( xml.get(i).equals("<bankaccount>") )
                loadAccountFromXML(xml, i);
        }
        return xml.size();
    }
    
    /**
     * This method is called from public int loadAccounts(String path)
     * This method adds a single account from the xml save file to the 
     * ATM
     * @param xml an ArrayList<String> representing the XML file
     * @param index the index of the ArrayList from which to start scanning
     * for an account to load
     */
    private void loadAccountFromXML(ArrayList<String> xml, int index) {
        long acctNum = 0;
        int pin = 0;
        double balance = 0;
        ArrayList<TransactionTuple> transactions = new ArrayList<>();
        ArrayList<PersonName> names = new ArrayList<>();
        int indexLimit = index;
        boolean iterateStop = false;
        Pattern pattern;
        Matcher matcher;
        
        
        for (int i = index + 1; i < xml.size() && !iterateStop; i++ ) {
            if ( xml.get(i).equals("</bankaccount>") ) {
                iterateStop = true;
                indexLimit = i;
            }
        }
        
        String s;        
        iterateStop = false;
        pattern = Pattern.compile("\\d+");
        for (int i = index + 1;!iterateStop && i < indexLimit; i++ ) {

            s = xml.get(i);

            if ( s.contains("<accountnumber>") ) {
                matcher = pattern.matcher(s);
                iterateStop = true;
                if (matcher.find()) {
                    acctNum = Long.parseLong(matcher.group());
                }//end inner if
            }//end outer if
        } //end for (search for accountnumber
        
        iterateStop = false;
        for (int i = index + 1; !iterateStop && i < indexLimit; i++ ) {
            s = xml.get(i);
            if ( s.contains("<pin>") ) {
                matcher = pattern.matcher(s);
                iterateStop = true;
                if ( matcher.find() ) {
                    pin = Integer.parseInt(matcher.group());
                }//end inner if
            }//end outer if
        }//end for ( search for pin )
        
        iterateStop = false;
        pattern = Pattern.compile("\\d*\\.\\d\\d");
        for ( int i = index + 1; !iterateStop && i < indexLimit; i++ ) {
            s = xml.get(i);
            if ( s.contains("<balance>") ) {
                matcher = pattern.matcher(s);
                iterateStop = true;
                if ( matcher.find() ) {
                    balance = Double.parseDouble(matcher.group() );
                }//end inner if
            }//end outer if
        }//end for ( search for balance )
        
        Pattern patternDouble = Pattern.compile("\\d*.\\d\\d");
        Pattern patternLong = Pattern.compile("\\d+");
        for ( int i = index + 1; i < indexLimit; i++ ) {
            s = xml.get(i);
            if ( s.contains("<transaction>") ) {
                String bal = xml.get((i+1));
                String dte = xml.get((i+2));
                if ( bal.contains("<bal>") && dte.contains("<date>") ) {
                    double xmlBal = 0;
                    long xmlDate = 0;
                    
                    matcher = patternDouble.matcher(bal.trim());
                    if ( matcher.find() ) {
                        String j = bal.trim().replaceAll("<bal>", "").replaceAll("</bal>", "");
                        xmlBal = Double.parseDouble(j);
                    }
                    
                    matcher = patternLong.matcher(dte);
                    if ( matcher.find() )
                        xmlDate = Long.parseLong(matcher.group());
                    
                    if ( xmlDate != 0 )
                        transactions.add(new TransactionTuple(xmlBal, xmlDate));
                } //end inner if
            }//end outer if
        } //end for ( search for transactions )
        
        pattern = Pattern.compile(">[a-zA-Z*]<");
        for ( int i = index + 1; i < indexLimit; i++ ) {
            s = xml.get(i).trim();
            if ( s.contains("<name>") ) {
                String fName = xml.get(i+1).trim();
                fName = fName.replaceAll("<firstname>", "");
                fName = fName.replaceAll("</firstname>", "");
                String lName = xml.get(i+2).trim();
                lName = lName.replaceAll("<lastname>", "").replaceAll("</lastname>", "");
                String mName = xml.get(i+3).trim();
                mName = mName.replaceAll("<midname>","").replaceAll("</midname>","");
                String title = xml.get(i+4).trim();
                title = title.replaceAll("<title>","").replaceAll("</title>","");
                PersonName temp = new PersonName(fName, lName, mName, title);
                names.add(temp);
            }
        }
        BankAccount temp = new BankAccount(acctNum, pin, balance, names, transactions);
        this.addAccount(temp);
    }
    
    /**
     * Returns true if the accounts loaded to both ATMs are the same
     * @param other ATM to check against
     * @return true if ATMs have same accounts loaded, false if not.
     */
    public boolean equals(ATM other) {
        if (accounts.size() != other.accounts.size() ) 
            return false;
        
        for ( int i = 0; i < accounts.size(); i++ ) {
            if ( !accounts.get(i).equals(other.accounts.get(i)) )
                return false;
        }
        
        return true;
    }
        
}
