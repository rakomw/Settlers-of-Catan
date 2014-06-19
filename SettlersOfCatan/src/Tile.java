 public class Tile {
	public Tile(int in_resource, int in_chance) {
		resource = in_resource;
		roll = in_chance;
      has_robber=false;
	}
	
	public final int resource;
	public final int roll;
	private boolean has_robber;

	public static final int LUMBER = 0;
	public static final int ORE = 1;
	public static final int BRICK = 2;
	public static final int WOOL = 3;
	public static final int GRAIN = 4;
	public static final int DESERT = 5;
   public static final int WATER = 6;
   
   public boolean hasRobber(){
      return has_robber;
   }
   public void giveRobber(boolean b){
      has_robber=b;
   }
   public String toString(){
      return "Resource: " + GameMenuBar.translate(resource) + "\nRoll: " + roll + "\nRobber: " + has_robber;
   }
}
