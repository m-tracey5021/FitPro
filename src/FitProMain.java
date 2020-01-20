import java.sql.*;
import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import javafx.stage.Stage;



public class FitProMain extends Application implements EventHandler<ActionEvent> {
	
	
	
	private Stage window;
	private Scene s1, s2;
	private GridPane grid1, grid2;
	private VBox layout1, layout2;
	private Button b1, b2, b3, b4;
	private Label l1, l2;

	public static void main(String[] args) {
		launch(args);
		/*
		 * ArrayList<ArrayList<Integer>> RPSPW = new ArrayList<ArrayList<Integer>>();
		 * ArrayList<Integer> s1 = new ArrayList<>(Arrays.asList(5, 5, 5));
		 * ArrayList<Integer> s2 = new ArrayList<>(Arrays.asList(3, 3, 3));
		 * ArrayList<Integer> s3 = new ArrayList<>(Arrays.asList(3, 2, 1));
		 * RPSPW.add(s1); RPSPW.add(s2); RPSPW.add(s3);
		 * 
		 * ArrayList<ArrayList<Double>> IPSPW = new ArrayList<ArrayList<Double>>();
		 * ArrayList<Double> i1 = new ArrayList<>(Arrays.asList(7.5, 5.0));
		 * ArrayList<Double> i2 = new ArrayList<>(Arrays.asList(5.0, 5.0));
		 * ArrayList<Double> i3 = new ArrayList<>(Arrays.asList(2.5, 2.5));
		 * IPSPW.add(i1); IPSPW.add(i2); IPSPW.add(i3); ArrayList<Double> maxPercents =
		 * new ArrayList<>(Arrays.asList(70.0, 80.0, 90.0)); Cycle c = new
		 * Cycle("Poops", 3, 1, 100.00, RPSPW, IPSPW, maxPercents);
		 */
		
		
		
    	
		

	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		LoginWindow login = new LoginWindow();
		/*
		 * window = primaryStage; window.setTitle("PoopyWindow"); grid1 = new
		 * GridPane(); grid1.setPadding(new Insets(10, 10, 10, 10)); grid1.setHgap(20);
		 * grid1.setVgap(10);
		 * 
		 * grid2 = new GridPane(); grid2.setPadding(new Insets(10, 10, 10, 10));
		 * grid2.setHgap(20); grid2.setVgap(10);
		 * 
		 * 
		 * s1 = new Scene(grid1, 300, 250); s2 = new Scene(grid2, 300, 250);
		 * 
		 * b1 = new Button("Create new cycle"); b1.setOnAction(this);
		 * GridPane.setConstraints(b1, 0, 1);
		 * 
		 * b2 = new Button("Go to non-Poopy scene"); b2.setOnAction(this);
		 * GridPane.setConstraints(b2, 1, 1);
		 * 
		 * b3 = new Button("Go to Poopy scene"); b3.setOnAction(this);
		 * GridPane.setConstraints(b3, 0, 1);
		 * 
		 * b4 = new Button("Open non-Poopy graph"); b4.setOnAction(this);
		 * GridPane.setConstraints(b4, 1, 1);
		 * 
		 * l1 = new Label("This is a Poopy scene"); GridPane.setConstraints(l1, 0, 0);
		 * l2 = new Label("This is a non-Poopy scene"); GridPane.setConstraints(l2, 0,
		 * 0);
		 * 
		 * grid1.getChildren().addAll(l1, b1, b2); grid2.getChildren().addAll(l2, b3,
		 * b4);
		 * 
		 * 
		 * 
		 * window.setScene(s1); window.show();
		 */
		
	}
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == b1) {
			//CycleCreationWindow ccw = new CycleCreationWindow("New cycle");
			
			
			//ccw.display("New cycle");
			
			/*
			 * System.out.println("New cycle creation window created");
			 * System.out.println("Cycle name from within FitProMain: " +
			 * ccw.getCycleName());
			 */
			
		}
		if (event.getSource() == b2) {
			window.setScene(s2);
		}
		if (event.getSource() == b3) {
			window.setScene(s1);
		}
		if (event.getSource() == b4) {
			GraphWindow gWindow = new GraphWindow();
			gWindow.display("GraphWindow", "This will be the graph");
		}
		
	}

}
