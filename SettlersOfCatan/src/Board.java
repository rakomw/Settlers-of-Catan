public class Board {
	public Board() {
		// create the random map
		int tile_count = 0, r = 0, c = 0;
		for (; r < tiles.length; r++) {
			for (; c < tiles[r].length; c++) {
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
			int[] rows = {(int)(Math.random() * 5.0), 
					   (int)(Math.random() * 5.0)};
			
			int[] cols = {(int)(Math.random() * (double)tiles[rows[0]].length), 
					   (int)(Math.random() * (double)tiles[rows[1]].length)};
			
			Tile tile1 = tiles[rows[0]][cols[0]], 
				 tile2 = tiles[rows[1]][cols[1]], 
				 temp = new Tile(tile1.resource, tile1.roll);
			
			tile1 = tile2;
			tile2 = temp;
		}
		
		// initialize the road nodes
		RoadNode[][][] constructors = new RoadNode[roads.length][][];
		for (r = 0; r < roads.length; r++) {
			constructors[r] = new RoadNode[roads[r].length][]; // instantiate the varying length of the columns
		}
		
		for (r = 0; r < roads.length; r++) {
			for (c = 0; c < roads[r].length; r++) {
				roads[r][c] = new RoadNode(constructors[r][c]);
			}
		}
		
		int length;
		for (r = 0; r < roads.length; r ++) {
			for (c = 0; c < roads[r].length; c++) {
				// find the correct size of the array
				length = 4;
				if (c == 0 || c == roads[r].length - 1) {
					length--;
				}
				if (r == 0 || r == roads.length - 1) {
					length--;
				}
				constructors[r][c] = new RoadNode[length];
			}
		}
		
		// construct vertical roads
		for (r = 1; r < roads.length; r += 2) {
			for (c = 1; c < roads[r].length; c++) {
				constructVerticals(r, c, constructors);
			}
		}
		
		// construct horizontal roads
		for (r = 0; r < roads.length; r += 2) {
			for (c = 0; c < roads[r].length; c++) {
				constructHorizontals(r, c, constructors);
			}
		}
		// initialize the town nodes
		
	}
	
	// these methods are used to ease construction of roads
	
	// constructs vertical references and references of adjacent horizontals to the vertical road
	void constructVerticals(int r, int c, RoadNode[][][] constructors) {
		int index = 0, // used to add to the road's array
			other_r, other_c; // used to store the other's location
		if (r < 5) {
			other_r = r - 1;
			other_c = c * 2;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++; // other_c = c * 2 + 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_r = r + 1; // other_c = c * 2 + 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++; // other_c = c * 2 + 2
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				//index++; don't need this, function ends
			}
		}
		else if (r == 5) {
			other_r = r - 1;
			other_c = c * 2 - 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_r = r + 1;
			other_c = c * 2 - 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				//index++; not needed, function ends
			}
		}
		else {
			other_r = r - 1;
			other_c = c * 2 + 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++; // other_c = c * 2 + 2;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_r = r + 1;
			other_c = c * 2;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				index++;
			}
			other_c++; // other_c = c * 2 + 1;
			if (onBoard(other_r, other_c)) {
				linkReference(r, c, other_r, other_c, index, constructors);
				//index++; not needed, function ends
			}
		}
	}
	// completes the references of horizontal roads
	private void constructHorizontals(int r, int c, RoadNode[][][] constructors) {
		RoadNode[] references = constructors[r][c];
		int x;
		if (onBoard(r, c - 1)) {
			for (x = 0; x < references.length; x++) {
				if (references[x] == null) {
					references[x] = roads[r][c - 1];
				}
			}
		}
		if (onBoard(r, c + 1)) {
			for (x = 0; x < references.length;  x++) {
				if (references[x] == null) {
					references[x] = roads[r][c - 1];
				}
			}
		}
	}
	
	private boolean onBoard(int r, int c) {
		return r >= 0 || r < roads.length || c >= 0 || c < roads[r].length;
	}
	private void linkReference(int r, int c, int other_r, int other_c, final int cur_adj,
							   RoadNode[][][] constructors) {
		RoadNode[] others_constructors;
		
		constructors[r][c][cur_adj] = roads[other_r][other_c];
		others_constructors = constructors[other_r][other_c];
		for (int x = 0; x < others_constructors.length; x++) {
			if (others_constructors[x] == null) {
				others_constructors[x] = roads[r][c];
				break;
			}
		}
	}
	
	// all array indexes represent row-major item locations on the map
	private Tile[][] tiles = {new Tile[3], new Tile[4], new Tile[5], new Tile[4], new Tile[3]};
	int[] robber_loc = new int[2];
	private static final int total_tiles = 19;
	//even rows are diagonal, odd are vertical
	private RoadNode[][] roads = {new RoadNode[6], new RoadNode[4], 
								  new RoadNode[8], new RoadNode[5], 
								  new RoadNode[10], new RoadNode[6], 
								  new RoadNode[10], new RoadNode[5],
								  new RoadNode[8], new RoadNode[4],
								  new RoadNode[6]};
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
