public class Card {
 
  String suit = "";
  int suit_val;
 
  public Card(int type) {
    suit_val = type;
  }
  public int get_suit() {
    return suit_val;
  }
  public String toString() {
    switch(suit_val) {
      case 0: {
	     suit = "Knight(move the robber and take a card from a player with a settlement adjacent to the new location)";
        break;
	   }
	   case 1: {
		  suit = "Victory point(Gives you one additional point towards victory)";
        break;
	   }
	   case 2: {
		  suit = "Monopoly(take all of the player supply of one resource)";
        break;
	   }
	   case 3: {
		  suit = "Road Building(Two free roads)";
        break;
		}
	   case 4: {
		  suit = "Year of Plenty(take any two resource cards from the bank)";
        break;
	   }
	 }
    return suit + "\n";
  }
}
