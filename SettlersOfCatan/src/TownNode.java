import java.lang.Exception;

public class TownNode extends Node {
	public TownNode(Tile[] in_tiles, RoadNode[] in_roads, TownNode[] in_towns) {
		adj_tiles = in_tiles;
		adj_roads = in_roads;
		adj_towns = in_towns;
	}
	
	public static final int NOTHING = 0;
	public static final int TOWN = 1;
	public static final int CITY = 2;
	
	private final Tile[] adj_tiles;
	private final RoadNode[] adj_roads;
	private final TownNode[] adj_towns;
	
	public void buildUp(HumanPlayer in_owner) throws Exception {
		if (level < 3) {
			level++;
		}
		else {
			throw new Exception();
		}
		
		if (owner == null) {
			owner = in_owner;
		}
		else { // a previously built settlement is being replaced with a city of a diff owner
			throw new Exception();
		}
	}
	
	// returns true if there is an adjacent road owned by the same player
	public boolean isBuildable(HumanPlayer prospector) {
		if (level >= 3) {
			return false;
		}
		for (TownNode t : adj_towns) {
			if (t.getBuildLevel() > 0) { // there is an adjacent town or city
				return false;
			}
		}
		for (RoadNode r : adj_roads) {
			if (r.getBuildLevel() == 1 && r.getOwner() == prospector) {
				return true;
			}
		}
		
		return false;
	}
	
	// TownNode specific functions
	public Tile[] getAdjacentTiles() {
		Tile[] output = new Tile[adj_tiles.length];
		// prevent return by reference
		for (int x = 0; x < output.length; x++) {
			output[x] = new Tile(adj_tiles[x].resource, adj_tiles[x].roll);
		}
		
		return output;
	}
	public Tile getAdjacentTile(int index) {
		// prevent returning by reference
		return new Tile(adj_tiles[index].resource, adj_tiles[index].roll);
	}
}
