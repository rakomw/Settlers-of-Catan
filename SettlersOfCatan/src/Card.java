public class Card {
 
  String suit = "";
 
  public Card(int type) {
    switch(type) {
      case 0: {
	     suit = "knight";
	   }
	   case 1: {
		  suit = "free_point";
	   }
	   case 2: {
		  suit = "monopoly";
	   }
	   case 3: {
		  suit = "roads";
		}
	   case 4: {
		  suit = "free_stuff";
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