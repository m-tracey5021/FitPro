import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import javafx.geometry.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CalendarView extends GridPane {
	
	private GridPane calendarGrid;
	private Cycle selectedCycle;
	private DateNode draggedNode;
	private AnchorPane sourcePane;
	private WindowController windowController;
	private ArrayList<DateSlot> allDateSlots;
	//private ArrayList<DateNode> allocatedDateNodes;
	private NodeContainer nodeContainer;
	//private ArrayList<DateNode> changedNodes;
	private LocalDate defaultDate, calendarStartDate, startDate;
	private DateTimeFormatter dtf;
	
	
	
	public CalendarView(WindowController windowController, NodeContainer nodeContainer) {
		this.windowController = windowController;
		
		this.allDateSlots = new ArrayList<DateSlot>();
		this.nodeContainer = nodeContainer;
		this.defaultDate = LocalDate.now();
		
		this.dtf = DateTimeFormatter.ofPattern("dd/MM//yyyy");
		defaultDate.format(dtf);
		this.setGridLinesVisible(true);
		
		
		
		/*
		 * use startDate if its not null, otherwise use current time if no startDate yet
		 * set startDate to first DateNode if not set
		 */
		
	}
	public void addHorizontalSpace(int startRow) {
		for (int i = 0; i < 9; i ++) {
			Region dummyRegion = new Region();
			dummyRegion.setPrefSize(70, 30);
			//dummyRegion.setPadding(new Insets(0, 0, 0, 35));
			this.add(dummyRegion, i, startRow);
		}
	}
	public void addAxisLabels(String type, int rows) {
		if (type.equals("Week")) {
			for (int i = 0; i < rows; i ++) {
				Label timeLabel = new Label(i + ":00");
				timeLabel.setPrefSize(70, 30);
				timeLabel.setPadding(new Insets(0, 0, 0, 20));
				this.add(timeLabel, 0, i + 1);
			}
		}else if (type.equals("Month")) {
			for(int i = 0; i < rows; i ++) {
				Label weekLabel = new Label("Week " + (i + 1));
				weekLabel.setPrefSize(70, 30);
				weekLabel.setPadding(new Insets(0, 0, 0, 15));
				this.add(weekLabel, 0, i + 1);
			}
		}else {
			System.out.println("Wrong type given to create labels");
		}
	}
	public void addRightSpace(int rows) {
		for (int i = 0; i < rows; i ++) {
			Region dummyRegion = new Region();
			dummyRegion.setPrefSize(70, 30);
			this.add(dummyRegion, 2, i + 1);
			
		}
	}
	public void setUpDefaultView() {
		this.getChildren().clear();
		Label noCycleLabel = new Label("Select a Cycle to get Started");
		noCycleLabel.getStyleClass().clear();
		noCycleLabel.getStyleClass().add("defaultLabel");
		noCycleLabel.setAlignment(Pos.CENTER);
		noCycleLabel.setPrefSize(850, 400);
		GridPane.setConstraints(noCycleLabel, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER);
		this.getChildren().add(noCycleLabel);
		this.setGridLinesVisible(true);
	}
	
	public void setUpView(String type, LocalDate startDate) {
		
		//System.out.println("Passed startDate: " + startDate);
		
		this.getChildren().clear();
		this.startDate = startDate;
		this.allDateSlots = new ArrayList<DateSlot>();
		this.calendarGrid = new GridPane();
		
		if (type.equals("Week")) {
			LocalTime startTime = LocalTime.MIN;
			int dayOfWeek = startDate.getDayOfWeek().getValue();
			if (dayOfWeek == 7) {
				dayOfWeek = 1;
			}else {
				dayOfWeek += 1;
			}
			calendarStartDate = startDate.minusDays(dayOfWeek - 1);

			addHorizontalSpace(0);
			addAxisLabels("Week", 24);
			
			for (int i = 0; i < 7; i ++) {
				for (int j = 0; j < 24; j ++) {
					//System.out.println(j);
					DateSlot dateSlot = new DateSlot();
					
					LocalDate thisDate = calendarStartDate.plusDays(i);
					LocalTime thisTime = startTime.plusHours(j);
					LocalDateTime thisDateTime = thisDate.atTime(thisTime);
					
					thisDate.format(dtf);
					dateSlot.getStyleClass().add("dateSlot");
					dateSlot.setPrefSize(70, 30);
					dateSlot.setDate(thisDate);
					dateSlot.setTime(thisTime);
					dateSlot.getChildren().add(new Label("POOP"));
					if (thisDateTime.isBefore(LocalDateTime.now())) {
						
						dateSlot.setIsPassed(true);
						dateSlot.getStyleClass().clear();
						dateSlot.getStyleClass().add("dateSlotPassed");
					}
					dateSlot.addPopup();
					dateSlot.setUpDateSlot(windowController, nodeContainer);
					if (allDateSlots.contains(dateSlot) == false) {
						allDateSlots.add(dateSlot);
					}
					
			
					calendarGrid.add(dateSlot, i, j);
					
					if (dateSlot.getDate().getDayOfWeek() == DayOfWeek.MONDAY) {
						System.out.println(i + " " + j);
						System.out.println("date: " + dateSlot.getDate());
						System.out.println("dateSlot just created: " + dateSlot);
						System.out.println("dateSlot added under calendarGrid " + calendarGrid.getChildren().get(calendarGrid.getChildren().size() - 1));
						System.out.println();
					}
					
				}
			}
			
			this.add(calendarGrid, 1, 1, 7, 24);
			addRightSpace(24);
			addHorizontalSpace(25);
			
			
		}else if (type.equals("Month")) {
			
			Month month = startDate.getMonth();
			int numberOfDateNodes = month.length(startDate.isLeapYear());
			int dayOfMonth = startDate.getDayOfMonth();
			calendarStartDate = startDate.minusDays(dayOfMonth - 1);
			int startDay = calendarStartDate.getDayOfWeek().getValue();
			if (startDay == 7) {
				startDay = 1;
			}else {
				startDay += 1;
			}
			
			numberOfDateNodes += startDay - 1; // to count the number of datenodes including month before hand
			
			int cols = 7;
			double rows = Math.ceil(Double.valueOf(numberOfDateNodes) / 7.0);
			//System.out.println(rows);
			
			addHorizontalSpace(0);
			addAxisLabels("Month", (int)rows);
			
			
			int col = 1;
			int row = 1;
			
			for (int i = 0; i < numberOfDateNodes; i ++) {
				
				if (i < (startDay - 1)) {
					Region dummyRegion = new Region();
					dummyRegion.setPrefSize(70, 30);
					calendarGrid.add(dummyRegion, col, row);
					col ++;
				}else {
					LocalDate thisDate = calendarStartDate.plusDays(i - startDay + 1);
					DateSlot dateSlot = new DateSlot();
					
					thisDate.format(dtf);
					dateSlot.getStyleClass().add("dateSlot");
					dateSlot.setPrefSize(70, 30);
					dateSlot.setDate(thisDate);
					dateSlot.setTime(LocalTime.NOON);
					if (thisDate.isBefore(LocalDate.now())) {
						dateSlot.setIsPassed(true);
						dateSlot.getStyleClass().clear();
						dateSlot.getStyleClass().add("dateSlotPassed");
					}
					dateSlot.addPopup();
					dateSlot.setUpDateSlot(windowController, nodeContainer);
					if (allDateSlots.contains(dateSlot) == false) {
						allDateSlots.add(dateSlot);
					}
					else {
						System.out.println("dateSlot already in dateSlots: " + dateSlot);
					}
					calendarGrid.add(dateSlot, col, row);
					
					col ++;
					if (col == 8) {
						col = 1;
						row ++;
					}
				}	
			}
			int rowsInt = (int) rows;
			this.add(calendarGrid, 1, 1, 7, rowsInt);
			addRightSpace(rowsInt);
			addHorizontalSpace(rowsInt + 1);
		}  
	}
	public void previousTerm(String type) {
		if (type.equals("Week")) {
			setUpView("Week", startDate.minusDays(7));
		}else if (type.equals("Month")) {
			setUpView("Month", startDate.minusMonths(1));
		}
		
		
	}
	public void nextTerm(String type) {
		if (type.equals("Week")) {
			setUpView("Week", startDate.plusDays(7));
		}else if (type.equals("Month")) {
			setUpView("Month", startDate.plusMonths(1));
		}
	}
	public void addAllocatedWorkout(DateNode dn, String type) {
		LocalDate workoutDate = dn.getWorkout().getDate();
		LocalTime workoutTime = dn.getWorkout().getTime();
		for (DateSlot ds : allDateSlots) {
			//System.out.println(ds.getDate());
			//System.out.println(workoutDate);
			if (type.equals("Week")) {
				if (workoutTime == null) { // if theres a date but no time put it at midday, for week view
					if (ds.getDate().equals(workoutDate) && ds.getTime().equals(LocalTime.NOON)) {
						ds.getChildren().add(dn);
						Tooltip.uninstall(ds, null);
						if (nodeContainer.getAllocatedNodes().contains(dn) == false) {
							nodeContainer.addToAllocatedNodes(dn);;
						}
					}
				}else { // if theres date and time for week view
					//System.out.println("before ds.getDate()");
					//System.out.println(ds.getDate());
					//System.out.println(ds.getTime());
					if (ds.getDate().equals(workoutDate) && ds.getTime().equals(workoutTime)) {
						ds.getChildren().add(dn);
						Tooltip.uninstall(ds, null);
						if (nodeContainer.getAllocatedNodes().contains(dn) == false) {
							nodeContainer.addToAllocatedNodes(dn);;
						}
						
					}
				}
				
			}else if (type.equals("Month")) {
				
				
				if (ds.getDate().equals(workoutDate)) {
					//System.out.println("DATE FOUND : " + ds.getDate() + " workoutDate = " + workoutDate);
					ds.getChildren().add(dn);
					Tooltip.uninstall(ds, null);
					if (nodeContainer.getAllocatedNodes().contains(dn) == false) {
						nodeContainer.addToAllocatedNodes(dn);;
					}
				}
			}
			
		}
		// allocate the node based on the date
	}
	public void scrollToFirstWorkout(ScrollPane parentScrollPane) {
		//System.out.println("scrollTo fired");
		if (nodeContainer.getAllocatedNodes().size() != 0) {
			nodeContainer.sortAllocatedDateNodes();
			DateNode firstNode = nodeContainer.getAllocatedNodes().get(0);
			LocalTime time = firstNode.getWorkout().getTime();
			int numberOfVDateSlots;
			int verticalValue;
			if (time != null) {
				numberOfVDateSlots = Integer.parseInt(time.toString().substring(0, 2));
				verticalValue = numberOfVDateSlots * 30;
		        parentScrollPane.setVvalue(verticalValue);
			}else {
				numberOfVDateSlots = 12;
				verticalValue = numberOfVDateSlots * 30;
				parentScrollPane.setVvalue(verticalValue);
			}
			
	        //scrollPane.setHvalue(x/width);
		}
		
	}
	public void saveChanges(Cycle selectedCycle) {
		if (nodeContainer.getAllocatedNodes().size() != 0) {
			SQLService sql = new SQLService();
			LocalDate firstDate = nodeContainer.getAllocatedNodes().get(0).getWorkout().getDate();
			LocalDate endDate = nodeContainer.getAllocatedNodes().get(nodeContainer.getAllocatedNodes().size() - 1).getWorkout().getDate();
			for (DateNode node : nodeContainer.getAllocatedNodes()) {
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
