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
  public void shuffle() {
    if (cards.size() < MAX) {
      return;
    }
    Random gen = new Random();
    Card[] array = new Card[MAX];
    while (cards.size() > 0) {
      Card card = cards.remove(cards.size() - 1);
	   int i = gen.nextInt(MAX);
      while (array[i] != null) {
        i = gen.nextInt(MAX);
        array[i] = card;
      }
    }
    for (Card card: array) {
      cards.add(card);
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