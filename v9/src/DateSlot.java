import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DateSlot extends StackPane {
	
	private LocalDate date;
	private LocalTime time;
	private boolean isPassed;
	
	public DateSlot(Node...children) {
		super(children);
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
	public boolean getIsPassed() {
		return this.isPassed;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
	public void setIsPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}
	public void addPopup() {
		String str = "";
		if (time == null) {
			str = date.toString();
		}else if (date == null) {
			str = "Date: ? " +  time;
		}else {
			str = date + " " + time;
		}
		Tooltip popup = new Tooltip(str);
		Tooltip.install(this, popup);
	}
	public void removePopup() {
		Tooltip.uninstall(this, null);
	}
	@Override
	public String toString() {
		return date.toString() + " " + time.toString();
	}
	public void setUpDateSlot(WindowController windowController, DateNodeContainer nodeContainer) {
		
		DateSlot dateSlot = this;
		//System.out.println(date);
		dateSlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				
			}
		});
		
		dateSlot.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                //System.out.println("onDragOver");
                
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
            	if (dateSlot.getIsPassed() == false) {
            		if (event.getGestureSource() != dateSlot &&
	                		event.getDragboard().hasString()) {
	                    /* allow for both copying and moving, whatever  user chooses */
	                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	                }
            	}
                
                
                event.consume();
            }
        });

		dateSlot.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
            	
                //System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
            	//System.out.println(dateSlot.date);
            	//System.out.println(dateSlot.getIsPassed());
            	
            	if (dateSlot.getIsPassed() == false) {
            		
            		if (event.getGestureSource() != dateSlot &&
	                		event.getDragboard().hasString()) {
	                	//dateSlot.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
	                	dateSlot.getStyleClass().clear();
	                	dateSlot.getStyleClass().add("dateSlotHover");
	                	//System.out.println(dateSlot.getDate());
	                	//System.out.println(dateSlot.getTime());
	                }
            	}
                
                
                event.consume();
            }
        });

		dateSlot.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
            	//dateSlot.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            	if (dateSlot.getIsPassed() == false) {
            		dateSlot.getStyleClass().clear();
                	dateSlot.getStyleClass().add("dateSlot");
            	}
            	
                event.consume();
            }
        });
        
		dateSlot.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                //System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (dateSlot.getIsPassed() == false) {
                	if (db.hasString()) {
                		//System.out.println("SHOULD BE DROPPED");
	                	
	                	DateNode oldNode = windowController.getStoredDateNode(); // THIS IS OLD LINE
	                	nodeContainer.removeFromAllNodes(oldNode);
	                	
	                	DateNode newNode = new DateNode(oldNode.getWorkout());
	                	nodeContainer.addToAllNodes(newNode);
	                	newNode.setPrefSize(dateSlot.getWidth(), dateSlot.getHeight());
	                	newNode.getWorkout().setDate(date);
	                	newNode.getWorkout().setTime(time);
	                	newNode.getWorkout().getParentCycle().sortWorkouts();
	                	//System.out.println(newNode.getWorkout().getDate());
	                	//System.out.println(newNode.getWorkout().getParentCycle().getWorkouts());
	                	nodeContainer.resetAllPopups();
	                	nodeContainer.resetAllLabels();
	                	newNode.makeDraggable(windowController);
	                	newNode.addPopup();
	                	
	                	
	                	
	                	Parent parent = oldNode.getParent();
	                	if (parent.getClass().toString().equals("class javafx.scene.layout.HBox")) {
	                		HBox parentHBox = (HBox) oldNode.getParent();
		                	parentHBox.getChildren().remove(oldNode);
	                	}else if (parent.getClass().toString().equals("class DateSlot")) {
	                		DateSlot parentDateSlot = (DateSlot) oldNode.getParent();
	                		parentDateSlot.getChildren().remove(oldNode);
	                		parentDateSlot.addPopup();
	                	}else {
	                		System.out.println("Some error with parent of draggable");
	                	}
	                	
	                	dateSlot.removePopup();
	                	
	                	
	                	
	                	//System.out.println(node.hashCode());
	                	
	                	//System.out.println(thisDate + " set here");
	                	
	                	if (nodeContainer.getAllocatedNodes().contains(newNode) == false) {
	                		nodeContainer.addToAllocatedNodes(newNode);
	                	}
	                	
	                	
	                	//DateNode replacementNode = new DateNode(node.getWorkout());
	                	
	                	
	                	/*System.out.println(node.getParent());
	                	//System.out.println(node.getParent().getWidth());
	                	System.out.println(node.getWidth());
	                	System.out.println(node.getMinWidth());
	                	System.out.println(node.getMaxWidth());
	                	System.out.println(node.getPrefWidth());
	                	System.out.println(node.getPrefHeight());
	                	System.out.println(node.getPadding());
	                	*/
	                	dateSlot.getChildren().clear();
	                	dateSlot.getChildren().add(newNode);
	                	//dateSlot.setMaxWidth(100);
	                	/*
	                	 * set the date of the workout below
	                	 */
	                	// node.getWorkout().setDate();
	                    success = true;
	                }
                }
                
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                
                event.consume();
            }
        });
	}
}
