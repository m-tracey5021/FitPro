
public class AlertBoxController {
	
	//private AlertBox controlledBox;
	
	private String strReturn;
	private int intReturn;
	private double doubleReturn;
	private boolean yes, no;
	
	public AlertBoxController() {
		
	}
	
	
	
	public boolean[] getYesNo() {
		boolean result[] = {yes, no};
		return result;
	}
	public void setYesNo(boolean yes, boolean no) {
		this.yes = yes;
		this.no = no;
	}
	/*
	 * public void setControlledBox(AlertBox ab) { this.controlledBox = ab; }
	 */
}
