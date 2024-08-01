import javax.swing.*;

public class InsuranceBetHandler {
    private final BlackJackGame game;
    private final PlayerPotAndBet playerAccount;

    public InsuranceBetHandler(BlackJackGame game, PlayerPotAndBet playerAccount) {
        this.game = game;
        this.playerAccount = playerAccount;
    }

    public void promptInsuranceBet() {
        int insuranceBet = game.playerAccount.getBet() / 2;
        int playerChoice = JOptionPane.showConfirmDialog(
                game,
                "The dealer's revealed card is an Ace. Do you want to place an insurance bet of $" + insuranceBet + "?",
                "Insurance Bet",
                JOptionPane.YES_NO_OPTION
        );

        if (playerChoice == JOptionPane.YES_OPTION) {
            playerAccount.deductFromPot(insuranceBet);

            // Check if dealer has blackjack
            if (game.dealer.calculateScore() == 21) {
                game.dealerFirstCardHidden = false; // Reveal dealer's first card
                game.updateHandLabels();
                game.getStatusLabel().setText("Dealer has Blackjack! You win the insurance bet.");
                playerAccount.addBet(); // Winning the insurance bet
                game.endRound();
            } else {
                game.getStatusLabel().setText("Dealer does not have Blackjack. You lose the insurance bet.");
                game.resumeRound();
            }
        } else {
            // Check if dealer has blackjack
            if (game.dealer.calculateScore() == 21) {
                game.dealerFirstCardHidden = false; // Reveal dealer's first card
                game.updateHandLabels();
                game.getStatusLabel().setText("Dealer has Blackjack! You lose your bet.");
                playerAccount.deductBet();
                game.endRound();
            } else {
                game.resumeRound();
            }
        }
    }
}