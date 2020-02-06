import java.time.LocalDate;
import java.util.ArrayList;

public class NodeContainer {
	private ArrayList<DateNode> allNodes;
	private ArrayList<DateNode> allocatedNodes;
	
	public NodeContainer() {
		this.allNodes = new ArrayList<DateNode>();
		this.allocatedNodes = new ArrayList<DateNode>();
		
	}
	public ArrayList<DateNode> getAllNodes() {
		return allNodes;
	}
	public ArrayList<DateNode> getAllocatedNodes(){
		return allocatedNodes;
	}
	public void addToAllNodes(DateNode n) {
		allNodes.add(n);
	}
	public void removeFromAllNodes(DateNode n) {
		allNodes.remove(n);
	}
	public void addToAllocatedNodes(DateNode n) {
		allocatedNodes.add(n);
	}
	public void removeFromAllocatedNodes(DateNode n) {
		allocatedNodes.remove(n);
	}
	public void resetAllNodes() {
		allNodes = new ArrayList<DateNode>();
	}
	public void resetAllocatedNodes() {
		allocatedNodes = new ArrayList<DateNode>();
	}
	public void resetAllLabels() {
		for(DateNode dn : allNodes) {
			dn.resetLabel();
		}
	}
	public void resetAllPopups() {
		for(DateNode dn : allNodes) {
			dn.removePopup();
			dn.addPopup();
		}
	}
	public void sortAllDateNodes() {
		ArrayList<DateNode> sorted = new ArrayList<DateNode>();
		
		boolean done = false;
		DateNode earliestNode;
		LocalDate earliestDate;
		LocalDate comparedDate;
		while (done == false) {
			earliestNode = allNodes.get(0);
			for (DateNode dn : allNodes) {
				comparedDate = dn.getWorkout().getDate();
				earliestDate = earliestNode.getWorkout().getDate();
				if (comparedDate.isBefore(earliestDate)) {
					earliestNode = dn;
				}
				
			}
			allNodes.remove(earliestNode);
			sorted.add(earliestNode);
			if (allNodes.size() == 0) {
				done = true;
			}
		}
	
		allNodes = sorted;
		//System.out.println(allNodes);
	}
	public void sortAllocatedDateNodes() {
		ArrayList<DateNode> sorted = new ArrayList<DateNode>();
		
		boolean done = false;
		DateNode earliestNode;
		LocalDate earliestDate;
		LocalDate comparedDate;
		while (done == false) {
			earliestNode = allocatedNodes.get(0);
			for (DateNode dn : allocatedNodes) {
				comparedDate = dn.getWorkout().getDate();
				earliestDate = earliestNode.getWorkout().getDate();
				if (comparedDate.isBefore(earliestDate)) {
					earliestNode = dn;
				}
				
			}
			allocatedNodes.remove(earliestNode);
			sorted.add(earliestNode);
			if (allocatedNodes.size() == 0) {
				done = true;
			}
		}
	
		allocatedNodes = sorted;
		//System.out.println(allocatedNodes);
	}
	
}
