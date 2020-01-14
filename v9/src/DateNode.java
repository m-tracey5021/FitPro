

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;




public class DateNode extends AnchorPane {
	
	private Workout workout;
	private Label label;
	private boolean isAllocated;
	
	public DateNode(Node...children) {
		super(children);
		this.isAllocated = false;
		this.setPrefSize(70, 30);
		this.getStyleClass().add("dateNode");
		//this.setStyle("-fx-background-color: #0099cc");
		//this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		String labelStr;
		if (workout.getWorkoutNumber() == 0) {
			labelStr = "Unallocated Workout";
		}else {
			labelStr = "Workout " + workout.getWorkoutNumber();
		}
		label = new Label(labelStr);
		label.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(label, 0.0);
		AnchorPane.setRightAnchor(label, 0.0);
		AnchorPane.setTopAnchor(label, 0.0);
		AnchorPane.setBottomAnchor(label, 0.0);
		label.getStyleClass().add("dateNodeLabel");
		label.setAlignment(Pos.CENTER);
		
		this.getChildren().add(label);
	}
	
	public DateNode(Workout workout, Node...children) {
		super(children);
		this.workout = workout;
		this.isAllocated = false;
		this.setPrefSize(70, 30);
		this.getStyleClass().add("dateNode");
		//this.setStyle("-fx-background-color: #0099cc");
		//this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		String labelStr;
		if (workout.getWorkoutNumber() == 0) {
			labelStr = "Unallocated Workout";
		}else {
			labelStr = "Workout " + workout.getWorkoutNumber();
		}
		label = new Label(labelStr);
		label.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(label, 0.0);
		AnchorPane.setRightAnchor(label, 0.0);
		AnchorPane.setTopAnchor(label, 0.0);
		AnchorPane.setBottomAnchor(label, 0.0);
		label.getStyleClass().add("dateNodeLabel");
		label.setAlignment(Pos.CENTER);
		
		this.getChildren().add(label);
	}
	
	public Workout getWorkout() {
		return this.workout;
	}
	public void setWorkout(Workout w) {
		this.workout = w;
	}
	public void setIsAllocated(boolean isAllocated) {
		this.isAllocated = isAllocated;
	}
	public void resetLabel() {
		this.getChildren().remove(label);
		String labelStr;
		if (workout.getWorkoutNumber() == 0) {
			labelStr = "Unallocated Workout";
		}else {
			labelStr = "Workout " + workout.getWorkoutNumber();
		}
		label = new Label(labelStr);
		label.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(label, 0.0);
		AnchorPane.setRightAnchor(label, 0.0);
		AnchorPane.setTopAnchor(label, 0.0);
		AnchorPane.setBottomAnchor(label, 0.0);
		label.getStyleClass().add("dateNodeLabel");
		label.setAlignment(Pos.CENTER);
		this.getChildren().add(label);
	}
	public void addPopup() {
		String str = "";
		if (workout.getDate() == null) {
			str = "Unallocated Workout: \n\n" + workout.toString();
		}else {
			str = workout.getDate() + ": \n\n" + workout.toString();
		}
		Tooltip detail = new Tooltip(str);
		Tooltip.install(this, detail);
	}
	public void removePopup() {
		Tooltip.uninstall(this, null);
	}
	public void makeDraggable(WindowController windowController) {
		DateNode node = this;
		node.setOnDragDetected(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent event) {
				/* drag was detected, start drag-and-drop gesture */
				//System.out.println("onDragDetected");
				Dragboard db = node.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(workout.getId());
				db.setContent(content);
				windowController.setStoredDateNode(node);
				//windowController.setStoredWorkout(w);
				/* allow any transfer mode */
				event.consume();
			}
		});
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//System.out.println("BEING DRAGGED");
				
				//newNode.setTranslateX(event.getX());
				//newNode.setTranslateY(event.getY());
			}
		});

		node.setOnDragDone(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture ended */
				// System.out.println("onDragDone");
				/* if the data was successfully m oved, clear it */
				if (event.getTransferMode() == TransferMode.MOVE) {
					// newNode.setBackground(new Background(new BackgroundFill(Color.WHITE,
					// CornerRadii.EMPTY, Insets.EMPTY)));
					
				}

				event.consume();
			}
		});
	}
}
