/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

/**
 *
 * @author NB <nb@fs.org>
 */
class PersonName {
    private final String firstName;
    private final String lastName;
    private final String midName;
    private final String title;
    private static Integer counter = 1;
    
    PersonName(String fName, String lName, String mName, String title) {
        firstName = fName;
        lastName = lName;
        midName = mName;
        this.title = title;
    }
    
    PersonName(String fName, String lName, String mName) {
        this(fName, lName, mName, null);
    }
    
    PersonName(String fName, String lName) {
        this(fName, lName, null, null);
    }
    
    PersonName() {
        this("Robert", "Bojangles", "Dwight", counter.toString());
        counter++;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getMidName() {
        return midName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String toString() {
        return title + " " + firstName + " " + midName + " " + lastName;
    }
    
    public boolean equals(PersonName other) {
        boolean result = true;
        if ( !firstName.equals(other.getFirstName()) || !lastName.equals(other.getLastName()) 
                || !midName.equals(other.getMidName()) || !title.equals(other.getTitle()) )
            result = false;
        
        return result;
    }
    
}
