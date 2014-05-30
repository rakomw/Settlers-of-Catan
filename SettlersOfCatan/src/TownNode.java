
public class TownNode extends Node {
	public TownNode(Tile[] in_tiles, RoadNode[] in_roads, TownNode[] in_towns,boolean trader,int trader_stats) {
		adj_tiles = in_tiles;
		adj_roads = in_roads;
		adj_towns = in_towns;
		is_trader = trader;
		trade_stats = trader_stats;
	}
	
	public static final int NOTHING = 0;
	public static final int TOWN = 1;
	public static final int CITY = 2;
	
	private final Tile[] adj_tiles;
	private final RoadNode[] adj_roads;
	private final TownNode[] adj_towns;
	private boolean is_trader;
	private int trade_stats;
	
	public boolean buildUp(HumanPlayer in_owner) {
		if (level < 3) {
			level++;
		}
		else {
			return false;
		}
		
		if (owner == null) {
			owner = in_owner;
		}
		else { // a previously built settlement is being replaced with a city of a diff owner
			return false;
		}
		
		return true;
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
	public boolean get_trader() {
	  return is_trader;
	}
   public int get_trade_stats() {
	  return trade_stats;//0-4 commodities 2/1 and 5 is general 3/1
	}
   public int get_level(){
      return level;
   }
}
