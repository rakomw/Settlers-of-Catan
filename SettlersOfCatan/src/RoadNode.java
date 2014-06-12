
public class RoadNode extends Node {
	public RoadNode(RoadNode[] in_roads, TownNode[] in_towns) {
		adj_roads = in_roads;
		adj_towns = in_towns;
	}
	
	public static final int NOTHING = 0;
	public static final int ROAD = 1;
	
	private int level = 0; // 0 for no road, 1 for road
	private final RoadNode[] adj_roads; // adjacent towns and roads
	private final TownNode[] adj_towns;
	
	public boolean buildUp(HumanPlayer in_owner) {
		if (level == 1) {
			return false;
		}
		
		level = 1;
		owner = in_owner;
		return true;
	}
	
	public boolean isBuildable(HumanPlayer prospector) {
		if (level > 0) {
			return false;
		}
		for (RoadNode r : adj_roads) {
			if (r.getBuildLevel() > 0 && r.getOwner() == prospector) {
				return true;
			}
		}
		for (TownNode t : adj_towns) {
			if (t.getBuildLevel() > 0 && t.getOwner() == prospector) {
				return true;
			}
		}
		
		return false;
	}
	
	public RoadNode[] getAdjacentRoads() {
		return adj_roads;
	}
}
