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
	
	private void tradeAndBuild() {
		if (menu instanceof BuildMenu) {
			
		}
		else if (menu instanceof TradeMenu) {
			
		}
	}
}
