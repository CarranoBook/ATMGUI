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
            grid.add(actionTarget, 1, 6);
            
        signInBtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                try{
                    String numString = numTextField.getCharacters().toString();
                    long num = Long.parseLong(numString);
                    numTextField.clear();
                    String pinString = pinBox.getCharacters().toString();
                    int pin = Integer.parseInt(pinString);
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
        });
        
        Scene scene = new Scene(grid, 750, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        grid.add(welcome, 0, 0, 2, 1);
        
        Text dtg = new Text();
        Date now = new Date();
        dtg.setText(now.toString().substring(0, 10));
        grid.add(dtg, 2, 0);
        
        Text with = new Text("Withdrawal History");
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
        
        grid.add(with, 0, 4);
        grid.add(hgetWith, 1, 4);
        
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
            grid.add(actionTarget, 1, 8);
            
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
        
        
        Scene transScene = new Scene(grid, 750, 275);
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
