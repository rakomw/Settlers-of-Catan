import java.util.ArrayList;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Color;

public class HumanPlayer {
   private Color color;
   private ArrayList<TownNode> towns;
   private ArrayList<RoadNode> roads;
   private ArrayList<Integer> hand;
   private ArrayList<Card> development;
   private int knights;
   private boolean longest_road;
   private boolean largest_army;
   
   public HumanPlayer(Color in_color) {
      color = in_color;
      towns=new ArrayList<TownNode>();
      roads=new ArrayList<RoadNode>();
      hand=new ArrayList<Integer>();
      development=new ArrayList<Card>();
      knights=0;
      longest_road=false;
      largest_army=false;
   }

   public void resourceProduction(int roll) {
      for (TownNode town : towns) {
         for (Tile tile : town.getAdjacentTiles()) {
            if (tile.roll == roll && !tile.has_robber) {
               if (town.getBuildLevel() == 2) {
                  hand.add(tile.resource);
               }
               hand.add(tile.resource);
            }
         }
      }
   }
   public ArrayList<Integer> get_ports() {
      ArrayList<Integer> ports = new ArrayList<Integer>();
      for(TownNode t: towns){
         if (t.get_trader()) {
            ports.add(t.get_trade_stats());
         }
      }
      return ports;
   }

   public ArrayList<TownNode> getTowns(){
      return towns;
   }
   public void add_knight() {
   	knights++;
   }
   public void free_points_plus() {
     free_points++;
   }
   public int get_num_roads() {
      return roads.size();
   }
   public int get_num_knights() {
      return knights;
   }
   public int get_points() {
      int points = 0;
      for (int i=0;i<towns.size();i++) 
         points += towns.get(i).getBuildLevel();	
      
      if (longest_road) 
         points+=2;
      
      if (largest_army) 
         points+=2;
      
      return points;
   }
	//private void tradeAndBuild() { //use this to call menus to get inputs for seperate helper functions?
     //call menus as players want and get function calls for below
	//}
   public void build_road(RoadNode rode) {
      rode.buildUp(this);
   }
   public void build_town(TownNode toun) {
      toun.buildUp(this);  
   }
	
   public boolean trade(int[] to_remove,int[] addition){
      if(hasResources(to_remove)){
         for(int i:to_remove)
            hand.remove(Integer.valueOf(i));
            /*Integer.valueOf is used to make an Integer object and prevent Arraylist
            from interpreting it as an index*/
         for(int i:addition)
            hand.add(i);
            
         return true;
      }
      else 
         return false;
   }
   
   public boolean hasResources(int[] removing){
      int[] count_taking = {0,0,0,0,0};
      int[] count_possessed = {0,0,0,0,0};
      for(int i:removing)
         count_taking[i]++;
      for(Integer i:hand)
         count_possessed[i]++;
      
      for(int k=0;k<count_taking.length;k++)
         if(count_taking[k]>count_possessed[k])
            return false;
            
      return true;
   }
   public void addDevelopmentCard(Card dev) {
      development.add(dev);
   }
   public ArrayList<Integer> getResourceCards(){
      return hand;
   }
   public ArrayList<Card> getDevelopmentCards(){
      return development;
   }
	
   public String toString(){
      String name = "The ";
      
      if(color.equals(Color.RED))
         name+="Red ";
      else if(color.equals(Color.GREEN))
         name+="Green ";
      else if(color.equals(Color.BLUE))
         name+="Blue ";
      else if(color.equals(Color.ORANGE))
         name+="Orange ";
         
      name+="Player";
      return name;
   }
   
}
