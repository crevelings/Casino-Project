public class Card {
    protected final String suit;
    protected final String rank;
    protected String imagePath;

    public Card(String suit, String rank, String imagePath) { // Modify the constructor
        this.suit = suit;
        this.rank = rank;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String toString() {
        return rank + " of " + suit;
    }

    public int getValue(int currentHandScore) {
        if (rank.equals("Ace")) {
            // Determine the value of Ace (1 or 11) based on the current hand score
            if (currentHandScore + 11 <= 21) {
                return 11; // Return 11 if it doesn't cause a bust
            } else {
                return 1; // Return 1 if returning 11 would cause a bust
            }
        } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }
}
