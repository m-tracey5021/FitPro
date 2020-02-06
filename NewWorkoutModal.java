import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NewWorkoutModal implements EventHandler<ActionEvent>{
	
	private Stage window;
	private Scene scene;
	private GridPane grid;
	private VBox labels, inputs, workoutView;
	private HBox selectionButtons;
	private StackPane currentWorkoutStack;
	private Label currentWorkout, chosenMovement, currentMax, percent, sets, reps;
	private TextField currentMaxInput, percentInput;
	private ChoiceBox<Integer> setsChoice, repsChoice;
	private ChoiceBox<Movement> movementsChoice;
	private TreeView<String> workoutTree;
	private Button okButton, cancelButton;
	private ArrayList<Movement> movementsList, movementsChosen;
	private Workout workout;
	private WindowController controller;
	
	public NewWorkoutModal(WindowController controller) {
		this.controller = controller;
		this.workout = new Workout();
		
		window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Create new workout");
		
		grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setHgap(20);
		grid.setVgap(10);
		grid.setGridLinesVisible(true);
		
		labels = new VBox(20);
		labels.setAlignment(Pos.CENTER_LEFT);
		labels.setPrefWidth(120);
		GridPane.setConstraints(labels, 0, 0);
		
		inputs = new VBox(20);
		inputs.setAlignment(Pos.CENTER_LEFT);
		inputs.setPrefWidth(150);
		GridPane.setConstraints(inputs, 1, 0);
		
		
		
		
		
		
		
		chosenMovement = new Label("Chosen movement");
		
		movementsChosen = new ArrayList<Movement>();
		movementsList = new ArrayList<Movement>(
				Arrays.asList(new Movement("Squat"), new Movement("Bench"), new Movement("Deadlift")));
		movementsChoice = new ChoiceBox<Movement>();
		movementsChoice.getItems().addAll(movementsList);
		movementsChoice.setPrefWidth(100);
		
		//movementsRow = new HBox();
		//movementsRow.getChildren().addAll(chosenMovement, movementsChoice);
		
		
		sets = new Label("Sets");
		ArrayList<Integer> setsAndReps = new ArrayList<Integer>(
				Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		
		
		setsChoice = new ChoiceBox<Integer>();
		setsChoice.getItems().addAll(setsAndReps);
		setsChoice.setPrefWidth(100);
		
		//setsRow = new HBox();
		//setsRow.getChildren().addAll(sets, setsChoice);
		
		reps = new Label("Reps");
		repsChoice = new ChoiceBox<Integer>();
		repsChoice.getItems().addAll(setsAndReps);
		repsChoice.setPrefWidth(100);
		
		//repsRow = new HBox();
		//repsRow.getChildren().addAll(reps, repsChoice);
		
		currentMax = new Label("Current 1RM");
		currentMaxInput = new TextField();
		
		//currentMaxRow = new HBox();
		//currentMaxRow.getChildren().addAll(currentMax, currentMaxInput);
		
		percent = new Label("Percent");
		percentInput = new TextField();
		
		//percentRow = new HBox();
		//percentRow.getChildren().addAll(percent, percentInput);
		
		labels.getChildren().addAll(chosenMovement, currentMax, percent, sets, reps);
		inputs.getChildren().addAll(movementsChoice, setsChoice, repsChoice, currentMaxInput, percentInput);
		
		//currentWorkoutStack = new StackPane();
		//currentWorkoutStack.setPrefSize(200, 200);
		//GridPane.setConstraints(currentWorkoutStack, 2, 0);
		
		currentWorkout = new Label("Current workout");
		currentWorkout.setPrefHeight(30);
		
		workoutView = new VBox(50);
		workoutView.setAlignment(Pos.BASELINE_CENTER);
		workoutView.setPrefSize(200, 300);
		GridPane.setConstraints(workoutView, 2, 0);
		
		
		workoutTree = new TreeView<String>();
		workoutTree.setPrefHeight(270);
		setUpWorkoutTree();
		workoutView.getChildren().addAll(currentWorkout, workoutTree);
		
		
		
		selectionButtons = new HBox(50);
		selectionButtons.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(selectionButtons, 0, 1, 3, 1);
		GridPane.setMargin(selectionButtons, new Insets(100, 0, 0, 0));
		
		okButton = new Button("OK");
		okButton.setOnAction(e -> {
			workout.setMovements(movementsChosen);
			controller.setStoredWorkout(workout);
			window.close();
		});
		
		cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			window.close();
		});
		
		selectionButtons.getChildren().addAll(okButton, cancelButton);
		
		
		grid.getChildren().addAll(labels, inputs, workoutView, selectionButtons);
		
		scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}
	
	
	
	public void setUpWorkoutTree() {
		TreeItem<String> root = new TreeItem<String>(workout.basicToString());
		
		workoutTree.setRoot(root);

	}
	
	@Override
	public void handle(ActionEvent e) {
		
	}
}
