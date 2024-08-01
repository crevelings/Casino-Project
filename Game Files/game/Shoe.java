import java.util.Collections;
import java.util.LinkedList;

public class Shoe {
    private final LinkedList<Card> cards;

    public Shoe(int numDecks) {
        cards = new LinkedList<>();
        for (int i = 0; i < numDecks; i++) {
            Deck deck = new Deck();
            cards.addAll(deck.getCards());
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        return cards.removeFirst();
    }

    public int size() {
        return cards.size();
    }

    public LinkedList<Card> getCards() {
        return cards;
    }
}
