public class Card {
 
  String suit = "";
 
  public Card(int type) {
    switch(type) {
      case 0: {
	     suit = "Knight(move the robber and take a card from a player with a settlement adjacent to the new location)";
        break;
	   }
	   case 1: {
		  suit = "Victory point(reveal when you can end the game)";
        break;
	   }
	   case 2: {
		  suit = "Monopoly(take all of the player supply of one resource)";
        break;
	   }
	   case 3: {
		  suit = "Two free roads";
        break;
		}
	   case 4: {
		  suit = "Year of Plenty(take any two resource cards from the bank)";
        break;
	   }
	 }
  }
  public String get_suit() {
    return suit;
  }
  public String toString() {
    return suit + "\n";
  }
}