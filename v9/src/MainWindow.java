import java.util.*;
import java.util.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.geometry.*;

public class MainWindow implements EventHandler<ActionEvent> {

	private User currentUser;
	private Stage window;
	private Scene s1;
	private GridPane g1, g2;
	private HBox bottomPaneLeft, selectedCycleAllocationOptions, bottomPaneRight;
	private VBox selectedCycleVBox;
	private ImageView imageView;
	private Image logo;
	private CalendarView calendar;
	//private ScrollPane scrollPane;
	private TreeView<String> cycleTree;
	private Map<TreeItem<String>, CycleObject> treeItemToObjectMap;
	private StackPane paneForSelectedCycle, paneForSelectedCycleInfo;
	private Label cyclesLabel, selectedCycleLabel, selectedCycleInfo;
	private Button selectButton, createCycleButton, allocateWorkoutButton, addNewWorkoutButton, saveButton, resetAllButton;
	private String periodSelection;

	private ArrayList<Cycle> cycles;
	private Cycle selectedCycle;
	private DateNodeContainer dateNodeContainer;
	private Workout selectedWorkout;

	private WindowController windowController;

	private Month defaultMonth;
	private LocalDate defaultDate, currentDatePointer;
	private DateTimeFormatter dtf;

	private SQLService sqlService;

	public MainWindow(User user) {
		currentUser = user;
		cycles = new ArrayList<Cycle>();
		selectedCycle = null;
		dateNodeContainer = new DateNodeContainer();
		windowController = new WindowController();
		dtf = DateTimeFormatter.ofPattern("dd/MM//yyyy");
		sqlService = new SQLService();
		defaultDate = LocalDate.now();
		defaultDate.format(dtf);
		defaultMonth = defaultDate.getMonth();
		treeItemToObjectMap = new HashMap<TreeItem<String>, CycleObject>();
		readyCycles();
		readyDate();

		window = new Stage();

		g1 = new GridPane();
		//g1.setGridLinesVisible(true);
		g1.setPadding(new Insets(10, 10, 10, 10));
		g1.setHgap(20);
		g1.setVgap(10);
		
		
		/*
		 * g2 = new GridPane(); g2.setPadding(new Insets(10, 10, 10, 10));
		 * g2.setHgap(20); g2.setVgap(10);
		 */
		logo = new Image("squiggle2.png");
		imageView = new ImageView();
		imageView.setImage(logo);
		imageView.setFitWidth(200);
		imageView.setFitHeight(175);
		
		GridPane.setConstraints(imageView, 0, 0, 1, 2, HPos.CENTER, VPos.CENTER);
		

		cyclesLabel = new Label("Cycles: ");
		AnchorPane cyclesLabelPane = new AnchorPane();
		AnchorPane.setTopAnchor(cyclesLabel, 85.0);
		AnchorPane.setLeftAnchor(cyclesLabel, 0.0);
		cyclesLabelPane.setPrefHeight(100);
		cyclesLabelPane.getChildren().add(cyclesLabel);
		// GridPane.setMargin(cyclesLabel, new Insets(75, 0, 0, 0));
		//cyclesLabel.setPadding(new Insets(50, 0, 0, 0));
		GridPane.setConstraints(cyclesLabelPane, 0, 2);

		cycleTree = new TreeView<String>();
		cycleTree.setPrefHeight(275);
		setUpCycleTree();
		//cycleTree.getItems().addAll(cycles);
		GridPane.setConstraints(cycleTree, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);

		selectButton = new Button("Select");
		selectButton.setOnAction(e -> {

			if (cycles.size() != 0) {
				selectedCycle = (Cycle) treeItemToObjectMap.get(cycleTree.getSelectionModel().getSelectedItem());
				System.out.println(selectedCycle);
				//readyNodes();
				readyDate();
				
				selectedCycleLabel.setText("Cycle: " + selectedCycle.getCycleName());

				
				calendar.setCurrentlyFocusedDate(currentDatePointer);
				//calendar.setDateNodeContainer(dateNodeContainer);

				calendar.setUpView(selectedCycle);
				//pickCalendarView(currentDatePointer);
				//previousButton.setDisable(false);
				//nextButton.setDisable(false);
				allocateWorkoutButton.setDisable(false);
				addNewWorkoutButton.setDisable(false);
				saveButton.setDisable(false);
				resetAllButton.setDisable(false);

			} else {
				System.out.println("No cycles!");
			}

		});

		createCycleButton = new Button("Create New Cycle");
		createCycleButton.setOnAction(e -> {
			WindowController windowController = new WindowController();
			
			CycleCreationWindow ccw = new CycleCreationWindow("New Cycle", currentUser, windowController);
			if (windowController.getBool() == true) {
				readyCycles(); // refresh the list
				System.out.println(cycles);
				//cycleTree = new TreeView<String>();
				setUpCycleTree();
				System.out.println("refreshed");
			}
			
			//NewCyclePopupModal createCycleModal = new NewCyclePopupModal("Create new cycle", "Enter cycle name", windowController);
			//if (windowController.getString() != null) {
			//	
			//}
		});

		bottomPaneLeft = new HBox(15);
		bottomPaneLeft.getChildren().addAll(selectButton, createCycleButton);
		bottomPaneLeft.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(bottomPaneLeft, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);

		selectedCycleVBox = new VBox(15);
		selectedCycleAllocationOptions = new HBox(10);
		selectedCycleAllocationOptions.setAlignment(Pos.BASELINE_CENTER);

		paneForSelectedCycle = new StackPane();
		selectedCycleLabel = new Label("Unsaved Cycle");
		paneForSelectedCycle.getChildren().add(selectedCycleLabel);
		
		allocateWorkoutButton = new Button("Allocate Workout");
		allocateWorkoutButton.setOnAction(e -> {
			
		});
		allocateWorkoutButton.setDisable(true);
		
		addNewWorkoutButton = new Button("Add New Workout");
		addNewWorkoutButton.setOnAction(e -> {
			
		});
		addNewWorkoutButton.setDisable(true);
		
		selectedCycleAllocationOptions.getChildren().addAll(allocateWorkoutButton, addNewWorkoutButton);
		selectedCycleVBox.getChildren().addAll(paneForSelectedCycle, selectedCycleAllocationOptions);
		GridPane.setConstraints(selectedCycleVBox, 1, 0);
		

		calendar = new CalendarView(700);
		//calendar.setDateNodeContainer(dateNodeContainer);
		calendar.setCurrentlyFocusedDate(defaultDate);
		calendar.setCurrentViewType("Week");
		calendar.setUpView(selectedCycle);
		//calendar.setUpDefaultView();
		GridPane.setConstraints(calendar, 1, 2, 1, 2);
		
		
		

		
		
		//calendar.setUpView("Week", defaultDate);
		


		saveButton = new Button("Save Changes");
		saveButton.setOnAction(e -> {
			calendar.saveChanges(selectedCycle);
			readyCycles();
			//readyNodes();
			readyDate();
			//System.out.println(currentDatePointer);
			calendar.setCurrentlyFocusedDate(currentDatePointer);
			calendar.setCurrentViewType("Week");
			calendar.setUpView(selectedCycle);
		});
		saveButton.setDisable(true);

		resetAllButton = new Button("Reset All");
		resetAllButton.setOnAction(e -> {
			resetAll(selectedCycle);
			calendar.setCurrentlyFocusedDate(defaultDate);
			calendar.setCurrentViewType("Week");
			calendar.setUpView(selectedCycle);
		});
		resetAllButton.setDisable(true);

		bottomPaneRight = new HBox(15);
		bottomPaneRight.getChildren().addAll(saveButton, resetAllButton);
		bottomPaneRight.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(bottomPaneRight, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);

		g1.getChildren().addAll(imageView, cyclesLabelPane, cycleTree, bottomPaneLeft, selectedCycleVBox, calendar, bottomPaneRight);
		// g2.getChildren().addAll(workoutsView, topRightPane, scrollPane,
		// bottomPaneRight);
		//g1.applyCss();
		//g1.layout();
		//System.out.println("currentFocusPane width: " + currentFocusPane.getWidth());

		s1 = new Scene(g1);
		s1.getStylesheets().add("FitProStyle.css");
		//window.setHeight(600);
		window.setScene(s1);
		window.show();
		
	}

	public void readyCycles() {
		cycles.clear();
		cycles = sqlService.getCyclesByUser(currentUser.getId());
		for (Cycle c : cycles) {
			c.sortWorkouts();
		}
		
	}
	public void readyDate() {
		if (selectedCycle != null) {
			if(selectedCycle.getDates()[0] != null) {
				currentDatePointer = selectedCycle.getDates()[0];
			}else {
				currentDatePointer = defaultDate;
			}
			
		}else {
			currentDatePointer = defaultDate;
		}
		
	}
	/*
	public void readyNodes() {
		if (selectedCycle != null) {
			dateNodeContainer.resetAllNodes();
			dateNodeContainer.resetAllocatedNodes();
			for (Workout workout : selectedCycle.getWorkouts()) {
				DateNode newNode = new DateNode(workout);
				
				newNode.addPopup();
				newNode.makeDraggable(windowController);
				if (workout.getDate() == null) {
					dateNodeContainer.addToUnallocatedNodes(newNode);
				}else {
					dateNodeContainer.addToAllocatedNodes(newNode);
				}
				dateNodeContainer.addToAllNodes(newNode);
			}
		}
	}
	*/
	

	public void setUpCycleTree(){
		if (cycleTree.getRoot() != null) {
			cycleTree.getRoot().getChildren().clear();
		}
		TreeItem<String> root = new TreeItem<String>();
		for (Cycle cycle : cycles) {
			TreeItem<String> cycleItem = new TreeItem<String>(cycle.getCycleName());
			treeItemToObjectMap.put(cycleItem, cycle);
			for(Workout workout : cycle.getWorkouts()) { 
				String itemString = workout.basicToString();
				TreeItem<String> workoutItem = new TreeItem<String>(itemString);
				treeItemToObjectMap.put(workoutItem, workout);
				for(Movement movement : workout.getMovements()) {
					TreeItem<String> movementItem = new TreeItem<String>(movement.getName());
					treeItemToObjectMap.put(movementItem, movement);
					for(Set set : movement.getSets()) {
						TreeItem<String> setItem = new TreeItem<String>(set.toString());
						treeItemToObjectMap.put(setItem, set);
						movementItem.getChildren().add(setItem);
					}
					workoutItem.getChildren().add(movementItem);
				}
				cycleItem.getChildren().add(workoutItem);
			}
			root.getChildren().add(cycleItem);
		}
		cycleTree.setRoot(root);
		cycleTree.setShowRoot(false);
	}
	


	public void resetAll(Cycle selectedCycle) {
		if (selectedCycle != null) {
			LocalDate dates[] = { null, null };
			selectedCycle.setDates(dates);
			selectedCycle.setHasDates(false);
			for (Workout w : selectedCycle.getWorkouts()) {
				w.setDate(null);
				w.setTime(null);

			}
			sqlService.upsertCycle(selectedCycle);
		}

		readyCycles();
		readyDate();
		dateNodeContainer.resetAllLabels();
		dateNodeContainer.resetAllPopups();

	}
	
	@Override
	public void handle(ActionEvent e) {

	}
}
