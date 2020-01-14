import java.io.*;
import java.util.*;

/*import javax.swing.event.ChangeListener;*/

/*import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/



import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.*;

public class CycleCreationWindow implements EventHandler<ActionEvent> {
	
	private User user;
	private Stage window;
	private Scene s1;
	private GridPane grid1, grid2;
	private HBox layout1;
	// private VBox layout2;
	private Label cycleNameLabel, totalWorkoutsLabel, metricSelectionLabel, current1RMLabel, movementLabel, repsLabel,
			setsLabel, percentLabel, workoutIndicator, cycleIndicator;
	private TextField cycleNameInput, totalWorkoutsInput, currentMaxInput, percentInput;
	private Button submitCycleButton, completeWorkoutButton, addSetButton, completeCycleButton;
	private ChoiceBox<Movement> movementChoice;
	private ChoiceBox<Integer> repsChoice, setsChoice;
	private CheckBox checkBox1;
	private TextArea workoutDescription, cycleDescription;
	private ToggleGroup metricSelectionGroup;
	private RadioButton kilogramCheck, poundsCheck;

	private String cycleName;
	private int currentWorkoutIndex; // this is a counter to know which workout is current
	private Workout currentWorkout;
	private ArrayList<Workout> workouts;
	private int totalWorkouts;
	private Cycle c;
	
	private SQLService sqlService;
	
	private WindowController controller;

	public CycleCreationWindow(String title, User user, WindowController controller) {
		this.user = user;
		this.controller = controller;
		workouts = new ArrayList<Workout>();
		Workout firstWorkout = new Workout();
		workouts.add(firstWorkout);
		currentWorkoutIndex = 0;
		currentWorkout = workouts.get(currentWorkoutIndex);

		window = new Stage();
		window.setTitle(title);
		window.setMinHeight(600);
		window.setMinWidth(1000);
		grid1 = new GridPane();
		grid1.setPadding(new Insets(100, 25, 100, 25));
		grid1.setHgap(20);
		grid1.setVgap(10);
		for (int i = 0; i <= 8; i++) {

			RowConstraints row = new RowConstraints();
			if (i == 8) {
				row.setMinHeight(50);
			}

			grid1.getRowConstraints().add(row);
		}

		grid2 = new GridPane();

		grid2.setPadding(new Insets(10, 10, 10, 10));
		grid2.setHgap(20);
		grid2.setVgap(10);

		layout1 = new HBox(20, grid1, grid2);
		// layout2 = new VBox(20);

		cycleNameLabel = new Label("Cycle Name: ");
		GridPane.setConstraints(cycleNameLabel, 0, 1);
		totalWorkoutsLabel = new Label("Total Workouts: ");
		GridPane.setConstraints(totalWorkoutsLabel, 0, 3);
		metricSelectionLabel = new Label("Choose Metric: ");
		GridPane.setConstraints(metricSelectionLabel, 0, 5);

		movementLabel = new Label("Movement: ");
		GridPane.setConstraints(movementLabel, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		setsLabel = new Label("Sets: ");
		GridPane.setConstraints(setsLabel, 2, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		repsLabel = new Label("Reps: ");
		GridPane.setConstraints(repsLabel, 3, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		current1RMLabel = new Label("Current Movement 1RM: ");
		GridPane.setConstraints(current1RMLabel, 4, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		percentLabel = new Label("Percent: ");
		GridPane.setConstraints(percentLabel, 5, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		workoutIndicator = new Label();
		GridPane.setConstraints(workoutIndicator, 0, 2);
		cycleIndicator = new Label();
		GridPane.setConstraints(cycleIndicator, 0, 3);

		cycleNameInput = new TextField();
		GridPane.setConstraints(cycleNameInput, 0, 2);
		totalWorkoutsInput = new TextField();
		GridPane.setConstraints(totalWorkoutsInput, 0, 4);
		currentMaxInput = new TextField();
		GridPane.setConstraints(currentMaxInput, 4, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		currentMaxInput.setDisable(true);
		percentInput = new TextField();
		GridPane.setConstraints(percentInput, 5, 1);
		percentInput.setDisable(true);

		metricSelectionGroup = new ToggleGroup(); // this wont have any functionality yet

		kilogramCheck = new RadioButton("Kilograms");
		kilogramCheck.setToggleGroup(metricSelectionGroup);
		GridPane.setConstraints(kilogramCheck, 0, 6);
		poundsCheck = new RadioButton("Pounds");
		poundsCheck.setToggleGroup(metricSelectionGroup);
		GridPane.setConstraints(poundsCheck, 0, 7);

		
		
		
		submitCycleButton = new Button("Submit cycle");
		submitCycleButton.setOnAction(e -> {
			try {
				cycleName = cycleNameInput.getText();

				// currentMax = Double.parseDouble(currentMaxInput.getText());
				totalWorkouts = Integer.parseInt(totalWorkoutsInput.getText());
				if (cycleName.equals("")) {
					AlertBox ab1 = new AlertBox("Forms not completed",
							"Please complete all forms before submitting a cycle");
				} else {
					workoutIndicator.setText("Workout " + (currentWorkoutIndex + 1) + ": ");
					cycleIndicator.setText("Current Cycle: ");
					if (grid2.getChildren().contains(workoutIndicator) == false) {
						grid2.getChildren().add(workoutIndicator);
					}
					if (grid2.getChildren().contains(cycleIndicator) == false) {
						grid2.getChildren().add(cycleIndicator);
					}

					/*
					 * make sure that this ^ is only added once, even if the submit cycle button is
					 * pressed more than once
					 */
					cycleNameInput.setDisable(true);
					totalWorkoutsInput.setDisable(true);
					submitCycleButton.setDisable(true);

					movementChoice.setDisable(false);
					repsChoice.setDisable(false);
					setsChoice.setDisable(false);
					currentMaxInput.setDisable(false);
					percentInput.setDisable(false);
					addSetButton.setDisable(false);
					completeWorkoutButton.setDisable(false);
				}

			} catch (NumberFormatException exc) {
				AlertBox ab2 = new AlertBox("Forms not completed",
						"Please complete all forms before submitting a cycle");
				System.out.println("Got error: " + exc);
			}
		});
		GridPane.setConstraints(submitCycleButton, 0, 8, 1, 1, HPos.CENTER, VPos.CENTER);

		
		
		
		
		addSetButton = new Button("Add Sets");
		addSetButton.setOnAction(e -> {
			currentWorkout = workouts.get(currentWorkoutIndex);
			Movement chosenMovement = movementChoice.getValue();
			for (int i = 0; i < currentWorkout.getMovements().size(); i ++) {
				Movement toCompare = currentWorkout.getMovements().get(i);
				if (toCompare.getName().equals(chosenMovement.getName())) {
					System.out.println("toCompare: " + toCompare.getName() + " chosenMovement: " + chosenMovement.getName());
					if (toCompare.getCurrent1RM() != Double.parseDouble(currentMaxInput.getText())) {
						WindowController newWC = new WindowController();
						AlertBox newAB = new AlertBox("Inconsistent Max Alert", "Youre entered one rep max does not equal "
								+ "your previous entry for this movement. \n Do you want to continue?", "choiceModal", newWC);
						
						
						if (newWC.getBool() == true) { // if continue was pressed
							addSetMovementToWorkout();
							return;
							
						}else { // if user does not want to continue
							// do nothing
							System.out.println("User chose not to continue");
							return;
						}
						
					}else {
						addSetMovementToWorkout();
						return;
						
					}
					
				}else {
					
				}
			}
			addSetMovementToWorkout();
			

			
			// System.out.println(currentWorkout);
		});
		addSetButton.setDisable(true);
		GridPane.setConstraints(addSetButton, 6, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		
		
		
		completeWorkoutButton = new Button("Complete Workout");
		completeWorkoutButton.setOnAction(e -> {
			currentWorkoutIndex++;
			//System.out.println("Current workout: " + currentWorkoutIndex);
			if (currentWorkoutIndex == totalWorkouts) { // if all workouts have been entered
				completeLastWorkout();

			} else { // if there are workouts yet to enter
				completeWorkout();
			}
		});
		completeWorkoutButton.setDisable(true);
		GridPane.setConstraints(completeWorkoutButton, 6, 2, 1, 1, HPos.CENTER, VPos.CENTER);

		
		
		
		
		completeCycleButton = new Button("Complete Cycle");
		completeCycleButton.setOnAction(e -> {
			completeCycle();
			controller.setBool(true); // refresh needed in MainWindow
			window.close();
		});
		completeCycleButton.setDisable(true);
		GridPane.setConstraints(completeCycleButton, 6, 3, 1, 1, HPos.CENTER, VPos.CENTER);
		
		
		
		

		ArrayList<Movement> defaultMovements = new ArrayList<Movement>(
				Arrays.asList(new Movement("Squat"), new Movement("Bench"), new Movement("Deadlift")));
		movementChoice = new ChoiceBox<Movement>();
		movementChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				
				//System.out.println(movementChoice.getItems().get((Integer) number2));
			}
		});
		movementChoice.getItems().addAll(defaultMovements); // eventually we want to create a custom name
		movementChoice.setDisable(true);
		GridPane.setConstraints(movementChoice, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		setsChoice = new ChoiceBox<Integer>();
		setsChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		setsChoice.setDisable(true);
		GridPane.setConstraints(setsChoice, 2, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		repsChoice = new ChoiceBox<Integer>();
		repsChoice.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		repsChoice.setDisable(true);
		GridPane.setConstraints(repsChoice, 3, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		/*
		 * have this checkbox on another page, so that the window doesnt even need to
		 * open if you want to use a preset schedule
		 */

		workoutDescription = new TextArea();
		GridPane.setConstraints(workoutDescription, 1, 2, 5, 1, HPos.CENTER, VPos.CENTER);

		cycleDescription = new TextArea();
		GridPane.setConstraints(cycleDescription, 1, 3, 5, 1, HPos.CENTER, VPos.CENTER);

		grid1.getChildren().addAll(cycleNameLabel, totalWorkoutsLabel, metricSelectionLabel, cycleNameInput,
				totalWorkoutsInput, repsChoice, kilogramCheck, poundsCheck, submitCycleButton);
		grid1.setBackground(
				new Background(new BackgroundFill(Color.rgb(169, 169, 169), CornerRadii.EMPTY, Insets.EMPTY)));
		grid2.getChildren().addAll(movementChoice, repsChoice, setsChoice, movementLabel, repsLabel, setsLabel,
				current1RMLabel, percentLabel, completeWorkoutButton, addSetButton, currentMaxInput, percentInput,
				workoutDescription, cycleDescription, completeCycleButton);
		// grid2.setBackground(new Background(new BackgroundFill(Color.rgb(100, 50, 0),
		// CornerRadii.EMPTY, Insets.EMPTY)));

		s1 = new Scene(layout1, 800, 500);
		s1.getStylesheets().add("FitProStyle.css");

		window.setScene(s1);
		window.showAndWait();
	}

	public WindowController getController() {
		return this.controller;
	}
	
	public void setController(WindowController wc) {
		this.controller = wc;
	}
	
	public void addSetMovementToWorkout() {
		Movement movement = new Movement(movementChoice.getValue());
		int numberOfSets = setsChoice.getValue();
		int numberOfReps = repsChoice.getValue();
		double currentMax = Double.parseDouble(currentMaxInput.getText());
		double percent = Double.parseDouble(percentInput.getText());
		

		movement.setCurrent1RM(currentMax);
		movement.setParentWorkout(currentWorkout);
		currentWorkout.addMovement(movement);
		for (int i = 0; i < numberOfSets; i++) {
			Set s = new Set(numberOfReps, currentMax * (percent * 0.01));
			s.setParentMovement(movement);
			movement.addSet(s);
			
		}
		//workoutDescription.setText(currentWorkout.toString().substring(0, 8) + (currentWorkoutIndex + 1)
		//		+ currentWorkout.toString().substring(9));
		workoutDescription.setText("Workout " + (currentWorkoutIndex + 1) + ": " 
				+ currentWorkout.toString().substring(21));
	}
	public void completeWorkout() {
		Workout newWorkout = new Workout();
		workouts.add(newWorkout);
		workoutDescription.clear();
		workoutIndicator.setText("Workout " + (currentWorkoutIndex + 1) + ": ");
		String currentText = cycleDescription.getText();
		currentWorkout = workouts.get(currentWorkoutIndex - 1);
		//cycleDescription.setText(currentText + currentWorkout.toString().substring(0, 8) + currentWorkoutIndex
		//		+ currentWorkout.toString().substring(9));
		cycleDescription.setText(currentText + "Workout " + currentWorkoutIndex + ": "
				+ currentWorkout.toString().substring(21));
	}
	public void completeLastWorkout() {
		completeWorkoutButton.setDisable(true);
		completeCycleButton.setDisable(false);
		addSetButton.setDisable(true);
		movementChoice.setDisable(true);
		setsChoice.setDisable(true);
		repsChoice.setDisable(true);
		currentMaxInput.setDisable(true);
		percentInput.setDisable(true);
		workoutDescription.clear();
		String currentText = cycleDescription.getText();
		currentWorkout = workouts.get(currentWorkoutIndex - 1);
		//cycleDescription.setText(currentText + currentWorkout.toString().substring(0, 8) + currentWorkoutIndex
		//		+ currentWorkout.toString().substring(9));
		cycleDescription.setText(currentText + "Workout " + currentWorkoutIndex + ": "
				+ currentWorkout.toString().substring(21));
	}
	public void completeCycle() {
		c = new Cycle(user.getId(), cycleName, totalWorkouts, workouts);
		for (Workout w : workouts) {
			w.setParentCycle(c);
			System.out.println(w.getParentCycle());
		}
		c.sortWorkouts();
		sqlService = new SQLService();
		sqlService.upsertCycle(c);
		
	}

	@Override
	public void handle(ActionEvent e) {

		if (e.getSource() == checkBox1) {
			if (checkBox1.isSelected()) {

			} else {

			}

		}

	}

	/*
	 * the method below creates a cycle AT THE GIVEN TIME, at the time it was
	 * called, so that new cycles can be created if the user wants to input more
	 * data
	 */
	
}
