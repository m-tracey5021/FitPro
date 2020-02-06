import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Cycle extends CycleObject {
	private String ownerId;
	private String cycleId;
	private String cycleName;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean hasDates;
	private int totalWeeks;
	private int frequencyPerWeek;
	private int totalWorkouts;
	private double currentMax;
	private ArrayList<ArrayList<Integer>> RPSPW; // 'reps per set, per workout'
	private ArrayList<ArrayList<Double>> IPSPW; // 'increase per set, per workout'
	private ArrayList<Double> maxPercents;
	private ArrayList<Workout> workouts;
	
	public Cycle() {
		this.cycleId = UUID.randomUUID().toString();
		this.workouts = new ArrayList<Workout>();
	}
	public Cycle(String ownerId, String cycleName, int totalWorkouts, ArrayList<Workout> workouts) {
		this.cycleId = UUID.randomUUID().toString();
		this.ownerId = ownerId;
		this.cycleName = cycleName;
		this.totalWorkouts = totalWorkouts;
		this.workouts = workouts;
		this.hasDates = false;
	}
	public Cycle(String ownerId, String cycleName, LocalDate startDate, LocalDate endDate, int totalWorkouts) {
		this.cycleId = UUID.randomUUID().toString();
		this.ownerId = ownerId;
		this.cycleName = cycleName;
		this.totalWorkouts = totalWorkouts;
		this.workouts = new ArrayList<Workout>();
		this.startDate = startDate;
		this.endDate = endDate;
		this.hasDates = true;
	}

	/*
	 * public Cycle(String cycleName, int totalWeeks, int frequencyPerWeek, double
	 * currentMax, ArrayList<Workout> workouts) { this.cycleName = cycleName;
	 * this.frequencyPerWeek = frequencyPerWeek; this.totalWeeks = totalWeeks;
	 * this.totalWorkouts = totalWeeks * frequencyPerWeek; this.currentMax =
	 * currentMax; this.workouts = workouts; }
	 */
	/*
	 * constructor above used if workouts entered manually
	 */
	/*
	 * public Cycle(String cycleName, int totalWeeks, int frequencyPerWeek, double
	 * currentMax, ArrayList<ArrayList<Integer>> RPSPW, ArrayList<ArrayList<Double>>
	 * IPSPW, ArrayList<Double> maxPercents) { this.cycleName = cycleName;
	 * this.frequencyPerWeek = frequencyPerWeek; this.totalWeeks = totalWeeks;
	 * this.totalWorkouts = totalWeeks * frequencyPerWeek; this.currentMax =
	 * currentMax; this.RPSPW = RPSPW; this.IPSPW = IPSPW; this.maxPercents =
	 * maxPercents; this.workouts = new ArrayList<Workout>(); createWorkouts();
	 * 
	 * }
	 */
	/*
	 * constructor above used if workouts to be calculated
	 */
	/*
	 * public void createWorkouts() { for (int i = 0; i < totalWorkouts; i ++) {
	 * ArrayList<Integer> RPS = RPSPW.get(i); ArrayList<Double> IPS = IPSPW.get(i);
	 * double totalIncrease = 0.0; for (Double d : IPS) { totalIncrease += d; }
	 * double topWeight = (maxPercents.get(i) * 0.01) * currentMax; double
	 * startingWeight = topWeight - totalIncrease; Workout w = new Workout("Squat",
	 * startingWeight, topWeight, IPS, RPS); workouts.add(w); }
	 * System.out.println(workouts); }
	 */
	public String getId() {
		return this.cycleId;
	}
	public String getOwnerId() {
		return this.ownerId;
	}
	public String getCycleName() {
		return this.cycleName;
	}
	public LocalDate[] getDates() {
		LocalDate[] dates = {this.startDate, this.endDate};
		return dates;
	}
	public boolean getHasDates() {
		return this.hasDates;
	}
	public int getTotalWorkouts() {
		return this.totalWorkouts;
	}
	public ArrayList<Workout> getWorkouts() {
		return this.workouts;
	}
	public void generateNewId() {
		this.cycleId = UUID.randomUUID().toString();
	}
	public void setId(String Id) {
		this.cycleId = Id;
	}
	public void setOwnerId(String newId) {
		this.ownerId = newId;
	}
	public void setCycleName(String name) {
		this.cycleName = name;
	}
	public void setDates(LocalDate[] dates) {
		this.startDate = dates[0];
		this.endDate = dates[1];
		
	}
	public void setHasDates(boolean hasDates) {
		this.hasDates = hasDates;
	}
	public void setTotalWorkouts(int num) {
		this.totalWorkouts = num;
	}
	public void setWorkouts(ArrayList<Workout> workouts) {
		this.workouts = workouts;
	}
	public void addWorkout(Workout w) {
		this.workouts.add(w);
	}
	public void resetWorkoutNumbers() {
		for(Workout w : workouts) {
			w.setWorkoutNumber(0);
		}
	}
	public void sortWorkouts() {
		ArrayList<Workout> sorted = new ArrayList<Workout>();
		ArrayList<Workout> nullDateWorkouts = new ArrayList<Workout>();
		//System.out.println(workouts);
		//System.out.println(workouts.size());
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
				//System.out.println("workout removed");
			}else { // only increase i if nothing was removed
				position ++;
				
			}
			count ++;
			if (count == originalSize) {
				done = true;
			}
			//System.out.println("current count: " + count);
			//System.out.println("position after possible increment: " + position);
			//System.out.println("current size: " + workouts.size());
			//System.out.println("original size: " + originalSize);
			//System.out.println();
			
			
			
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
		//System.out.println("workouts: " + workouts);
		//System.out.println("sorted: " + sorted);
		//System.out.println("null dates: " + nullDateWorkouts);
		
	}
	
	@Override
	public String toString() {
		return this.cycleName;
	}
	public String printCycle() {
		String result = "Cycle: " + this.cycleName + " owned by user: " + this.ownerId + "\n";
		for (Workout w :this.workouts) {
			result += w;
		}
		
		return result;
	}
}
