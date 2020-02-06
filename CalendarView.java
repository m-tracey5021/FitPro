import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.geometry.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CalendarView extends GridPane {
	
	private Label currentlyFocusedDateLabel;
	private Button nextButton, previousButton;
	private GridPane calendarGrid;
	private Pane calendarPane;
	private ScrollPane scrollPane;
	private StackPane paneForViewTypeSelection, overlayStack;
	private HBox horizontalAxisLabelPane, currentFocusPane, navButtons;
	private VBox verticalAxisLabelPane, currentFocusWithSeparators;
	private BorderPane borderPane;
	private ChoiceBox<String> viewTypeSelection;
	private String currentViewType;
	private WindowController windowController;
	private ArrayList<DateSlot> allDateSlots;
	//private ArrayList<DateNode> allocatedDateNodes;
	//private DateNodeContainer dateNodeContainer;
	private ArrayList<Workout> workouts;
	private Cycle selectedCycle;

	//private ArrayList<DateNode> changedNodes;
	private LocalDate defaultDate, calendarStartDate, startDate, currentlyFocusedDate;
	private DateTimeFormatter dtf;
	private double calendarWidth, edgeWidth, dateSlotWidth, dateSlotHeight;
	private CalendarViewElementContainer elementContainer;
	
	
	
	public CalendarView(double calendarWidth) {
		//calendarWidth -= 100;


		
		this.calendarWidth = calendarWidth;
		this.edgeWidth = calendarWidth / 9;
		//this.setGridLinesVisible(true);
		this.setVgap(10);
		
		allDateSlots = new ArrayList<DateSlot>();
		defaultDate = LocalDate.now();
		
		dtf = DateTimeFormatter.ofPattern("dd/MM//yyyy");
		defaultDate.format(dtf);
		
		currentFocusWithSeparators = new VBox();
		currentFocusWithSeparators.setPrefWidth(calendarWidth);
		
		currentFocusPane = new HBox(); // eventually implement this so that calendar has nav panel
		currentFocusPane.setPrefSize(calendarWidth, 75);
		currentFocusPane.setAlignment(Pos.CENTER_LEFT);
		GridPane.setConstraints(currentFocusPane, 0, 0);
		
		paneForViewTypeSelection = new StackPane();
		paneForViewTypeSelection.setPrefSize(edgeWidth, 75);
		
		//currentViewType = "Week"; // default viewType
		viewTypeSelection = new ChoiceBox<String>();
		viewTypeSelection.getItems().addAll("Week", "Month", "Year");
		viewTypeSelection.setValue("Week");
		viewTypeSelection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
				currentViewType = viewTypeSelection.getItems().get((Integer) newIndex);
				System.out.println("picked from bx: " + viewTypeSelection.getItems().get((Integer) newIndex));
				System.out.println("current: " + currentViewType);
				setUpView(selectedCycle);
			}
		});
		currentViewType = "Week";
		
		
		paneForViewTypeSelection.getChildren().add(viewTypeSelection);
		
		currentlyFocusedDateLabel = new Label("");
		currentlyFocusedDateLabel.setPrefSize(200, 75);
		currentlyFocusedDateLabel.setPadding(new Insets(0, 0, 0, 25));
		
		previousButton = new Button("<< ");
		previousButton.setOnAction(e -> {
			if (currentViewType.equals("Week")) {
				currentlyFocusedDate = currentlyFocusedDate.minusDays(7);

				
			}else if (currentViewType.equals("Month")) {
				currentlyFocusedDate = currentlyFocusedDate.minusMonths(1);
				
			}else if (currentViewType.equals("Year")) {
				currentlyFocusedDate = currentlyFocusedDate.minusYears(1);
			}
			setUpView(selectedCycle);
			
		});
		
		nextButton = new Button(" >>");
		nextButton.setOnAction(e -> {
			if (currentViewType.equals("Week")) {
				currentlyFocusedDate = currentlyFocusedDate.plusDays(7);
				
			}else if (currentViewType.equals("Month")) {
				currentlyFocusedDate = currentlyFocusedDate.plusMonths(1);
				
			}else if (currentViewType.equals("Year")) {
				currentlyFocusedDate = currentlyFocusedDate.plusYears(1);
			}
			setUpView(selectedCycle);
		});
		
		navButtons = new HBox(15);
		navButtons.setAlignment(Pos.CENTER);
		navButtons.getChildren().addAll(previousButton, nextButton);
		
		currentFocusPane.getChildren().addAll(paneForViewTypeSelection, currentlyFocusedDateLabel, navButtons);
		
		Separator horizontalSep1 = new Separator(Orientation.HORIZONTAL);
		Separator horizontalSep2 = new Separator(Orientation.HORIZONTAL);
		
		currentFocusWithSeparators.getChildren().addAll(horizontalSep1, currentFocusPane, horizontalSep2);
		
		horizontalAxisLabelPane = new HBox();
		horizontalAxisLabelPane.setPrefSize(calendarWidth, 30);
		GridPane.setConstraints(horizontalAxisLabelPane, 0, 1);
		
		scrollPane = new ScrollPane();
		scrollPane.setPrefSize(calendarWidth, 275);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVmin(0.0);
		GridPane.setConstraints(scrollPane, 0, 2);
		
		this.getChildren().addAll(currentFocusWithSeparators, horizontalAxisLabelPane, scrollPane);

		
	}
	public LocalDate getCurrentlyFocusedDate() {
		return this.currentlyFocusedDate;
	}
	public String getCurrentViewType() {
		return this.currentViewType;
	}
	/*
	public DateNodeContainer getDateNodeContainer() {
		return this.dateNodeContainer;
	}
	*/
	public ArrayList<DateSlot> getAllDateSlots(){
		return this.allDateSlots;
	}
	public void setCurrentlyFocusedDate(LocalDate date) {
		this.currentlyFocusedDate = date;
	}
	public void setCurrentViewType(String type) {
		this.currentViewType = type;
	}
	/*
	public void setDateNodeContainer(DateNodeContainer container) {
		this.dateNodeContainer = container;
	}
	*/
	public void setWorkouts(ArrayList<Workout> workouts) {
		this.workouts = workouts;
	}

	public void setUpCurrentFocusPane() {
		
		int startYear = currentlyFocusedDate.getYear();
		Month startMonth = currentlyFocusedDate.getMonth();
		int dayOfWeek = currentlyFocusedDate.getDayOfWeek().getValue();
		LocalDate startOfWeek = currentlyFocusedDate.minusDays(dayOfWeek);

		if (currentViewType.equals("Week")) {
			currentlyFocusedDateLabel.setText("Week starting: " + startOfWeek.toString()); 
		}else if (currentViewType.equals("Month")) {
			currentlyFocusedDateLabel.setText("Month: " + startMonth.toString().substring(0, 1) + startMonth.toString().substring(1).toLowerCase() + " " + currentlyFocusedDate.getYear());
		}else if (currentViewType.equals("Year")) {
			currentlyFocusedDateLabel.setText("Year: " + startYear);
		}else {
			currentlyFocusedDateLabel.setText("");
		}
	}
	
	public void setUpHorizontalAxisLabels() {
		ArrayList<String> labels;
		int iterations = 0;
		if (currentViewType.equals("Week") || currentViewType.equals("Month")) {
			iterations = 7;
			labels = new ArrayList<String>(Arrays.asList("S", "M", "T", "W", "T", "F", "S"));
		}else if (currentViewType.equals("Year")) {
			iterations = 12;
			labels = new ArrayList<String>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", 
					"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
		}else {
			System.out.println("Wrong type chosen");
			labels = new ArrayList<String>();
		}
		
		horizontalAxisLabelPane.getChildren().clear();
		Separator sep;
		
		StackPane dummyPane1 = new StackPane();
		dummyPane1.setPrefSize(edgeWidth + 5, 30);
		horizontalAxisLabelPane.getChildren().add(dummyPane1);
		
		
		
		for (int i = 0; i < iterations; i ++) {
			
			Label dayLabel = new Label(labels.get(i));
			//dayLabel.setPadding(new Insets(20, 20, 20, 20));
			StackPane labelPane = new StackPane();
			sep = new Separator(Orientation.VERTICAL);
			//sep.setWidth(20);
			labelPane.setPrefSize(dateSlotWidth, 30);
			labelPane.getChildren().add(dayLabel);
			if (i != iterations - 1) {
				horizontalAxisLabelPane.getChildren().addAll(sep, labelPane);
			}else {
				Separator lastSep = new Separator(Orientation.VERTICAL);
				horizontalAxisLabelPane.getChildren().addAll(sep, labelPane, lastSep);
			}

		}
		StackPane dummyPane2 = new StackPane();
		dummyPane2.setPrefSize(edgeWidth - 5, 30);
		horizontalAxisLabelPane.getChildren().add(dummyPane2);
	}	
	public void setUpVerticalAxisLabels(String type, int rows) {
		verticalAxisLabelPane = new VBox();
		if (type.equals("Week")) {
			for (int i = 0; i < rows; i ++) {
				StackPane pane = new StackPane();
				pane.setPrefSize(edgeWidth, 30);
				Label timeLabel = new Label(i + ":00");
				pane.getChildren().add(timeLabel);
				verticalAxisLabelPane.getChildren().add(pane);
				borderPane.setLeft(verticalAxisLabelPane);

			}
		}else if (type.equals("Month")) {
			for(int i = 0; i < rows; i ++) {
				StackPane pane = new StackPane();
				pane.setPrefSize(edgeWidth, 30);
				Label weekLabel = new Label("Week " + (i + 1));
				pane.getChildren().add(weekLabel);
				verticalAxisLabelPane.getChildren().add(pane);
				borderPane.setLeft(verticalAxisLabelPane);

			}
		}else if (type.equals("Year")){
			for (int i = 0; i < rows; i ++) {
				StackPane pane = new StackPane();
				pane.setPrefSize(edgeWidth, 20);
				Label dateLabel = new Label(Integer.toString(i + 1));
				pane.getChildren().add(dateLabel);
				verticalAxisLabelPane.getChildren().add(pane);
				borderPane.setLeft(verticalAxisLabelPane);

			}
		}else {
			
			System.out.println("Wrong type given to create labels");
		}
		
		
	}
	
	
	public void setUpView(Cycle selectedCycle) {
		
		//System.out.println("Passed startDate: " + startDate);
		this.selectedCycle = selectedCycle;

		//this.workouts = selectedCycle.getWorkouts();
		//calendarPane.getChildren().clear();
		
		
		calendarPane = new Pane();

		
		elementContainer = new CalendarViewElementContainer();
		elementContainer.setCycle(selectedCycle);
		elementContainer.setDateNodesByCycle();
		elementContainer.setCalendarPane(calendarPane);
		
		//allDateSlots = new ArrayList<DateSlot>();
		//calendarGrid = new GridPane();
		//calendarGrid.setGridLinesVisible(true);
		//calendarGrid.getStyleClass().add("scrollPane");
		
		borderPane = new BorderPane();
		
		HBox dummyHBox1 = new HBox();
		HBox dummyHBox2 = new HBox();
		dummyHBox1.setPrefHeight(30);
		dummyHBox2.setPrefHeight(30);
		borderPane.setTop(dummyHBox1);
		borderPane.setBottom(dummyHBox2);
		
		
		//intermediateGrid.getStyleClass().add("scrollPane");
		scrollPane.setContent(borderPane);
		//scrollPane.getStyleClass().add("scrollPane");
		
		
		if (currentViewType.equals("Week")) {
			

			setUpWeekView();
			scrollPane.setVmax((24 * dateSlotHeight) + 60);
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addNonNullWorkouts();
			scrollToFirstWorkout();
			
		}else if (currentViewType.equals("Month")) {
			
			setUpMonthView();
			scrollPane.setVmax((5 * dateSlotHeight) + 60);
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addNonNullWorkouts();
			
		} else if (currentViewType.equals("Year")) {
			
			setUpYearView();
			scrollPane.setVmax((31 * dateSlotHeight) + 60);
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addNonNullWorkouts();
		}

	}
	
	public void setUpWeekView(){
		this.dateSlotWidth = (calendarWidth - (2 * edgeWidth)) / 7;
		this.dateSlotHeight = 30;

		
		
		LocalTime startTime = LocalTime.MIN;
		int dayOfWeek = currentlyFocusedDate.getDayOfWeek().getValue();
		if (dayOfWeek == 7) {
			dayOfWeek = 1;
		}else {
			dayOfWeek += 1;
		}
		calendarStartDate = currentlyFocusedDate.minusDays(dayOfWeek - 1);

		setUpVerticalAxisLabels("Week", 24);
		
		double x = 0.0;
		double y = 0.0;
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 24; j ++) {
				
				LocalDate thisDate = calendarStartDate.plusDays(i);
				LocalTime thisTime = startTime.plusHours(j);
				thisDate.format(dtf);
				
				DateSlot dateSlot = new DateSlot(thisDate, thisTime, dateSlotWidth, dateSlotHeight, x, y, elementContainer);
				
				elementContainer.addDateSlot(dateSlot);
				dateSlot.setUpDateSlot();
				//calendarGrid.add(dateSlot, i, j);
				//dateSlot.relocate(x, y);
				y += dateSlotHeight;
			}
			x += dateSlotWidth;
			y = 0.0;
		}
		
		
		//overlayPane.getChildren().add(new Rectangle(10, 10, 10, 10));
		//overlayStack.getChildren().addAll(calendarGrid, overlayPane);
		//borderPane.setCenter(overlayStack);
		borderPane.setCenter(calendarPane);
		//borderPane.setCenter(calendarGrid);
		

	}
	
	public void setUpMonthView() {
		this.dateSlotWidth = (calendarWidth - (2 * edgeWidth)) / 7;
		this.dateSlotHeight = 30;

		
		Month month = currentlyFocusedDate.getMonth();
		int numberOfDateNodes = month.length(currentlyFocusedDate.isLeapYear());
		int dayOfMonth = currentlyFocusedDate.getDayOfMonth();
		calendarStartDate = currentlyFocusedDate.minusDays(dayOfMonth - 1);
		int startDay = calendarStartDate.getDayOfWeek().getValue();
		if (startDay == 7) {
			startDay = 1;
		}else {
			startDay += 1;
		}
		
		numberOfDateNodes += startDay - 1; // to count the number of datenodes including month before hand
		
		int cols = 7;
		double rows = Math.ceil(Double.valueOf(numberOfDateNodes) / 7.0);
		
		setUpVerticalAxisLabels("Month", (int)rows);
		
		double x = 0.0;
		double y = 0.0;
		
		for (int i = 0; i < numberOfDateNodes; i ++) {
			
			if (i < (startDay - 1)) {
				Region dummyRegion = new Region();
				dummyRegion.setPrefSize(dateSlotWidth, dateSlotHeight);
				dummyRegion.relocate(x, y);
				calendarPane.getChildren().add(dummyRegion);
				//calendarGrid.add(dummyRegion, col, row);
				//col ++;
				x += dateSlotWidth;
			}else {
				LocalDate thisDate = calendarStartDate.plusDays(i - startDay + 1);
				LocalTime thisTime = LocalTime.NOON;
				thisDate.format(dtf);
				
				DateSlot dateSlot = new DateSlot(thisDate, thisTime, dateSlotWidth, dateSlotHeight, x, y, elementContainer);
				
				elementContainer.addDateSlot(dateSlot);
				dateSlot.setUpDateSlot();
				//calendarGrid.add(dateSlot, col, row);
				//overlayPane.getChildren().add(dateSlot);
				
				
				x += dateSlotWidth;
				if (x == dateSlotWidth * 7) {
					x = 0.0;
					y += dateSlotHeight;
				}
			}	
		}

		borderPane.setCenter(calendarPane);
	}
	
	public void setUpYearView() {
		this.dateSlotWidth = (calendarWidth - (2 * edgeWidth)) / 12;
		this.dateSlotHeight = 20;
		
		
		int startingMonthNumber = currentlyFocusedDate.getMonthValue();
		LocalDate pointerDate = currentlyFocusedDate.minusMonths(startingMonthNumber - 1);
		LocalDate calendarStartDate = pointerDate.minusDays(pointerDate.getDayOfMonth() - 1); // this is the start of the year
		int lengthOfMonth = pointerDate.lengthOfMonth();
		ArrayList<Integer> lengthsOfMonths = new ArrayList<Integer>();
		lengthsOfMonths.add(lengthOfMonth);
		pointerDate = pointerDate.plusMonths(1);
		for (int i = 0; i < 11; i ++) {
			
			lengthOfMonth = pointerDate.lengthOfMonth();
			lengthsOfMonths.add(lengthOfMonth);
			pointerDate = pointerDate.plusMonths(1);
		}
		
		double x = 0.0;
		double y = 0.0; 

		setUpVerticalAxisLabels("Year", 31);
		int dateCounter = 0;
		for (int i = 0; i < lengthsOfMonths.size(); i ++) {
			int monthLength = lengthsOfMonths.get(i);
			for (int j = 0; j < monthLength; j ++) {
				
				LocalDate thisDate = calendarStartDate.plusDays(dateCounter);
				LocalTime thisTime = LocalTime.NOON;
				thisDate.format(dtf);
				
				DateSlot dateSlot = new DateSlot(thisDate, thisTime, dateSlotWidth, dateSlotHeight, x, y, elementContainer);
				
				elementContainer.addDateSlot(dateSlot);
				dateSlot.setUpDateSlot();
				//overlayPane.getChildren().add(dateSlot);
				//calendarGrid.add(dateSlot, i + 1, j + 1);
				y += dateSlotHeight;
				dateCounter ++;
				
			}
			x += dateSlotWidth;
			y = 0.0;
		}

		borderPane.setCenter(calendarPane);

	}

	
	
	
	public void addNonNullWorkouts() {
		if (elementContainer.getSelectedCycle() != null) {
			for (Workout workout : elementContainer.getSelectedCycle().getWorkouts()) {
				if (workout.getDateTime() != null) {
					LocalDate date = workout.getDate();
					LocalTime time = workout.getTime();
					DateNode nodeToAdd = elementContainer.getDateNodeByWorkout(workout);
					for (DateSlot dateSlot : elementContainer.getDateSlots()) {
						System.out.println("SCHEMAZZED");
						if (currentViewType.equals("Week")) {
							if (workout.getDateTime().equals(dateSlot.getDateTime())) {
								nodeToAdd.setSize(dateSlotWidth - 4, dateSlotHeight - 4);
								nodeToAdd.setPos(dateSlot.getPos()[0] + 2, dateSlot.getPos()[1] + 2);
								calendarPane.getChildren().add(nodeToAdd);
							}
						}else {
							if (workout.getDate().equals(dateSlot.getDate())) {
								nodeToAdd.setSize(dateSlotWidth - 4, dateSlotHeight - 4);
								nodeToAdd.setPos(dateSlot.getPos()[0] + 2, dateSlot.getPos()[1] + 2);
								calendarPane.getChildren().add(nodeToAdd);
							}
						}
					}
				}
			}
		}
		
	}

	public void scrollToFirstWorkout() {
		calendarPane.applyCss();
		calendarPane.layout();
		if (elementContainer.getSelectedCycle() != null) {
			//elementContainer.getSelectedCycle().sortWorkouts();
			DateNode firstNode = elementContainer.getDateNodeByWorkout(elementContainer.getSelectedCycle().getWorkouts().get(0));
			LocalTime time = firstNode.getWorkout().getTime();
			if (time != null) {
				int numberOfVDateSlots = Integer.parseInt(time.toString().substring(0, 2));
				double verticalValue = numberOfVDateSlots * dateSlotHeight;
		        scrollPane.setVvalue(verticalValue);
			}
		}
		
	}
	
	public void saveChanges(Cycle selectedCycle) {
		if (elementContainer.getSelectedCycle() != null) {
			SQLService sql = new SQLService();
			//elementContainer.getSelectedCycle().sortWorkouts();
			
			LocalDate firstDate = elementContainer.getSelectedCycle().getWorkouts().get(0).getDate();
			LocalDate endDate = elementContainer.getSelectedCycle().getWorkouts().get(elementContainer.getSelectedCycle().getWorkouts().size() - 1).getDate();
			
			LocalDate dates[] = {firstDate, endDate};
			selectedCycle.setDates(dates);

			sql.upsertCycle(selectedCycle);
			
		}else { // if new cycle with no name, just adding workouts
			
		}
		

		
		
			
	}
	
	
}
