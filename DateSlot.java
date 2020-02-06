import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;

public class DateSlot extends StackPane {
	
	private CalendarViewElementContainer elementContainer;
	private LocalDate date;
	private LocalTime time;
	private LocalDateTime dateTime;
	private double width, height, xPos, yPos;
	private Pane parentPane;
	private boolean isPassed;
	
	public DateSlot(Node...children) {
		super(children);
	}
	
	public DateSlot(LocalDate thisDate, LocalTime thisTime, double width, double height, double xPos, double yPos, CalendarViewElementContainer elementContainer, Node...children) {
		super(children);
		this.getStyleClass().add("dateSlot");
		
		this.elementContainer = elementContainer;
		this.date = thisDate;
		this.time = thisTime;
		this.dateTime = thisDate.atTime(thisTime);
		this.width = width;
		this.height = height;
		this.setPrefSize(width, height);
		//dateSlot.getChildren().add(new Label("POOP"));
		if (this.dateTime.isBefore(LocalDateTime.now())) {
			
			this.isPassed = true;
			this.getStyleClass().clear();
			this.getStyleClass().add("dateSlotPassed");
		}
		this.addPopup();
		this.setPos(xPos, yPos);
		this.parentPane = elementContainer.getCalendarPane();
		this.parentPane.getChildren().add(this);
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
	public double[] getPos() {
		double[] pos = {this.xPos, this.yPos};
		return pos;
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
	public void setPos(double xPos, double yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.relocate(xPos,  yPos);
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
	public void setUpDateSlot() {
		
		DateSlot dateSlot = this;
		
		dateSlot.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				elementContainer.setLastEnteredDateSlot(dateSlot);
			}
		});
		//System.out.println(date);
		dateSlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.SECONDARY) {
					ContextMenu menu = new ContextMenu();
					MenuItem createNewWorkoutItem = new MenuItem("Add new Workout");
					createNewWorkoutItem.setOnAction(e -> {
						WindowController controller = new WindowController();
						NewWorkoutModal newWorkoutModal = new NewWorkoutModal(controller);
						if (controller.getStoredWorkout() != null) {
							elementContainer.getSelectedCycle().getWorkouts().add(controller.getStoredWorkout());
							elementContainer.getSelectedCycle().sortWorkouts();
						}
					});
					menu.getItems().add(createNewWorkoutItem);
					if (elementContainer.getSelectedCycle() != null) {
						Menu allocateExistingWorkout = new Menu("Allocate existing Workout");
						//nodeContainer.sortAllDateNodes();
						
						for (Workout workout: elementContainer.getSelectedCycle().getWorkouts()) {
							//Workout workout = dateNode.getWorkout();
							//System.out.println(workout);
							Menu workoutSubMenu = new Menu(workout.basicToString());
							allocateExistingWorkout.getItems().add(workoutSubMenu);
							//System.out.println(dateNode);
							
							String movementsStr = "";
							for (Movement movement : workout.getMovements()) {
								movementsStr += movement.fullToString() + "\n\n";
							}
							//System.out.println(movementsStr);
							MenuItem movementsItem = new MenuItem(movementsStr);
							workoutSubMenu.getItems().add(movementsItem);
							
							movementsItem.setOnAction(e -> {
								workout.setDate(date);
								workout.setTime(time);
								DateNode relevantDateNode = elementContainer.getDateNodeByWorkout(workout);
								relevantDateNode.setSize(width - 4, height - 4);
								relevantDateNode.setPos(xPos + 2, yPos + 2);
								if (parentPane.getChildren().contains(relevantDateNode)) {
									parentPane.getChildren().remove(relevantDateNode);
								}else {
									elementContainer.getNullDateNodes().remove(relevantDateNode);
									elementContainer.getNonNullDateNodes().add(relevantDateNode);
								}
								
								parentPane.getChildren().add(relevantDateNode);
								elementContainer.getSelectedCycle().sortWorkouts();
								//elementContainer.sortNodes();
								elementContainer.resetAllDateNodeUiInfo();

							});

						}
						menu.getItems().add(allocateExistingWorkout);
					}
					
					
					dateSlot.setOnContextMenuRequested(e -> {
						menu.show(dateSlot, e.getScreenX(), e.getScreenY());
					});
				}
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
	                	
	                	DateNode transferredNode = elementContainer.getStoredDateNode(); // THIS IS OLD LINE 
	                	DateSlot transferredFromDateSlot = elementContainer.getStoredDateSlot();
                		transferredFromDateSlot.addPopup();

                		transferredNode.setSize(width - 4, height - 4);
                		transferredNode.setPos(xPos + 2, yPos + 2);
                		transferredNode.getWorkout().setDate(date);
                		transferredNode.getWorkout().setTime(time);
	                	//trasnferredNode.getWorkout().getParentCycle().sortWorkouts();
                		elementContainer.getSelectedCycle().sortWorkouts();
                		//elementContainer.sortNodes();
	                	elementContainer.resetAllDateNodeUiInfo();
	                	
	                	dateSlot.removePopup();
	                	elementContainer.getCalendarPane().getChildren().remove(transferredNode);
	                	elementContainer.getCalendarPane().getChildren().add(transferredNode);
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
