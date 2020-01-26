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
	private Pane overlayPane;
	private ScrollPane scrollPane;
	private StackPane paneForViewTypeSelection, overlayStack;
	private HBox horizontalAxisLabelPane, currentFocusPane;
	private VBox verticalAxisLabelPane, currentFocusWithSeparators;
	private BorderPane borderPane;
	private ChoiceBox<String> viewTypeSelection;
	private String currentViewType;
	private WindowController windowController;
	private ArrayList<DateSlot> allDateSlots;
	//private ArrayList<DateNode> allocatedDateNodes;
	private DateNodeContainer dateNodeContainer;

	//private ArrayList<DateNode> changedNodes;
	private LocalDate defaultDate, calendarStartDate, startDate, currentlyFocusedDate;
	private DateTimeFormatter dtf;
	private double calendarWidth, edgeWidth, dateSlotWidth, dateSlotHeight;
	
	
	
	public CalendarView(WindowController windowController, double calendarWidth) {
		//calendarWidth -= 100;
		this.windowController = windowController;
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
				setUpView();
			}
		});
		
		
		paneForViewTypeSelection.getChildren().add(viewTypeSelection);
		
		currentlyFocusedDateLabel = new Label("");
		currentlyFocusedDateLabel.setPrefSize(200, 75);
		currentlyFocusedDateLabel.setPadding(new Insets(0, 0, 0, 25));
		
		previousButton = new Button("<< ");
		previousButton.setOnAction(e -> {
			if (currentViewType.equals("Week")) {
				currentlyFocusedDate.minusDays(7);
				
			}else if (currentViewType.equals("Month")) {
				currentlyFocusedDate.minusMonths(1);
				
			}else if (currentViewType.equals("Year")) {
				currentlyFocusedDate.minusYears(1);
			}
			
			
		});
		
		nextButton = new Button(" >>");
		nextButton.setOnAction(e -> {
			if (currentViewType.equals("Week")) {
				currentlyFocusedDate.plusDays(7);
				
			}else if (currentViewType.equals("Month")) {
				currentlyFocusedDate.plusMonths(1);
				
			}else if (currentViewType.equals("Year")) {
				currentlyFocusedDate.plusYears(1);
			}
			
		});
		
		currentFocusPane.getChildren().addAll(paneForViewTypeSelection, currentlyFocusedDateLabel);
		
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
		GridPane.setConstraints(scrollPane, 0, 2);
		
		this.getChildren().addAll(currentFocusWithSeparators, horizontalAxisLabelPane, scrollPane);

		
	}
	public LocalDate getCurrentlyFocusedDate() {
		return this.currentlyFocusedDate;
	}
	public String getCurrentViewType() {
		return this.currentViewType;
	}
	public DateNodeContainer getDateNodeContainer() {
		return this.dateNodeContainer;
	}
	public ArrayList<DateSlot> getAllDateSlots(){
		return this.allDateSlots;
	}
	public void setCurrentlyFocusedDate(LocalDate date) {
		this.currentlyFocusedDate = date;
	}
	public void setCurrentViewType(String type) {
		this.currentViewType = type;
	}
	public void setDateNodeContainer(DateNodeContainer container) {
		this.dateNodeContainer = container;
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
	
	
	public void setUpView() {
		
		//System.out.println("Passed startDate: " + startDate);
		
		
		
		
		allDateSlots = new ArrayList<DateSlot>();
		calendarGrid = new GridPane();
		//calendarGrid.setGridLinesVisible(true);
		//calendarGrid.getStyleClass().add("scrollPane");
		
		borderPane = new BorderPane();
		HBox dummyHBox1 = new HBox();
		HBox dummyHBox2 = new HBox();
		dummyHBox1.setPrefHeight(30);
		dummyHBox2.setPrefHeight(30);
		borderPane.setTop(dummyHBox1);
		borderPane.setBottom(dummyHBox2);
		
		overlayStack = new StackPane();
		overlayPane = new Pane();
		//intermediateGrid.getStyleClass().add("scrollPane");
		scrollPane.setContent(borderPane);
		//scrollPane.getStyleClass().add("scrollPane");
		
		
		if (currentViewType.equals("Week")) {
			
			setUpWeekView();
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addAllocatedEvents();
			
		}else if (currentViewType.equals("Month")) {
			
			setUpMonthView();
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addAllocatedEvents();
			
		} else if (currentViewType.equals("Year")) {
			
			setUpYearView();
			setUpHorizontalAxisLabels();
			setUpCurrentFocusPane();
			addAllocatedEvents();
		}

	}
	public DateSlot initDateSlot(LocalDate thisDate, LocalTime thisTime) {
		DateSlot dateSlot = new DateSlot();
		LocalDateTime thisDateTime = thisDate.atTime(thisTime);
		dateSlot.getStyleClass().add("dateSlot");
		dateSlot.setPrefSize(dateSlotWidth, dateSlotHeight);
		dateSlot.setDate(thisDate);
		dateSlot.setTime(thisTime);
		//dateSlot.getChildren().add(new Label("POOP"));
		if (thisDateTime.isBefore(LocalDateTime.now())) {
			
			dateSlot.setIsPassed(true);
			dateSlot.getStyleClass().clear();
			dateSlot.getStyleClass().add("dateSlotPassed");
		}
		dateSlot.addPopup();
		dateSlot.setUpDateSlot(windowController, dateNodeContainer);
		if (allDateSlots.contains(dateSlot) == false) {
			allDateSlots.add(dateSlot);
		}
		return dateSlot;
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
		
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 24; j ++) {
				
				LocalDate thisDate = calendarStartDate.plusDays(i);
				LocalTime thisTime = startTime.plusHours(j);
				thisDate.format(dtf);

				DateSlot dateSlot = initDateSlot(thisDate, thisTime);
				calendarGrid.add(dateSlot, i, j);

			}
		}
		
		
		//overlayPane.getChildren().add(new Rectangle(10, 10, 10, 10));
		//overlayStack.getChildren().addAll(calendarGrid, overlayPane);
		//borderPane.setCenter(overlayStack);
		
		borderPane.setCenter(calendarGrid);
		

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
		
		int col = 1;
		int row = 1;
		
		for (int i = 0; i < numberOfDateNodes; i ++) {
			
			if (i < (startDay - 1)) {
				Region dummyRegion = new Region();
				dummyRegion.setPrefSize(dateSlotWidth, 30);
				calendarGrid.add(dummyRegion, col, row);
				col ++;
			}else {
				LocalDate thisDate = calendarStartDate.plusDays(i - startDay + 1);
				LocalTime thisTime = LocalTime.NOON;
				thisDate.format(dtf);
				
				DateSlot dateSlot = initDateSlot(thisDate, thisTime);
				calendarGrid.add(dateSlot, col, row);
				
				col ++;
				if (col == 8) {
					col = 1;
					row ++;
				}
			}	
		}

		borderPane.setCenter(calendarGrid);
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

		setUpVerticalAxisLabels("Year", 31);
		int dateCounter = 0;
		for (int i = 0; i < lengthsOfMonths.size(); i ++) {
			int monthLength = lengthsOfMonths.get(i);
			for (int j = 0; j < monthLength; j ++) {
				
				LocalDate thisDate = calendarStartDate.plusDays(dateCounter);
				LocalTime thisTime = LocalTime.NOON;
				thisDate.format(dtf);
				
				DateSlot dateSlot = initDateSlot(thisDate, thisTime);
				calendarGrid.add(dateSlot, i + 1, j + 1);
				
				dateCounter ++;
				
			}
			
		}

		borderPane.setCenter(calendarGrid);

	}
	
	public void addAllocatedEvents() {
		for (DateNode node : dateNodeContainer.getAllNodes()) {
			LocalDateTime workoutDateTime = node.getWorkout().getDateTime();
			if (workoutDateTime != null){
				for (DateSlot dateSlot : allDateSlots) {
					if (currentViewType.equals("Week")) {
						if (dateSlot.getDateTime().equals(workoutDateTime)){
							addEventToDateSlot(dateSlot, node);
						}
					}else if (currentViewType.equals("Month")) {
						if (dateSlot.getDate().equals(workoutDateTime.toLocalDate())) {
							addEventToDateSlot(dateSlot, node);
						}
					}else if (currentViewType.equals("Year")) {
						if (dateSlot.getDate().equals(workoutDateTime.toLocalDate())) {
							addEventToDateSlot(dateSlot, node);
						}
					}
					
				}
			}
			
		}
		
		
		// allocate the node based on the date
	}
	public void addEventToDateSlot(DateSlot dateSlot, DateNode node) {
		dateSlot.getChildren().add(node);
		Tooltip.uninstall(dateSlot, null);
		if (dateNodeContainer.getAllocatedNodes().contains(node) == false) {
			dateNodeContainer.addToAllocatedNodes(node);
		}
		
	}
	
	//public void addAllocatedEvents(ArrayList<DateNode> nodes) {
		
	//}
	
	public void scrollToFirstWorkout() {
		//System.out.println("scrollTo fired");
		this.applyCss();
		this.layout();
		if (dateNodeContainer.getAllocatedNodes().size() != 0) {
			dateNodeContainer.sortAllocatedDateNodes();
			DateNode firstNode = dateNodeContainer.getAllocatedNodes().get(0);
			LocalTime time = firstNode.getWorkout().getTime();
			int numberOfVDateSlots;
			int verticalValue;
			if (time != null) {
				numberOfVDateSlots = Integer.parseInt(time.toString().substring(0, 2));
				verticalValue = numberOfVDateSlots * 30;
		        scrollPane.setVvalue(verticalValue);
			}else {
				numberOfVDateSlots = 12;
				verticalValue = numberOfVDateSlots * 30;
				scrollPane.setVvalue(verticalValue);
			}
			
	        //scrollPane.setHvalue(x/width);
		}
		
	}
	public void saveChanges(Cycle selectedCycle) {
		if (dateNodeContainer.getAllocatedNodes().size() != 0) {
			SQLService sql = new SQLService();
			LocalDate firstDate = dateNodeContainer.getAllocatedNodes().get(0).getWorkout().getDate();
			LocalDate endDate = dateNodeContainer.getAllocatedNodes().get(dateNodeContainer.getAllocatedNodes().size() - 1).getWorkout().getDate();
			for (DateNode node : dateNodeContainer.getAllocatedNodes()) {
				Workout workout = node.getWorkout();
				if (workout.getDate().isBefore(firstDate)) {
					firstDate = workout.getDate();
				}
				if (workout.getDate().isAfter(endDate)) {
					endDate = workout.getDate();
				}
				
			}
			LocalDate dates[] = {firstDate, endDate};
			selectedCycle.setDates(dates);
			selectedCycle.sortWorkouts();
			//for (Workout w : selectedCycle.getWorkouts()) {
			//	System.out.println(w.getDate());
			//}
			sql.upsertCycle(selectedCycle);
		}
		
		//for (DateNode changedNode : changedNodes) {
			
		//	Workout w = changedNode.getWorkout();
		//	sql.upsertWorkout(w, selectedCycle.getId());
			
			
		//}
		//changedNodes.clear(); // reset so that if not moved again before next save, no unessecary sql
		
		
			
	}
	
	
}
