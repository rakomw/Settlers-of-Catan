import java.util.*;

public class Deck {
  public static final int MAX = 25; //constant so we can change as needed if we make expansions
  private ArrayList<Card> cards;
   
  public Deck() {
    reset();
  }
  public void reset() {
    cards = new ArrayList<Card>();
    for (int i=0;i<15;i++) {
	   cards.add(new Card(0));
	 }
	 for (int i=0;i<5;i++) {
      cards.add(new Card(1));
	 }
    cards.add(new Card(2));			
    cards.add(new Card(2));
    cards.add(new Card(3));
    cards.add(new Card(3));
    cards.add(new Card(4));
	 cards.add(new Card(4));
    shuffle();
  }
  public boolean isEmpty() {
    return cards.isEmpty();
  }
  public int size() {
    return cards.size();
  }
  public Card deal() {
    if (isEmpty()) {
      return null;
    }
    else return cards.remove(cards.size() - 1);
  }
  public ArrayList<Card> deal(int n){
      ArrayList<Card> a = new ArrayList<Card>();
      for(int k=0;k<n;k++)
         a.add(cards.remove(cards.size()-1));
      return a;
  }
  public void shuffle() {
      for(int k=0;k<10000;k++){
         int a= (int)(Math.random()*cards.size());
         int b= (int)(Math.random()*cards.size());
         Card temp = cards.get(a);
         
         cards.set(a,cards.get(b));
         cards.set(b,temp);
      }
  }
  public String toString() {
    String result = "";
    for (Card card : cards) {
      result += card + "\n";
	 }
    return result;
  }
}