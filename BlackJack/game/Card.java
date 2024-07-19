import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    protected ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String value : values) {
                // Construct the image path based on the card's suit and rank
                String imagePath = "BlackJack/cards/" + value + "_of_" + suit + ".png";
                cards.add(new Card(suit, value, imagePath)); // Pass the imagePath
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        return cards.removeFirst();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

}
