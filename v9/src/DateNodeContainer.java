import java.time.LocalDate;
import java.util.ArrayList;

public class DateNodeContainer {
	private ArrayList<DateNode> allNodes;
	private ArrayList<DateNode> unallocatedNodes;
	private ArrayList<DateNode> allocatedNodes;
	
	public DateNodeContainer() {
		this.allNodes = new ArrayList<DateNode>();
		this.unallocatedNodes = new ArrayList<DateNode>();
		this.allocatedNodes = new ArrayList<DateNode>();
		
	}
	public ArrayList<DateNode> getAllNodes() {
		return allNodes;
	}
	public ArrayList<DateNode> getUnallocatedNodes(){
		return this.unallocatedNodes;
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
	public void addToUnallocatedNodes(DateNode n) {
		unallocatedNodes.add(n);
	}
	public void removeFromUnallocatedNodes(DateNode n) {
		unallocatedNodes.remove(n);
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
	/*
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
	*/
	public void sortAllDateNodes() {
		sortAllocatedDateNodes();
		allNodes = new ArrayList<DateNode>();
		allNodes.addAll(allocatedNodes);
		allNodes.addAll(unallocatedNodes);
	}
	public void sortAllocatedDateNodes() {
		if (allocatedNodes.size() != 0) {
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
		}
		
		//System.out.println(allocatedNodes);
	}
	
}
