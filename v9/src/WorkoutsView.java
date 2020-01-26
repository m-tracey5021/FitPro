import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WorkoutsView {
	private Stage window;
	private Scene scene;
	private VBox layout;
	private Button cancel;
	private Label message;
	private WindowController controller;
	private DateNodeContainer dateNodeContainer;
	
	public WorkoutsView(String title, WindowController controller, DateNodeContainer dateNodeContainer) {
		
		this.controller = controller;
		this.dateNodeContainer = dateNodeContainer;
		window = new Stage();
		window.setTitle(title);
		
		
		
		scene = new Scene(layout);
		window.setScene(scene);
		window.show();
	}
}
