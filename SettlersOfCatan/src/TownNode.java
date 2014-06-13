public class TownNode extends Node {
	// if the town has a trader
	public TownNode(Tile[] in_tiles, RoadNode[] in_roads, TownNode[] in_towns, int trader_stats) {
		adj_tiles = in_tiles;
		adj_roads = in_roads;
		adj_towns = in_towns;
		is_trader = true;
		trade_stats = trader_stats;
	}
	public TownNode(Tile[] in_tiles, RoadNode[] in_roads, TownNode[] in_towns) {
		adj_tiles = in_tiles;
		adj_roads = in_roads;
		adj_towns = in_towns;
		is_trader = false;
		trade_stats = -1;
	}
	
	public static final int NOTHING = 0;
	public static final int TOWN = 1;
	public static final int CITY = 2;
	
	private final Tile[] adj_tiles;
	private final RoadNode[] adj_roads;
	private final TownNode[] adj_towns;
	private final boolean is_trader;
	private final int trade_stats;
	
	public void buildUp(HumanPlayer in_owner) {
      if(level==0)
         owner=in_owner;
      level++;
   }
	
	// returns true if there is an adjacent road owned by the same player
	public boolean isBuildable(HumanPlayer prospector) {
      
		if (level >= 2 || (owner!=null && !owner.equals(prospector))) {
			return false;
		}
		for (TownNode t : adj_towns) {
			if (t.getBuildLevel() > 0) { // there is an adjacent town or city
				return false;
			}
		}
		for (RoadNode r : adj_roads) {
			if (r.getBuildLevel() == 1 && r.getOwner()!=null && r.getOwner() == prospector) {
				return true;
			}
		}
		
		return false;
	}
	
   //returns whether or not the node is a valid location for a player's first town
   public boolean startBuildable(HumanPlayer prospector){
      if(owner!=null)//can only build on an empty space
         return false;
      for (TownNode t : adj_towns) {
			if (t.getBuildLevel() > 0) { // there is an adjacent town or city
				return false;
			}
		}
      return true;
   }
	// TownNode specific functions
	public Tile[] getAdjacentTiles() {
		return adj_tiles;
	}
	public Tile getAdjacentTile(int index) {
		return adj_tiles[index];
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
