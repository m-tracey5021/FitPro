import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import java.sql.*;
import java.util.*;

public class LoginWindow implements EventHandler<ActionEvent> {
	private Stage window;
	private Scene s1, s2;
	//private Layout layout;
	private GridPane g1, g2;
	private PasswordField passInput1, passInput2, passConfirmInput;
	private TextField userNameInput1, userNameInput2, emailInput;
	private Button loginButton, registerButton, submitButton;
	private Label userName1, userName2, pass1, pass2, passConfirm, email, notAUser, passwordWarning, userNameWarning;
	
	public LoginWindow() {
		window = new Stage();
		window.setTitle("Login");
		//window.setMinHeight(250);
		//window.setMinWidth(300);
		// figure out how to center grids in window
		
		g1 = new GridPane();
		g1.setHgap(20);
		g1.setVgap(10);
		g1.setPadding(new Insets(50, 50, 50, 50));
		/*
		 * for (int i = 0; i <= 4; i++) {
		 * 
		 * RowConstraints row = new RowConstraints(); if (i == 3) {
		 * row.setMinHeight(50); }
		 * 
		 * g1.getRowConstraints().add(row); }
		 */
		
		g2 = new GridPane();
		g2.setHgap(20);
		g2.setVgap(10);
		g2.setPadding(new Insets(50, 50, 50, 50));
		//g2.setVgrow(window, Priority.ALWAYS);
		
		userName1 = new Label("User name: ");
		GridPane.setConstraints(userName1, 0, 1);
		
		userNameInput1 = new TextField();
		GridPane.setConstraints(userNameInput1, 1, 1);
		
		userNameWarning = new Label("User Name does not Exist");
		userNameWarning.setVisible(false);
		GridPane.setConstraints(userNameWarning, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		
		pass1 = new Label("Password: ");
		GridPane.setConstraints(pass1, 0, 3);
		
		passInput1 = new PasswordField();
		GridPane.setConstraints(passInput1, 1, 3);
		
		passwordWarning = new Label("Wrong Password");
		passwordWarning.setVisible(false);
		GridPane.setConstraints(passwordWarning, 1, 2, 1, 1, HPos.CENTER, VPos.CENTER);
		
		notAUser = new Label("Don't have an Account?");
		GridPane.setMargin(notAUser, new Insets(100, 0, 0, 0));
		GridPane.setConstraints(notAUser, 0, 5, 2, 1, HPos.CENTER, VPos.CENTER);
		
		
		
		
		
		
		
		
		userName2 = new Label("User name: ");
		GridPane.setConstraints(userName2, 0, 0);
		
		userNameInput2 = new TextField();
		GridPane.setConstraints(userNameInput2, 1, 0);
		
		pass2 = new Label("Password: ");
		GridPane.setConstraints(pass2, 0, 1);
		
		passInput2 = new PasswordField();
		GridPane.setConstraints(passInput2, 1, 1);
		
		passConfirm = new Label("Confirm Password: ");
		GridPane.setConstraints(passConfirm, 0, 2);
		
		passConfirmInput = new PasswordField();
		GridPane.setConstraints(passConfirmInput, 1, 2);
		
		email = new Label("Enter Email: ");
		GridPane.setConstraints(email, 0, 3);
		
		emailInput = new TextField();
		GridPane.setConstraints(emailInput, 1, 3);
		
		loginButton = new Button("Login");
		loginButton.setOnAction(e -> {
			String inputUserName = userNameInput1.getText();
			String inputUserPass = passInput1.getText();
			
			SQLService sqlService = new SQLService();
			//sqlService.getObjectById
			try {
				ResultSet userRow = sqlService.getObjectByColumn(inputUserName, "userName", "Users");
				if (userRow.first()) {
					System.out.println("User exists");
					userNameWarning.setVisible(false);
					User retrievedUser = new User(userRow.getString(2), 
							userRow.getString(3), userRow.getString(4), userRow.getInt(5));
					retrievedUser.setId(userRow.getString(1));
					if (retrievedUser.getUserPass().equals(inputUserPass)) {
						System.out.println("Passwords Match!");
						passwordWarning.setVisible(false);
						MainWindow mw = new MainWindow(retrievedUser);
						window.close();
					}else {
						System.out.println("Passwords do not match");
						passwordWarning.setVisible(true);
					}
				}else {
					System.out.println("No User exists for that userName");
					userNameWarning.setVisible(true);
				}
			}catch (SQLException ex) {
				System.out.println(ex);
			}
			
			
			
		});
		GridPane.setConstraints(loginButton, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
		
		registerButton = new Button("Create an Account");
		registerButton.setOnAction(e -> {
			window.setTitle("Create an Account");
			window.setScene(s2);
		});
		GridPane.setConstraints(registerButton, 0, 6, 2, 1, HPos.CENTER, VPos.CENTER);
		
		submitButton = new Button("Submit Details");
		submitButton.setOnAction(e -> {
			if (passInput2.getText().equals(passConfirmInput.getText())) {
				User newUser = new User(userNameInput2.getText(), passInput2.getText(), emailInput.getText());
				SQLService sqlService = new SQLService();
				sqlService.connect();
				sqlService.upsertUser(newUser);
				MainWindow mw = new MainWindow(newUser);
				window.close();
			}else {
				
				System.out.println("Entered passwords do not match");
			}
			
			
		});
		GridPane.setConstraints(submitButton, 0, 4, 2, 1, HPos.CENTER, VPos.CENTER);
		
		g1.getChildren().addAll(userName1, pass1, userNameInput1, passInput1, notAUser, 
				userNameWarning, passwordWarning, loginButton, registerButton);
		g2.getChildren().addAll(userName2, pass2, passConfirm, email, userNameInput2, passInput2, passConfirmInput,
				emailInput, submitButton);
		
		s1 = new Scene(g1);
		s1.getStylesheets().add("FitProStyle.css");
		s2 = new Scene(g2);
		s2.getStylesheets().add("FitProStyle.css");
		window.setScene(s1);
		window.show();
		
	}
	
	
	
	
	
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
