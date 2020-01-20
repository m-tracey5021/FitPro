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
	private HBox workoutsView, currentFocusPane, navButtons, weekDaysPane, bottomPaneLeft, bottomPaneRight;
	private ImageView imageView;
	private Image logo;
	private VBox radioButtons;
	private CalendarView calendar;
	private StackPane defaultView;
	//private ScrollPane scrollPane;
	private ListView<Cycle> cycleList;
	private Label cyclesLabel, currentlyFocusedDate;
	private Button selectButton, createCycleButton, nextButton, previousButton, saveButton, resetAllButton;
	private ToggleGroup YMWSelection;
	private RadioButton yearSelection, monthSelection, weekSelection;
	private String periodSelection;

	private ArrayList<Cycle> cycles;
	private Cycle selectedCycle;
	private DateNodeContainer nodeContainer;
	private Workout selectedWorkout;

	private WindowController windowController;

	private Month defaultMonth;
	private LocalDate defaultDate, startDate, currentDatePointer;
	private DateTimeFormatter dtf;

	private SQLService sqlService;

	public MainWindow(User user) {
		currentUser = user;
		cycles = new ArrayList<Cycle>();
		selectedCycle = null;
		nodeContainer = new DateNodeContainer();
		windowController = new WindowController();
		dtf = DateTimeFormatter.ofPattern("dd/MM//yyyy");
		sqlService = new SQLService();
		defaultDate = LocalDate.now();
		defaultDate.format(dtf);
		defaultMonth = defaultDate.getMonth();
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

		cycleList = new ListView<Cycle>();
		cycleList.setPrefHeight(275);
		cycleList.getItems().addAll(cycles);
		GridPane.setConstraints(cycleList, 0, 3, 1, 1, HPos.CENTER, VPos.CENTER);

		selectButton = new Button("Select");
		selectButton.setOnAction(e -> {

			if (cycles.size() != 0) {
				selectedCycle = cycleList.getSelectionModel().getSelectedItem();
				readyDate();
				g1.getChildren().remove(defaultView);
				if (!(g1.getChildren().contains(calendar))) {
					g1.getChildren().add(calendar);
				}
				
				pickCalendarView(currentDatePointer);
				previousButton.setDisable(false);
				nextButton.setDisable(false);
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
				cycleList.getItems().clear();
				cycleList.getItems().addAll(cycles);
				System.out.println("refreshed");
			}
		});

		bottomPaneLeft = new HBox(50);
		bottomPaneLeft.getChildren().addAll(selectButton, createCycleButton);
		bottomPaneLeft.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(bottomPaneLeft, 0, 4, 1, 1, HPos.CENTER, VPos.CENTER);

		workoutsView = new HBox(10);
		workoutsView.setPrefHeight(50);
		//workoutsView.setStyle("-fx-border-style: solid inside");
		GridPane.setConstraints(workoutsView, 1, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		setUpWorkoutsView();
		
		/*
		 * have a top panel with all the workouts to drag onto the calendarViewer
		 */
		YMWSelection = new ToggleGroup();

		radioButtons = new VBox(10);
		
		yearSelection = new RadioButton("Year View");
		yearSelection.setToggleGroup(YMWSelection);
		
		monthSelection = new RadioButton("Month View");
		monthSelection.setToggleGroup(YMWSelection);

		weekSelection = new RadioButton("Week View");
		weekSelection.setToggleGroup(YMWSelection);
		weekSelection.setSelected(true);
		

		radioButtons.getChildren().addAll(yearSelection, monthSelection, weekSelection);

		previousButton = new Button("<< ");
		previousButton.setOnAction(e -> {
			if (YMWSelection.getSelectedToggle() == monthSelection) {
				//calendar.previousTerm("Month");
				//scrollPane.setContent(calendar);
				pickCalendarView(currentDatePointer.minusMonths(1));
				currentDatePointer = currentDatePointer.minusMonths(1);
			}else if (YMWSelection.getSelectedToggle() == weekSelection) {
				//calendar.previousTerm("Week");
				//scrollPane.setContent(calendar);
				pickCalendarView(currentDatePointer.minusDays(7));
				currentDatePointer = currentDatePointer.minusDays(7);
			}
			
			
		});
		previousButton.setDisable(true);
		
		nextButton = new Button(" >>");
		nextButton.setOnAction(e -> {
			if (YMWSelection.getSelectedToggle() == monthSelection) {
				//calendar.nextTerm("Month");
				//scrollPane.setContent(calendar);
				pickCalendarView(currentDatePointer.plusMonths(1));
				currentDatePointer = currentDatePointer.plusMonths(1);
			}else if (YMWSelection.getSelectedToggle() == weekSelection) {
				//calendar.nextTerm("Week");
				//scrollPane.setContent(calendar);
				pickCalendarView(currentDatePointer.plusDays(7));
				currentDatePointer = currentDatePointer.plusDays(7);
			}
			
		});
		nextButton.setDisable(true);

		navButtons = new HBox(25);
		navButtons.setAlignment(Pos.CENTER);
		navButtons.getChildren().addAll(previousButton, nextButton);
		
		//GridPane.setConstraints(topRightPane, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);

		currentFocusPane = new HBox();
		setUpCurrentFocusPane("null", defaultDate); // this just adds radio buttons
		GridPane.setConstraints(currentFocusPane, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
		GridPane.setMargin(currentFocusPane, new Insets(10, 0, 10, 0));

		YMWSelection.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> value, Toggle oldTog, Toggle newTog) {
				readyDate();
				if (selectedCycle != null) { // if cycle is selected
					pickCalendarView(currentDatePointer); // startDate must be set by this point under the cycle selection button
				}else { // if no cycle selected
					//setUpDefaultView();
				}
				
				
			}
		});
		
		//weekDaysPane = new HBox();
		//weekDaysPane.setPrefSize(630, 25);
		//setUpWeekDaysPane();
		//GridPane.setConstraints(weekDaysPane, 1, 2);

		calendar = new CalendarView(windowController, nodeContainer, 700);
		//calendar.setUpDefaultView();
		GridPane.setConstraints(calendar, 1, 2, 1, 2);
		
		
		
		setUpDefaultView();
		
		
		//calendar.setUpView("Week", defaultDate);
		
		
		//scrollPane = new ScrollPane();
		//scrollPane.setPrefHeight(200);
		//System.out.println(scrollPane.getWidth());
		//scrollPane.setVmin(0.0);
		//scrollPane.setVmax(500.0);
		//scrollPane.setContent(calendar);
		//GridPane.setConstraints(scrollPane, 1, 3);
		
		//setUpDefaultCalendarView();

		saveButton = new Button("Save Changes");
		saveButton.setOnAction(e -> {
			calendar.saveChanges(selectedCycle);
			readyCycles();
			readyDate();
			//System.out.println(currentDatePointer);
			pickCalendarView(currentDatePointer);
		});
		saveButton.setDisable(true);

		resetAllButton = new Button("Reset All");
		resetAllButton.setOnAction(e -> {
			resetAll(selectedCycle);
			pickCalendarView(defaultDate);
		});
		resetAllButton.setDisable(true);

		bottomPaneRight = new HBox(50);
		bottomPaneRight.getChildren().addAll(saveButton, resetAllButton);
		bottomPaneRight.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(bottomPaneRight, 1, 4, 1, 1, HPos.CENTER, VPos.CENTER);

		g1.getChildren().addAll(imageView, cyclesLabelPane, cycleList, workoutsView, currentFocusPane,
				bottomPaneLeft, bottomPaneRight);
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
		
		//System.out.println("currentFocusPane width: " + currentFocusPane.getWidth());
		setSizing(); // to be able to get the height and width of things the window has to be shown first
		//System.out.println(scrollPane.getWidth());
	}
	public void setSizing() {
		
	}

	public void readyCycles() {
		cycles.clear();
		cycles = sqlService.getCyclesByUser(currentUser.getId());
		
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
	public void setUpDefaultView() {
		defaultView = new StackPane();
		Label noCycleLabel = new Label("Select a Cycle to get Started");
		noCycleLabel.getStyleClass().clear();
		noCycleLabel.getStyleClass().add("defaultLabel");
		defaultView.getStyleClass().clear();
		defaultView.getStyleClass().add("defaultView");
		defaultView.getChildren().add(noCycleLabel);
		defaultView.setPrefWidth(700);
		
		GridPane.setConstraints(defaultView, 1, 3);
		g1.getChildren().add(defaultView);
		//double scrollHeight = scrollPane.getHeight();
		//double scrollWidth = scrollPane.getWidth();
		//System.out.println(scrollHeight);
		
		
		//AnchorPane.setRightAnchor(noCycleLabel, 315.0);
		//AnchorPane.setTopAnchor(noCycleLabel, 100.0);
		//AnchorPane.setBottomAnchor(noCycleLabel, 100.0);
		
		
		
		AnchorPane.setLeftAnchor(noCycleLabel, 315.0);
		AnchorPane.setTopAnchor(noCycleLabel, 100.0);
		//centerInAnchorPane(noCycleLabel, 630, 200);
		

		
	}
	public void pickCalendarView(LocalDate date) {
		if (YMWSelection.getSelectedToggle() == yearSelection) {
			setUpCurrentFocusPane("Year", date);
			calendar.setUpView("Year", date);
			setUpWorkoutsView();
		}else if (YMWSelection.getSelectedToggle() == monthSelection) {
			setUpCurrentFocusPane("Month", date);
			calendar.setUpView("Month", date);
			setUpWorkoutsView();
		} else if (YMWSelection.getSelectedToggle() == weekSelection) {
			setUpCurrentFocusPane("Week", date);
			calendar.setUpView("Week", date);
			setUpWorkoutsView();

		}
	}

	public void setUpWorkoutsView() {
		workoutsView.getChildren().clear();
		nodeContainer.resetAllNodes();
		nodeContainer.resetAllocatedNodes();
		if (selectedCycle != null) {
			selectedCycle.sortWorkouts();
			for (Workout w : selectedCycle.getWorkouts()) {
				//System.out.println("workout number: " + w.getWorkoutNumber());
				//System.out.println("parent cycle: " + w.getParentCycle());
				DateNode newNode = new DateNode(w);
				nodeContainer.addToAllNodes(newNode);
				newNode.addPopup();
				newNode.makeDraggable(windowController);
				
				
				if (w.getDate() == null) { // if no allocated date, add the node to the workoutsView
					workoutsView.getChildren().add(newNode);
				} else { // else add it to the calendar in its place
					//System.out.println("WORKOUT HAS DATE");
					if (YMWSelection.getSelectedToggle() == monthSelection) {
						calendar.addAllocatedWorkout(newNode, "Month");
					} else if (YMWSelection.getSelectedToggle() == weekSelection) {
						calendar.addAllocatedWorkout(newNode, "Week");

					}
					
				}

			}
			calendar.scrollToFirstWorkout();
			
		}
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
		selectedCycle.sortWorkouts();
		
		nodeContainer.resetAllLabels();
		nodeContainer.resetAllPopups();
		readyCycles();
		readyDate();

	}
	public <T extends Region> void centerInAnchorPane(T region, double width, double height) {
		region.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(region, width / 2);
		AnchorPane.setRightAnchor(region, width / 2);
		AnchorPane.setTopAnchor(region, height / 2);
		AnchorPane.setBottomAnchor(region, height / 2);
		
	}
	/*
	public void setUpWeekDaysPane() {
		weekDaysPane.getChildren().clear();
		AnchorPane dummyPane1 = new AnchorPane();
		Region dummyRegion1 = new Region();
		dummyPane1.setPrefSize(70, 25);
		centerInAnchorPane(dummyRegion1, 70, 25);
		dummyPane1.getChildren().add(dummyRegion1);
		
		weekDaysPane.getChildren().add(dummyPane1);
		String days[] = {"S", "M", "T", "W", "T", "F", "S"};
		for (int i = 0; i < 7; i ++) {
			AnchorPane labelPane = new AnchorPane();
			Label dayLabel = new Label(days[i]);
			labelPane.setPrefSize(70, 25);
			centerInAnchorPane(dayLabel, 70, 25);
			labelPane.getChildren().add(dayLabel);
			//dayLabel.setPadding(new Insets(50, 0, 0, 40));
			weekDaysPane.getChildren().add(labelPane);
		}
		AnchorPane dummyPane2 = new AnchorPane();
		Region dummyRegion2 = new Region();
		dummyPane2.setPrefSize(70, 25);
		centerInAnchorPane(dummyRegion2, 70, 25);
		dummyPane2.getChildren().add(dummyRegion2);

		weekDaysPane.getChildren().add(dummyPane2);
	}
	*/

	public void setUpCurrentFocusPane(String type, LocalDate date) {
		currentFocusPane.getChildren().clear();
		currentFocusPane.getStyleClass().clear();
		currentFocusPane.getStyleClass().add("currentDatePanel");
		currentFocusPane.setAlignment(Pos.CENTER_LEFT);
		
		radioButtons.setPrefSize(115, 100);
		radioButtons.getStyleClass().clear();
		radioButtons.getStyleClass().add("radioButtons");
		radioButtons.setAlignment(Pos.CENTER_LEFT);
		
		int startYear = date.getYear();
		Month startMonth = date.getMonth();
		int dayOfWeek = date.getDayOfWeek().getValue();
		LocalDate startOfWeek = date.minusDays(dayOfWeek);
		
		//Line sep = new Line(100, 0, 100, 50);
		//Line underline = new Line(0, 0, 100, 0);
		
		//radioButtons.getChildren().add(underline);
		
		if (type.equals("Week")) {
			currentlyFocusedDate = new Label("Week starting: " + startOfWeek.toString());
		}else if (type.equals("Month")) {
			currentlyFocusedDate = new Label("Month: " + startMonth.toString().substring(0, 1) + startMonth.toString().substring(1).toLowerCase() + " " + date.getYear());
		}else if (type.equals("Year")) {
			currentlyFocusedDate = new Label("Year: " + startYear);
		}else {
			currentlyFocusedDate = new Label("");
		}
		currentlyFocusedDate.setPrefSize(200, 50);
		currentlyFocusedDate.setPadding(new Insets(0, 0, 0, 25));
		
		
		
		currentFocusPane.getChildren().addAll(radioButtons, currentlyFocusedDate, navButtons);
		//currentFocusPane.applyCss();
		//currentFocusPane.layout();
		//System.out.println("navButtons width: " + navButtons.getWidth());
		/*
		 * Here put the currently focused week and month, and put the days within the calendar again like
		 * they are in the month view
		 */
		
	}

	@Override
	public void handle(ActionEvent e) {

	}
}
