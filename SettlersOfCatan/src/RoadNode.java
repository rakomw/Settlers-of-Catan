import java.lang.Exception;

public class RoadNode extends Node {
	public RoadNode(RoadNode[] in_roads) {
		adj_roads = in_roads;
	}
	
	public static final int NOTHING = 0;
	public static final int ROAD = 1;
	
	private int level = 0; // false for no road, true for road
	private final RoadNode[] adj_roads;
	
	public void buildUp(HumanPlayer in_owner) throws Exception {
		if (level == 1) {
			throw new Exception();
		}
		
		level = 1;
		owner = in_owner;
	}
	
	public boolean isBuildable(HumanPlayer prospector) {
		if (level == 1) {
			return false;
		}
		for (RoadNode r : adj_roads) {
			if (r.getBuildLevel() == 1 && r.getOwner() == prospector) {
				return true;
			}
		}
		
		return false;
	}
}
