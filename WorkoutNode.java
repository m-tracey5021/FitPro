import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Rectangle;

public class WorkoutNode extends Rectangle {

	
	private Workout workout;
	private Label label;
	
	public WorkoutNode(Workout w) {
		super(100, 50);
		this.workout = w;
		this.label = new Label("Workout x: ");
		
		Tooltip detail = new Tooltip(workout.toString());
		Tooltip.install(this, detail);
		//this.getChildren().add(label);
	}
}
