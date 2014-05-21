import java.lang.Exception;

public class RoadNode extends Node {
	public RoadNode(Node[] in_nodes) {
		adj_nodes = in_nodes;
	}
	
	public static final int NOTHING = 0;
	public static final int ROAD = 1;
	
	private int level = 0; // false for no road, true for road
	private final Node[] adj_nodes; // adjacent towns and roads
	
	public void buildUp(HumanPlayer in_owner) throws Exception {
		if (level == 1) {
			throw new Exception();
		}
		
		level = 1;
		owner = in_owner;
	}
	
	public boolean isBuildable(HumanPlayer prospector) {
		if (level > 0) {
			return false;
		}
		for (Node n : adj_nodes) {
			if (n.getBuildLevel() > 0 && n.getOwner() == prospector) {
				return true;
			}
		}
		
		return false;
	}
}
