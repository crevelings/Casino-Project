import java.util.Scanner;

public class BlackJack {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello and welcome to BlackJack!");

        char playAgain;
        do {
            // Shuffle deck
            Deck deck = new Deck();
            deck.shuffle();

            // Set up player and dealer characters
            Player player = new Player("Player");
            Player dealer = new Player("Dealer");


            // Deal initial cards to player and dealer
            dealer.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());

            System.out.println("Dealer's Hand:");
            dealer.displayHand(true);
            System.out.println("Player's Hand:");
            player.displayHand(false);

            // Player's turn
            while (true) {
                System.out.println("\nDo you want to hit or stand? (h/s)");
                char choice = scanner.nextLine().charAt(0);
                if (choice == 'h') {
                    player.addCard(deck.dealCard());
                    System.out.println("Player's Hand:");
                    player.displayHand(false);
                    if (player.calculateScore() > 21) {
                        System.out.println("Bust! You lose this hand.");
                        break;
                    }
                } else if (choice == 's') {
                    break;
                } else {
                    System.out.println("Invalid choice. Please enter 'h' or 's'.");
                }
            }

            // Dealer's turn
            while (dealer.calculateScore() < 17) {
                dealer.addCard(deck.dealCard());
            }
            System.out.println("\nDealer's Hand:");
            dealer.displayHand(false);

            System.out.println("\nPlayer's Hand:");
            player.displayHand(false);

            // Determine the outcome of the hand
            int dealerScore = dealer.calculateScore();
            int playerScore = player.calculateScore();

            if (dealerScore > 21) {
                System.out.println("Dealer busted! You win this hand.");
            } else if (playerScore > 21) {
                System.out.println("You busted! Dealer wins this hand.");
            } else if (dealerScore > playerScore) {
                System.out.println("Dealer wins this hand.");
            } else if (dealerScore < playerScore) {
                System.out.println("You win this hand!");
            } else {
                System.out.println("It's a tie!");
            }

            // Ask the player if they want to play another round
            System.out.println("\nDo you want to play another round? (y/n)");
            playAgain = scanner.nextLine().charAt(0);
        } while (playAgain == 'y' || playAgain == 'Y');

        System.out.println("Thanks for playing!");
    }
}
