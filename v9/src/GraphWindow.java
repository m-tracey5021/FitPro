import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GraphWindow {
	private Stage window;
	private Scene s1;
	private VBox layout1;
	private Label l1;
	
	
	
	public GraphWindow() {
		window = new Stage();
		layout1 = new VBox(20);
		s1 = new Scene(layout1, 300, 400);
		l1 = new Label();
		
	}
	public void display(String title, String message) {
		window.setTitle(title);
		l1.setText(message);
		layout1.getChildren().add(l1);
		window.setScene(s1);
		window.show();
	}
}






