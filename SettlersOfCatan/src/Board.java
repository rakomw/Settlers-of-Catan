public class Board {
	public Board() {
		// create the random map
		int tile_count = 0;
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				if (tile_count < 3) {
					tiles[r][c] = new Tile(Tile.BRICK, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 6) {
					tiles[r][c] = new Tile(Tile.ORE, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 10) {
					tiles[r][c] = new Tile(Tile.GRAIN, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 14) {
					tiles[r][c] = new Tile(Tile.WOOL, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 18) {
					tiles[r][c] = new Tile(Tile.LUMBER, (int)(Math.random() * 10) + 2);
				}
				else {
					tiles[r][c] = new Tile(Tile.DESERT, -1);
					tiles[r][c].has_robber = true;
					robber_loc[0] = r;
					robber_loc[1] = c;
				}
				tile_count++;
			}
		}
		
		for (int x = 0; x < 10000; x++) { // switch random tiles to randomize the map
			int[] r = {(int)(Math.random() * 5.0), 
					   (int)(Math.random() * 5.0)};
			
			int[] c = {(int)(Math.random() * (double)tiles[r[0]].length), 
					   (int)(Math.random() * (double)tiles[r[1]].length)};
			
			Tile tile1 = tiles[r[0]][c[0]], 
				 tile2 = tiles[r[1]][c[1]], 
				 temp = new Tile(tile1.resource, tile1.roll);
			
			tile1 = tile2;
			tile2 = temp;
		}
		
		// another way of doing this
		/*int created_tiles = 0;
		for (int r = (int)(Math.random() * 5.0); 
			 created_tiles < total_tiles; 
			 r = (int)(Math.random() * 5.0)) {
			
			for (int c = (int)(Math.random() * (double)tiles[r].length); 
				 created_tiles < total_tiles;
				 c = (int)(Math.random() * 5.0)) {
				
				if (tiles[r][c] == null) {
					int roll = (int)(Math.random() * 10.0) + 2;
					if (created_tiles < 3) {
						tiles[r][c] = new Tile(Tile.ORE, roll);
					}
					else if (created_tiles < 6) {
						tiles[r][c] = new Tile(Tile.BRICK, roll);
					}
					else if (created_tiles < 10) {
						tiles[r][c] = new Tile(Tile.LUMBER, roll);
					}
					else if (created_tiles < 14) {
						tiles[r][c] = new Tile(Tile.GRAIN, roll);
					}
					else if (created_tiles < 18) {
						tiles[r][c] = new Tile(Tile.WOOL, roll);
					}
					else {
						tiles[r][c] = new Tile(Tile.DESERT, roll);
						tiles[r][c].has_robber = true;
						robber_loc[0] = r;
						robber_loc[1] = c;
					}
					created_tiles++;
				}
			}
		}*/
		
		// initialize the road nodes
		
		
		// initialize the town nodes
		
	}
	
	// all array indexes represent row-major item locations on the map
	private Tile[][] tiles = {new Tile[3], new Tile[4], new Tile[5], new Tile[4], new Tile[3]};
	int[] robber_loc = new int[2];
	private static final int total_tiles = 19;
	private RoadNode[][] roads = {new RoadNode[6], new RoadNode[8], new RoadNode[10], new RoadNode[10], new RoadNode[8], new RoadNode[6]};
	private TownNode[][] towns = {new TownNode[7], new TownNode[9], new TownNode[11], new TownNode[11], new TownNode[9], new TownNode[7]};

	public int getResourceAt(int row, int col) {
		return tiles[row][col].resource;
	}
	
	public void moveRobber() {
		
	}
	
	public int[] getRobberLoc() {
		int[] output = {robber_loc[0], robber_loc[1]};
		return output;
	}
}
