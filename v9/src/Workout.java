import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Workout implements CycleComponent {
	private String workoutId;
	private int workoutNumber;
	
	
	private Cycle parentCycle;
	private ArrayList<Movement> movements;
	private LocalDate date;
	private LocalTime time;
	
	// calories burned
	// time taken (could be set after workout is completed (then estimate future workouts??))
	// muscle group (could be under movement)
	// intensity? make a rating scale? could be calories burned / time taken
	// average increase in weight across movements (iterate through and count) 
	
	
	
	
	public Workout() {
		//sets = new ArrayList<Set>();
		this.workoutId = UUID.randomUUID().toString();
		this.workoutNumber = 0;
		this.movements = new ArrayList<Movement>();
		
	}
	public Workout(LocalDate date, LocalTime time) {
		this.workoutId = UUID.randomUUID().toString();
		this.workoutNumber = 0;
		this.movements = new ArrayList<Movement>();
		this.date = date;
		this.time = time;
		
	}

	/*
	 * public Workout(String movement, double startingWeight, double topWeight,
	 * ArrayList<Double> IPS, ArrayList<Integer> RPS) { this.movement = movement;
	 * this.startingWeight = startingWeight; this.topWeight = topWeight; this.IPS =
	 * IPS; this.RPS = RPS; this.sets = new ArrayList<Set>(); createSets();
	 * 
	 * } public void createSets() { double weight = startingWeight; for (int i = 0;
	 * i < RPS.size(); i ++) { Set s = new Set(RPS.get(i), weight); sets.add(s); if
	 * (i != IPS.size()) { weight += IPS.get(i); } } }
	 */
	public String getId() {
		return this.workoutId;
	}
	public int getWorkoutNumber() {
		return this.workoutNumber;
	}
	public Cycle getParentCycle() {
		return this.parentCycle;
	}
	public ArrayList<Movement> getMovements(){
		return this.movements;
	}
	public Movement getMovement(int index) {
		return this.movements.get(index);
	}
	public LocalDate getDate() {
		return this.date;
	}
	public LocalTime getTime() {
		return this.time;
	}
	public LocalDateTime getDateTime() {
		return this.date.atTime(this.time);
	}
	public void generateNewId() {
		this.workoutId = UUID.randomUUID().toString();
	}
	public void setId(String Id) {
		this.workoutId = Id;
	}
	public void setWorkoutNumber(int num) {
		this.workoutNumber = num;
	}
	public void setParentCycle(Cycle parent) {
		this.parentCycle = parent;
	}
	public void setMovements(ArrayList<Movement> movements) {
		this.movements = movements;
	}
	public void addMovement(Movement movement) {
		this.movements.add(movement);
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	
	
	
	/*
	 * public void addSet(Set s) { sets.add(s); } public ArrayList<Set> getSets(){
	 * return sets; }
	 */
	
	@Override
	public String toString() {
		String result = "";
		if (workoutNumber == 0) {
			result = "Unallocated Workout: \n\t" ;
		}else {
			result = "Workout " + workoutNumber + ": \n\t" ;
		}
		
		for (int i = 0; i < this.movements.size(); i ++) {
			
			Movement currentMovement = this.movements.get(i);
			result += currentMovement + ": \n" + "\t\t";
			for (int j = 0; j < currentMovement.getSets().size(); j ++) {
				
				Set set = currentMovement.getSets().get(j);
				if (j == currentMovement.getSets().size() - 1) { // if its the last set
					if (i == this.movements.size() - 1) { // if its the last set of the last movement
						result += set + "\n";
					}else { // if its the last set and not the last movement
						result += set + "\n\t";
					}
				}else { // if its not the last set
					result += set + "\n" + "\t\t";
				}
				
				
			}
		}
		
		return result;
		/*
		 * MAKE SURE THAT THIS RETURNS THE WHOLE WORKOUT PROPERLY, SO 
		 * I DONT HAVE TO KEEP UPDATING THE TEXT BOX MANUALLY
		 */
	}
}










