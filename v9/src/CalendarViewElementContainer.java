import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import javafx.scene.layout.Pane;


public class CalendarViewElementContainer {
	private ArrayList<DateSlot> dateSlots;
	private DateSlot storedDateSlot;
	private DateSlot lastEnteredDateSlot;
	private ArrayList<DateNode> nullDateNodes;
	private ArrayList<DateNode> nonNullDateNodes;
	private ArrayList<DateNode> allDateNodes;
	private DateNode storedDateNode;
	private Cycle selectedCycle;
	private Pane calendarPane;
	private WindowController windowController;
	
	public CalendarViewElementContainer() {
		this.dateSlots = new ArrayList<DateSlot>();
		this.nullDateNodes = new ArrayList<DateNode>();
		this.nonNullDateNodes = new ArrayList<DateNode>();
		this.allDateNodes = new ArrayList<DateNode>();


	}
	public CalendarViewElementContainer(ArrayList<DateSlot> dateSlots, ArrayList<DateNode> nullDateNodes, ArrayList<DateNode> nonNullDateNodes, 
			Cycle selectedCycle, Pane pane) {
		this.dateSlots = dateSlots;
		this.nullDateNodes = nullDateNodes;
		this.nonNullDateNodes = nonNullDateNodes;
		this.allDateNodes = new ArrayList<DateNode>();
		this.allDateNodes.addAll(nonNullDateNodes);
		this.allDateNodes.addAll(nullDateNodes);
		this.selectedCycle = selectedCycle;
		this.calendarPane = pane;

	}
	
	public ArrayList<DateSlot> getDateSlots(){
		return this.dateSlots;
	}
	
	public DateSlot getStoredDateSlot() {
		return this.storedDateSlot;
	}
	
	public DateSlot getLastEnteredDateSlot() {
		return this.lastEnteredDateSlot;
	}
	public ArrayList<DateNode> getNullDateNodes(){
		return this.nullDateNodes;
	}
	
	public ArrayList<DateNode> getNonNullDateNodes(){
		return this.nonNullDateNodes;
	}
	
	public ArrayList<DateNode> getAllDateNodes(){
		return this.allDateNodes;
	}
	
	public DateNode getStoredDateNode() {
		return this.storedDateNode;
	}
	
	public DateNode getDateNodeByWorkout(Workout workout) {
		for (DateNode dateNode : allDateNodes) {
			if (dateNode.getWorkout() == workout) {
				return dateNode;
			}
		}
		return new DateNode();
	}
	
	public Cycle getSelectedCycle() {
		return this.selectedCycle;
	}
	
	
	public Pane getCalendarPane() {
		return this.calendarPane;
	}
	
	public void addDateSlot(DateSlot dateSlot) {
		this.dateSlots.add(dateSlot);
	}
	
	public void setStoredDateSlot(DateSlot dateSlot) {
		this.storedDateSlot = dateSlot;
	}
	
	public void setLastEnteredDateSlot(DateSlot dateSlot) {
		this.lastEnteredDateSlot = dateSlot;
	}
	public void addNullDateNode(DateNode dateNode) {
		this.nullDateNodes.add(dateNode);
	}
	
	public void addNonNullDateNode(DateNode dateNode) {
		this.nonNullDateNodes.add(dateNode);
	}
	
	public void setStoredDateNode(DateNode dateNode) {
		this.storedDateNode = dateNode;
	}
	
	public void setDateNodesByCycle() {
		if (this.selectedCycle != null) {
			for (Workout workout : this.selectedCycle.getWorkouts()) {
				DateNode newNode = new DateNode(workout, this);
				newNode.addPopup();
				newNode.makeDraggable();
				if (workout.getDateTime() != null) {
					this.nonNullDateNodes.add(newNode);
				}else {
					this.nullDateNodes.add(newNode);
				}
			}
			this.allDateNodes.addAll(nonNullDateNodes);
			this.allDateNodes.addAll(nullDateNodes);
		}
	}
	
	public void setCycle(Cycle cycle) {
		if (cycle != null) {
			this.selectedCycle = cycle;
			//this.workouts = cycle.getWorkouts();
			//System.out.println(this.workouts);
		}
		
	}
	
	public void setCalendarPane(Pane pane) {
		this.calendarPane = pane;
	}
	
	public void resetAllDateNodeUiInfo() {
		for (DateNode dateNode : nonNullDateNodes) {
			dateNode.resetUiInfo();
		}
	}
	
	public void sortNonNullDateNodes() {
		if (nonNullDateNodes.size() != 0) {
			ArrayList<DateNode> sorted = new ArrayList<DateNode>();
			
			boolean done = false;
			DateNode earliestNode;
			LocalDate earliestDate;
			LocalDate comparedDate;
			while (done == false) {
				earliestNode = nonNullDateNodes.get(0);
				for (DateNode dn : nonNullDateNodes) {
					comparedDate = dn.getWorkout().getDate();
					earliestDate = earliestNode.getWorkout().getDate();
					if (comparedDate.isBefore(earliestDate)) {
						earliestNode = dn;
					}
					
				}
				nonNullDateNodes.remove(earliestNode);
				sorted.add(earliestNode);
				if (nonNullDateNodes.size() == 0) {
					done = true;
				}
			}
		
			nonNullDateNodes = sorted;
		}
		
		//System.out.println(allocatedNodes);
	}
	public void sortNodes() {
		
		sortNonNullDateNodes();
		allDateNodes = new ArrayList<DateNode>();
		allDateNodes.addAll(nonNullDateNodes);
		allDateNodes.addAll(nullDateNodes);
		
	}
	/*
	public void sortWorkouts() {
		ArrayList<Workout> sorted = new ArrayList<Workout>();
		ArrayList<Workout> nullDateWorkouts = new ArrayList<Workout>();
		int position = 0;
		int count = 0;
		int originalSize = workouts.size();
		boolean done = false;
		while (done == false) {
			//System.out.println("loop entered again");
			
			Workout w = workouts.get(position);
			if (w.getDate() == null) {
				nullDateWorkouts.add(w);
				workouts.remove(w);

			}else { // only increase i if nothing was removed
				position ++;
				
			}
			count ++;
			if (count == originalSize) {
				done = true;
			}

			
		}
		Workout earliestWorkout;
		LocalDateTime earliestDateTime;
		LocalTime earliestTime;
		LocalDateTime comparedDateTime;
		LocalTime comparedTime;
		done = false;
		while (done == false) {
			if (workouts.size() == 0) {
				done = true;
			}else {
				earliestWorkout = workouts.get(0);
				
				 // chose random workout to compare to before loop starts
				for (Workout w : workouts) {
					comparedDateTime = w.getDate().atTime(w.getTime());
					earliestDateTime = earliestWorkout.getDate().atTime(earliestWorkout.getTime());

					if (comparedDateTime.isBefore(earliestDateTime)) {
						earliestWorkout = w;
					}
					
				}
				workouts.remove(earliestWorkout);
				sorted.add(earliestWorkout);
			}
			
		}
		sorted.addAll(nullDateWorkouts);
		this.workouts = sorted;
		
		for (int i = 0; i < workouts.size(); i ++) {
			Workout w = workouts.get(i);
			if (w.getDate() != null) {
				w.setWorkoutNumber(i + 1);
			}else {
				w.setWorkoutNumber(0);
			}
			
		}

		
	}
	*/
	
	
	
	
	
	
	
	
}

