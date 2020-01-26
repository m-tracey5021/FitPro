import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewCyclePopupModal implements EventHandler<ActionEvent>{
	
	private Stage window;
	private Scene scene;
	private VBox verticalLayout;
	private HBox buttonsBar;
	private TextField cycleNameInput;
	private Button submit, cancel;
	private Label message;
	private WindowController controller;
	
	public NewCyclePopupModal(String title, String messageStr, WindowController controller) {
		
		this.controller = controller;
		
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		
		
		message = new Label(messageStr);
		
		cycleNameInput = new TextField();
		
		
		buttonsBar = new HBox(20);
		buttonsBar.setPrefHeight(50);
		
		
		submit = new Button("Submit");
		submit.setOnAction(e -> {
			controller.setString(cycleNameInput.getText());
			window.close();
		});
		cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			window.close();
		});
		
		
		buttonsBar.getChildren().addAll(submit, cancel);
		
		
		verticalLayout.getChildren().addAll(message, cycleNameInput, buttonsBar);
		
		
		scene = new Scene(verticalLayout);
		window.setScene(scene);
		window.showAndWait();
	}
	
	public void handle(ActionEvent e) {
		
	}
}
