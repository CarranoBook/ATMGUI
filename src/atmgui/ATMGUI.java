/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package atmgui;

import java.util.Date;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author NB <nb@fs.org>
 */
public class ATMGUI extends Application {
    
    @Override
    public void start(final Stage primaryStage) {
        final ATM atm = new ATM("src\\\\ATMGUI\\\\save.xml");
        atm.loadAccounts();
        primaryStage.setTitle("ATM Banking");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setId("scene-title");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label actNumLbl = new Label("Account Number:");
        grid.add(actNumLbl, 0, 1);

        final TextField numTextField = new TextField();
        grid.add(numTextField, 1, 1);

        Label pw = new Label("PIN:");
        grid.add(pw, 0, 2);

        final PasswordField pinBox = new PasswordField();
        grid.add(pinBox, 1, 2);
        grid.setGridLinesVisible(false);
        
        Button signInBtn = new Button("Login");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signInBtn);
        grid.add(hbBtn, 1, 4);
        final Text actionTarget = new Text();
        actionTarget.setId("action-target");
            grid.add(actionTarget, 1, 6, 3, 1);
            
        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                String acct = numTextField.getCharacters().toString();
                String pinS = pinBox.getCharacters().toString();
                
                if ( pinS.equals(atm.getAdminPassword()) && acct.toLowerCase().equals("admin") ) {
                    adminATM(primaryStage, atm);
                }
                else {
                    try{
                    long num = Long.parseLong(acct);
                    numTextField.clear();
                    int pin = Integer.parseInt(pinS);
                    pinBox.clear();
                    
                    boolean success = atm.login(num, pin);
                    if ( !success )
                        actionTarget.setText("Incorrect PIN");
                    
                    else {
                        actionTarget.setText("Logged in!");
                        mainATM(atm.getAccount(), primaryStage, atm);
                    }
                    }
                    catch (IllegalArgumentException f) {
                        actionTarget.setText("Account not found");
                    }
                }
                
            }
        });
        
        Scene scene = new Scene(grid, 750, 375);
        scene.getStylesheets().add(ATMGUI.class.getResource("ATMCSS.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void adminATM(final Stage stage, final ATM atm) {
        stage.setTitle("Add an Account");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        
        Text header = new Text("Enter Account Information:");
        header.setId("admin-header");
        header.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        grid.add(header, 0, 0, 2, 1);
        
        Date now = new Date();
        Label day = new Label(now.toString().substring(0,10));
        GridPane dGrid = new GridPane();
        dGrid.setHgap(0);
        dGrid.setVgap(0);
        dGrid.setAlignment(Pos.CENTER_LEFT);
        
        grid.add(day, 4, 0);
        Text asterisk = new Text("*");
        asterisk.setFill(Color.FIREBRICK);
        dGrid.add(asterisk, 0, 0);
        Text asterText = new Text(" indicates required field");
        dGrid.add(asterText, 1, 0);
        
        grid.add(dGrid, 4, 1);
        
        Label acctNumber = new Label("Account Number:");
        final TextField acctNumField = new TextField();
        Text asterNum = new Text("*");
        asterNum.setFill(Color.FIREBRICK);
        
        grid.add(acctNumber, 0, 1);
        grid.add(acctNumField, 1, 1);
        grid.add(asterNum, 2, 1);
        
        Label pinText = new Label("PIN:");
        final TextField pinField = new TextField();
        Text asterPin = new Text("*");
        asterPin.setFill(Color.FIREBRICK);
        
        
        
        grid.add(pinText, 0, 2);
        grid.add(pinField, 1, 2);
        grid.add(asterPin, 2, 2);
        
        Label titleText = new Label("Title:");
        Label fNameText = new Label("First Name:");
        Label mNameText = new Label("Middle Name:");
        Label lNameText = new Label("Last Name:");
        Text asteriskfName = new Text("*");
        asteriskfName.setFill(Color.FIREBRICK);
        Text asterisklName = new Text("*");
        asterisklName.setFill(Color.FIREBRICK);
        final TextField titleField = new TextField();
        final TextField fNameField = new TextField();
        final TextField mNameField = new TextField();
        final TextField lNameField = new TextField();
        
        grid.add(titleText, 0, 3);
        grid.add(titleField, 1, 3);
        grid.add(fNameText, 0, 4);
        grid.add(fNameField, 1, 4);
        grid.add(asteriskfName, 2, 4);
        grid.add(mNameText, 0, 5);
        grid.add(mNameField, 1, 5);
        grid.add(lNameText, 0, 6);
        grid.add(lNameField, 1, 6);
        grid.add(asterisklName, 2, 6);
        
        Label createText = new Label("Create Account");
        Button create = new Button("Create");
        HBox hCreate = new HBox();
        hCreate.setAlignment(Pos.BOTTOM_LEFT);
        hCreate.getChildren().add(create);
        final Text warningText = new Text();
        warningText.setId("warningText");
        
        
        grid.add(warningText, 1, 8, 4, 1);
        grid.add(createText, 0, 7);
        grid.add(hCreate, 1, 7);
        
        Button saveBtn = new Button("Done");
        HBox hSave = new HBox();
        hSave.setAlignment(Pos.BOTTOM_LEFT);
        hSave.getChildren().add(saveBtn);
        
        grid.add(hSave, 0, 8);
        
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                atm.saveToXML();
                start(stage);
            }
        });

        create.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                try {
                    String firstS = fNameField.getCharacters().toString().trim();
                    String midS = mNameField.getCharacters().toString().trim();
                    String lastS = lNameField.getCharacters().toString().trim();
                    String titleS = titleField.getCharacters().toString().trim();
                    long actNumber = Long.parseLong(acctNumField.getCharacters().toString());
                    int pinNumber = Integer.parseInt(pinField.getCharacters().toString());
                    if ( !firstS.equals("") && !lastS.equals("") ) {
                        PersonName newPerson = new PersonName(firstS, lastS, midS, titleS);
                        BankAccount newBA = new BankAccount(actNumber, pinNumber, 0, newPerson);
                        atm.addAccount(newBA);
                        warningText.setText("Account added");
                        warningText.setFill(Color.GREEN);
                    }
                    else {
                        warningText.setText("Please fill in all required fields");
                        warningText.setFill(Color.FIREBRICK);
                    }
                }
                catch (NumberFormatException f) {
                    warningText.setText("Please enter numbers for Account Number and PIN");
                    warningText.setFill(Color.FIREBRICK);
                }
                catch (IllegalArgumentException f) {
                    warningText.setText("Account number already in use.");
                    warningText.setFill(Color.FIREBRICK);
                }
            }
        });
        
        Scene scene = new Scene(grid, 750, 375);
        scene.getStylesheets().add(ATMGUI.class.getResource("ATMCSS.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    
    public void mainATM(final BankAccount ba, final Stage atmStage, final ATM atm) {
        
        PersonName user = ba.getNames().get(0);
        String title = "Welcome " + user.getTitle() + " " + user.getLastName();
        atmStage.setTitle(title);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        
        Text welcome = new Text("Welcome, " + user.getTitle() + " " + user.getLastName());
        welcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        welcome.setId("welcome");
        grid.add(welcome, 0, 0, 2, 1);
        
        Label dtg = new Label();
        Date now = new Date();
        dtg.setText(now.toString().substring(0, 10));
        grid.add(dtg, 2, 0);
        
        Label with = new Label("Withdrawal History");
        Button getWith = new Button("OK");
        HBox hgetWith = new HBox();
        hgetWith.getChildren().add(getWith);
        hgetWith.setAlignment(Pos.CENTER_LEFT);
        
        Button depositBtn = new Button("Deposit");
        HBox hdeposit = new HBox();
        hdeposit.getChildren().add(depositBtn);
        hdeposit.setAlignment(Pos.CENTER_LEFT);
        final TextField depositField = new TextField();
        
        Button withdrawalBtn = new Button("Withdraw");
        HBox hwithdraw = new HBox();
        hwithdraw.getChildren().add(withdrawalBtn);
        hwithdraw.setAlignment(Pos.CENTER_LEFT);
        final TextField withdrawField = new TextField();
        
        grid.add(hwithdraw, 0, 3);
        grid.add(withdrawField, 1, 3);
        
        grid.add(hdeposit, 0, 2);
        grid.add(depositField, 1, 2);
        
        //grid.add(with, 0, 4);
        //grid.add(hgetWith, 1, 4);
        
        Button balBtn = new Button("View Balance");
        HBox hbalBtn = new HBox();
        hbalBtn.getChildren().add(balBtn);
        hbalBtn.setAlignment(Pos.CENTER_LEFT);
        
        grid.add(hbalBtn, 0, 5);
        
        Button exitBtn = new Button("Exit");
        HBox hexitBtn = new HBox();
        hexitBtn.getChildren().add(exitBtn);
        hexitBtn.setAlignment(Pos.CENTER_LEFT);
        
        grid.add(hexitBtn, 1, 5);
       
        
        final Text actionTarget = new Text();
            grid.add(actionTarget, 1, 8, 3, 1);
            
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent e) {
                atm.saveToXML();
                start(atmStage);
            }
        });
            
        balBtn.setOnAction(new EventHandler<ActionEvent>() {
           
            @Override
            public void handle(ActionEvent e) {
                actionTarget.setText("Your balance is " + ba.getBalanceString());
                actionTarget.setFill(Color.GREEN);
            }
        });
        
        depositBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                try {
                    double d = Double.parseDouble(depositField.getCharacters().toString());
                    ba.deposit(d);
                    actionTarget.setText(BankAccount.doubleToCash(d) + " depositted.  "
                                + "New balance of " + ba.getBalanceString());
                    depositField.clear();
                    actionTarget.setFill(Color.GREEN);
                }
                catch (IllegalArgumentException f) {
                    actionTarget.setText("Please enter a positive value to deposit.");
                    actionTarget.setFill(Color.FIREBRICK);
                }
            }
        });
        
        withdrawalBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                try {
                    double d = Double.parseDouble(withdrawField.getCharacters().toString());
                    ba.withdraw(d);
                    actionTarget.setText(BankAccount.doubleToCash(d) + " withdrawn.  "
                                    + "New balance of " + ba.getBalanceString());
                    withdrawField.clear();
                    actionTarget.setFill(Color.GREEN);
                }
                catch ( IllegalArgumentException f) {
                    actionTarget.setText("Please enter a positive value less than\n" + ba.getBalanceString() + " to withdraw.");
                    actionTarget.setFill(Color.FIREBRICK);
                    withdrawField.clear();
                }
                
            }
        });
        
        
        Scene transScene = new Scene(grid, 750, 375);
        transScene.getStylesheets().add(ATMGUI.class.getResource("ATMCSS.css").toExternalForm());
        atmStage.setScene(transScene);
        atmStage.show(); 
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
