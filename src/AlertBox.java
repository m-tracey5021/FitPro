


import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox implements EventHandler<ActionEvent>{
	
	private Stage window;
	private Scene scene;
	private VBox layout;
	private Button okButton, yes, no;
	private Label message;
	private WindowController controller;
	
	public AlertBox(String title, String messageStr) {
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		scene = new Scene(layout, 300, 100);
		okButton = new Button("OK");
		okButton.setOnAction(this);
		okButton.setAlignment(Pos.CENTER);
		
		
		message = new Label(messageStr);
		message.setAlignment(Pos.CENTER);
		message.setFont(new Font("Cambria", 10));
		
		
		layout.getChildren().addAll(message, okButton);
		window.setScene(scene);
		window.showAndWait();
		
	}
	public AlertBox(String title, String messageStr, String modalType, WindowController controller) {
		this.controller = controller;
		
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		
		message = new Label(messageStr);
		message.setAlignment(Pos.CENTER);
		message.setFont(new Font("Cambria", 10));
		
		layout.getChildren().addAll(message);
		
		if (modalType == "choiceModal") {
			this.addYesNo();
		}else if(modalType == "warningModal") {
			this.addOK();
		}else {
			System.out.println("Wrong modalType passed");
			window.close();
		}
		
		
	
		scene = new Scene(layout, 300, 100);
		window.setScene(scene);
		window.showAndWait();
		
	}
	public WindowController getController() {
		return this.controller;
	}
	public void setController(WindowController wc) {
		this.controller = wc;
	}
	public void addOK() {
		okButton = new Button("OK");
		okButton.setOnAction(e -> {
			window.close();
		});
		okButton.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(okButton);
	}
	public void addYesNo() {
		yes = new Button("Yes");
		yes.setOnAction(e -> {
			this.controller.setBool(true);
			window.close();
		});
		yes.setAlignment(Pos.BASELINE_LEFT);
		
		no = new Button("No");
		no.setOnAction(e -> {
			this.controller.setBool(false);
			window.close();
		});
		no.setAlignment(Pos.BASELINE_RIGHT);
		layout.getChildren().addAll(yes, no);
		
	}
	@Override
	public void handle(ActionEvent e) {
		
	}
}
