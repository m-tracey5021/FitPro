
import java.util.*;

public class Movement implements CycleComponent {
	
	private String movementId;
	private Workout parentWorkout;
	private String name;
	private double current1RM;
	private ArrayList<Set> sets;
	
	
	public Movement() {
		this.movementId = UUID.randomUUID().toString();
		this.sets = new ArrayList<Set>();
	}
	public Movement(String name) {
		this.movementId = UUID.randomUUID().toString();
		this.name = name;
		this.sets = new ArrayList<Set>();
	}
	public Movement(String name, double current1RM) {
		this.movementId = UUID.randomUUID().toString();
		this.name = name;
		this.current1RM = current1RM;
		this.sets = new ArrayList<Set>();
	}
	public Movement(Movement movementToBeCopied) {
		this.movementId = UUID.randomUUID().toString();
		this.name = movementToBeCopied.getName();
		this.current1RM = movementToBeCopied.getCurrent1RM();
		this.sets = new ArrayList<Set>();
		for (int i = 0; i < movementToBeCopied.getSets().size(); i ++) {
			Set copiedSet = new Set(movementToBeCopied.getSets().get(i));
			this.sets.add(copiedSet);
		}
	}
	
	public String getId() {
		return this.movementId;
	}
	public Workout getParentWorkout() {
		return this.parentWorkout;
	}
	public ArrayList<Set> getSets() {
		return this.sets;
	}
	public Set getSet(int index) {
		return this.sets.get(index);
	}
	public String getName() {
		return this.name;
	}
	public double getCurrent1RM() {
		return this.current1RM;
	}
	public void generateNewId() {
		this.movementId = UUID.randomUUID().toString();
	}
	public void setId(String Id) {
		this.movementId = Id;
	}
	public void setParentWorkout(Workout parent) {
		this.parentWorkout = parent;
	}
	public void setSets(ArrayList<Set> sets) {
		this.sets = sets;
	}
	public void addSet(Set set) {
		this.sets.add(set);
	}
	public void addName(String name) {
		this.name = name;
	}
	public void setCurrent1RM(double current1RM) {
		this.current1RM = current1RM;
	}
	@Override
	public String toString() {
		return this.name;
	}
}
