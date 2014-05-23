import java.util.ArrayList;
import java.awt.Color;

public class HumanPlayer {
	public HumanPlayer(Color in_color, Menu in_menu) {
		color = new Color(in_color.getRGB());
		menu = in_menu;
	}
	
	public void takeTurn() {
		resourceProduction();
		tradeAndBuild();
	}
	
	public final Color color;
	private ArrayList<TownNode> towns = new ArrayList<TownNode>();
	private ArrayList<RoadNode> roads = new ArrayList<RoadNode>();
	private ArrayList<Integer> hand = new ArrayList<Integer>();
   private ArrayList<Card> development = new ArrayList<Card>();
	private Menu menu;
	private static Deck deck = new Deck();
	
	private void resourceProduction() {
		int roll = (int)(Math.random() * 6) + (int)(Math.random() * 6) + 2;
		for (TownNode town : towns) {
			for (Tile tile : town.getAdjacentTiles()) {
				if (tile.roll == roll) {
				  if (town.getBuildLevel() == 2) {
				    hand.add(tile.resource);
				  }
					hand.add(tile.resource);
				}
			}
		}
	}
	
	private void tradeAndBuild() { //use this to call menus to get inputs for seperate helper functions?
     //call menus as players want and get function calls for below
	}
	private void build_road(RoadNode rode) {
	    try {
	    rode.buildUp(this);
		 }
		 catch(Exception e) {
		 
		 }
	}
	private void build_town(TownNode toun) {
	    try { 
		 toun.buildUp(this);
		 }
		 catch(Exception e) {
		 
		 }
	}
	//@param whether or not the trade can be done
	private boolean trade(int[] to_remove,int[] addition) throws Exception {
	  boolean has_commodity = false;
	  for (int i=0;i<to_remove.length;i++) {
	    for (int j=0;j<hand.size();j++) {
	      if (to_remove[i] == hand.get(j)) {
		     has_commodity = true;
		  	  to_remove[i] = j;
		   }
	    }
	  }
	  if (has_commodity) {
	    for (int i=0;i<to_remove.length;i++) {
	      hand.remove(to_remove[i]);
		 }
		 for (int i=0;i<addition.length;i++) {
		   hand.add(addition[i]);
		 }
		 return true;
	  }
	  else {
	    throw new Exception();
	  }
	}
	private void build_card() throws Exception {
	  if (!deck.isEmpty()) {
	    development.add(deck.deal());
	  }
	  else throw new Exception();
	}
	/*private void build_ship() {
	
	} maybe???*/
}