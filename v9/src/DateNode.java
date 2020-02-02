

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




public class DateNode extends StackPane {
	
	private CalendarViewElementContainer elementContainer;
	private Workout workout;
	private Label label;
	private double width, height, xPos, yPos;
	private boolean isAllocated;
	
	public DateNode(Node...children) {
		super(children);
		this.isAllocated = false;
		this.getStyleClass().add("dateNode");
		//this.setStyle("-fx-background-color: #0099cc");
		//this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		resetLabel();
	}
	
	public DateNode(Workout workout, CalendarViewElementContainer elementContainer, Node...children) {
		super(children);
		this.workout = workout;
		this.elementContainer = elementContainer;
		this.isAllocated = false;
		this.getStyleClass().add("dateNode");
		//this.setStyle("-fx-background-color: #0099cc");
		//this.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		resetUiInfo();

	}
	
	public Workout getWorkout() {
		return this.workout;
	}
	public double[] getPos() {
		double[] pos = {xPos, yPos};
		return pos;
	}
	public double[] getSize() {
		double[] size = {width, height};
		return size;
	}
	public void setWorkout(Workout w) {
		this.workout = w;
	}
	public void setPos(double x, double y) {
		this.relocate(x, y);
		this.xPos = x;
		this.yPos = y;
	}
	public void setSize(double width, double height) {
		this.setPrefSize(width, height);
		this.width = width;
		this.height = height;
	}
	public void setIsAllocated(boolean isAllocated) {
		this.isAllocated = isAllocated;
	}
	public void resetLabel() {
		this.getChildren().remove(label);
		String labelStr;
		if (workout.getDateTime() == null) {
		
			labelStr = "Unallocated Workout";
		}else {
			labelStr = "Workout " + workout.getWorkoutNumber();
		}
		label = new Label(labelStr);
		label.getStyleClass().add("dateNodeLabel");
		this.getChildren().add(label);
	}
	public void addPopup() {
		String str = "";
		if (workout.getDateTime() == null) {
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
	public void resetPopup() {
		removePopup();
		addPopup();
	}
	
	public void resetUiInfo() {
		resetLabel();
		resetPopup();
	}
	public void makeDraggable() {
		DateNode node = this;
		node.setOnDragDetected(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent event) {
				/* drag was detected, start drag-and-drop gesture */
				//System.out.println("onDragDetected");
				Dragboard db = node.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(workout.getId());
				db.setContent(content);
				elementContainer.setStoredDateNode(node);
				elementContainer.setStoredDateSlot(elementContainer.getLastEnteredDateSlot());
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
