import java.util.*;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private Player secondHand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int calculateScore() {
        int score = 0;
        int numberOfAces = 0;

        for (Card card : hand) {
            int cardValue = card.getValue(score);
            if (cardValue == 11) {
                numberOfAces++;
            }
            score += cardValue;
        }

        // Adjust for aces if score is greater than 21
        while (score > 21 && numberOfAces > 0) {
            score -= 10;
            numberOfAces--;
        }

        return score;
    }

    public void displayHand(boolean hideFirstCard) {
        for (int i = 0; i < hand.size(); i++) {
            if (i == 0 && hideFirstCard) {
                System.out.println("Hidden Card");
            } else {
                System.out.println(hand.get(i));
            }
        }
        System.out.println("Total score: " + calculateScore());
    }

    public Player splitHand() {
        // Check if the player can split their hand (i.e., the hand size is 2 and the cards have the same value)
        if (hand.size() == 2) {
            Card firstCard = hand.get(0);
            Card secondCard = hand.get(1);

            // Check if the cards have the same value (including face cards and 10)
            if (firstCard.getValue(0) == secondCard.getValue(0)) {
                // Create a new Player object for the second hand
                secondHand = new Player(name + " (Split)");

                // Move the second card from the original hand to the new hand
                secondHand.addCard(secondCard);
                hand.remove(1); // Remove the second card from the original hand

                // Return the new Player object representing the second hand
                return secondHand;
            }
        }

        // If the hand cannot be split, return null
        return null;
    }

    public Player getSecondHand() {
        return secondHand;
    }
}
