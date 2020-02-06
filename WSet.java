import java.util.*;

public class WSet {
	private int reps;
	private double weight;
	
	public WSet(int reps, double weight) {
		this.reps = reps;
		this.weight = weight;
	}
	public int getReps() {
		return this.reps;
	}
	public double getWeight() {
		return this.weight;
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
