
public class WindowController {
	/*
	 * fields for returning values from windows
	 */
	private String strReturn;
	private int intReturn;
	private double doubleReturn;
	private boolean boolReturn;
	/*
	 * fields for storing workouts etc when dragging and dropping
	 */
	private Workout storedWorkout;
	private Cycle storedCycle;
	private DateNode storedDateNode;
	
	public WindowController() {
		
	}
	
	public String getString() {
		return strReturn;
	}
	public int getInt() { // can use this return value for multiple choices
		return intReturn;
	}
	public double getDouble() {
		return this.doubleReturn;
	}
	public boolean getBool() {
		return this.boolReturn;
	}
	public Workout getStoredWorkout() {
		return this.storedWorkout;
	}
	public Cycle getStoredCycle() {
		return this.storedCycle;
	}
	public DateNode getStoredDateNode() {
		return this.storedDateNode;
	}
	public void setString(String s) {
		this.strReturn = s;
	}
	public void setInt(int i) {
		this.intReturn = i;
	}
	public void setDouble(double d) {
		this.doubleReturn = d;
	}
	public void setBool(boolean b) {
		this.boolReturn = b;
	}
	public void setStoredWorkout(Workout w) {
		this.storedWorkout = w;
	}
	public void setStoredCycle(Cycle c) {
		this.storedCycle = c;
	}
	public void setStoredDateNode(DateNode dn) {
		this.storedDateNode = dn;
	}
}
