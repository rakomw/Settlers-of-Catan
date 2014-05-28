public class Card {
 
  String suit = "";
 
  public Card(int type) {
    switch(type) {
      case 0: {
	     suit = "knight";
        break;
	   }
	   case 1: {
		  suit = "free_point";
        break;
	   }
	   case 2: {
		  suit = "monopoly";
        break;
	   }
	   case 3: {
		  suit = "roads";
        break;
		}
	   case 4: {
		  suit = "free_stuff";
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