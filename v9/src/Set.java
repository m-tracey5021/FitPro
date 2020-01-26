import java.util.*;

public class Set extends CycleObject{
	private String setId;
	private Movement parentMovement;
	private int reps;
	private double weight;
	
	public Set(int reps, double weight) {
		this.setId = UUID.randomUUID().toString();
		this.reps = reps;
		this.weight = weight;
	}
	public Set(int reps, double weight, String Id) {
		this.setId = Id;
		this.reps = reps;
		this.weight = weight;
	}
	public Set(Set setToBeCopied) {
		this.setId = UUID.randomUUID().toString();
		this.reps = setToBeCopied.getReps();
		this.weight = setToBeCopied.getWeight();
	}
	public String getId() {
		return this.setId;
	}
	public Movement getParentMovement() {
		return this.parentMovement;
	}
	public int getReps() {
		return this.reps;
	}
	public double getWeight() {
		return this.weight;
	}
	public void generateNewId() {
		this.setId = UUID.randomUUID().toString();
	}
	public void setId(String Id) {
		this.setId = Id;
	}
	public void setParentMovement(Movement parent) {
		this.parentMovement = parent;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return reps + " x " + weight;
	}
}
