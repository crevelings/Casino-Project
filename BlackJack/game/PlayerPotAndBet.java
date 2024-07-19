public class PlayerPotAndBet {
    private int pot; // Change to int
    private int bet; // Change to int
    protected static final int POT_CAP = 10000; // Maximum allowed pot size

    public PlayerPotAndBet() {
        // Constructor logic (if any)
        this.pot = 0;
        this.bet = 0;
    }

    // Method to set the player's pot amount
    public void setPot(int pot) {
        if (pot > POT_CAP) {
            System.out.println("The pot amount cannot exceed $10,000. Setting it to the maximum cap.");
            this.pot = POT_CAP;
        } else {
            this.pot = pot;
        }
    }

    // Getter for the pot
    public int getPot() {
        return pot;
    }

    // Method to set the player's bet amount
    public void setBet(int bet) {
        // Ensure bet is non-negative and does not exceed the player's pot
        if (bet < 0) {
            System.out.println("Invalid bet amount. Bet cannot be negative.");
        } else if (bet > pot) {
            System.out.println("Bet cannot exceed your pot amount. Adjusting bet to your pot.");
            this.bet = pot;
        } else {
            this.bet = bet;
        }
    }

    // Getter for the bet
    public int getBet() {
        return bet;
    }

    // Method to deduct the bet from the pot
    public void deductBet() {
        pot -= bet;
    }

    // Method to add the bet to the pot and enforce the pot cap
    public void addBet() {
        pot += bet;
    }

    // Method to push the bet back to the pot
    public void pushBet() {
        pot += bet;
    }

    // Method to double the bet and deduct it from the pot
    public void doubleBet() {
        pot -= bet;
        bet *= 2;
    }

    // Method to display the player's current pot
    public void displayPot() {
        System.out.println("Your current pot: $" + pot);
    }

    public void deductFromPot(int playerBet) {
        pot -= playerBet;
    }

    public void addBlackJack() {
        pot += (bet * 1.5);
    }
}
