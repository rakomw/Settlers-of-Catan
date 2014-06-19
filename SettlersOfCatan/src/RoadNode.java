
public class RoadNode extends Node {
   public RoadNode(RoadNode[] in_roads, TownNode[] in_towns) {
      adj_roads = in_roads;
      adj_towns = in_towns;
      level=0;
   }
	
   public static final int NOTHING = 0;
   public static final int ROAD = 1;
   
   private final RoadNode[] adj_roads; // adjacent towns and roads
   private final TownNode[] adj_towns;
	
   //only call if isBuildable has verified that it is legal to build, because this doesn't check
   public void buildUp(HumanPlayer in_owner) {
      level = 1;
      owner = in_owner;
   }
	
   public boolean isBuildable(HumanPlayer prospector) {
      if (owner!=null) {
         System.out.println("Attempt to build road on top of another road");
         return false;
      }
      System.out.println("Adj roads:");
      for (RoadNode r : adj_roads) {
         System.out.println(r+" level: "+r.getBuildLevel()+" owner: "+r.getOwner());
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
   //returns whether prospector can build their starting road here
   public boolean startBuildable(HumanPlayer prospector){
      if(level > 0)
         return false;
         
      //can only build starting roads adjacent to owned towns
      for(TownNode t: adj_towns)
         if(t!=null && t.getBuildLevel() > 0 && t.getOwner()==prospector)
            return true;
      
      return false;
   }
   public RoadNode[] getAdjacentRoads() {
      return adj_roads;
   }
   
   public TownNode[] getAdjacentTowns(){
      return adj_towns;
   }
}
