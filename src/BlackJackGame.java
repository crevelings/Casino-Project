import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;


public class BlackJackGame extends JFrame {
    // GUI components
    private final JLabel dealerHandLabel;
    private final JLabel playerHandLabel;
    private final JLabel statusLabel;
    private final JLabel potLabel;
    private final JLabel playerBetLabel;
    private final JButton hitButton;
    private final JButton standButton;
    private final JButton doubleDownButton;
    private final JButton playAgainButton;
    private final JButton quitGameButton;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
//    private final JPanel gamePanel;

    // Card Panels
    private final JPanel dealerCardPanel;
    private final JPanel playerCardPanel;

    // Game state
    private Deck deck;
    public Player player;
    public Player dealer;
    protected PlayerPotAndBet playerAccount;
    private final InsuranceBetHandler insuranceBetHandler;
    protected boolean dealerFirstCardHidden = true;
    protected int currentHand = 0;
    private int playerBet;
    private int lastBet = 0;

    public BlackJackGame(PlayerPotAndBet playerAccount) {
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("backgrounds/default.jpg")); // Load your background image here
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.playerAccount = playerAccount;
        this.insuranceBetHandler = new InsuranceBetHandler(this, playerAccount);
        Shoe shoe = new Shoe(6); // Create a shoe with 6 decks

        // Set up the frame
        setTitle("BlackJack Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the card layout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create the main game panel with a BorderLayout
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.setOpaque(false);

        // Create the dealer panel
        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setOpaque(false);
        dealerHandLabel = new JLabel("Dealer's Hand:");
        dealerHandLabel.setOpaque(true);
        dealerCardPanel = new JPanel(new FlowLayout());
        dealerCardPanel.setOpaque(false);
        dealerPanel.add(dealerHandLabel, BorderLayout.NORTH);
        dealerPanel.add(dealerCardPanel, BorderLayout.CENTER);

        // Create the player panel
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setOpaque(false);
        playerHandLabel = new JLabel("Player's Hand:");
        playerHandLabel.setOpaque(true);
        playerCardPanel = new JPanel(new FlowLayout());
        playerCardPanel.setOpaque(false);
        playerPanel.add(playerHandLabel, BorderLayout.NORTH);
        playerPanel.add(playerCardPanel, BorderLayout.CENTER);

        // Create the status panel
        JPanel statusPanel = new JPanel(new GridLayout(1, 3));
        statusPanel.setOpaque(false);
        statusLabel = new JLabel("Welcome to BlackJack!", SwingConstants.CENTER);
        potLabel = new JLabel("Your Pot: $" + playerAccount.getPot(), SwingConstants.CENTER);
        playerBetLabel = new JLabel("Player's Bet: $" + playerAccount.getBet(), SwingConstants.CENTER);
        statusPanel.add(statusLabel);
        statusPanel.add(potLabel);
        statusPanel.add(playerBetLabel);

        // Create the control panel for buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout());
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        doubleDownButton = new JButton("Double Down");
        playAgainButton = new JButton("Play Again");
        quitGameButton = new JButton("Quit Game");
        playAgainButton.setEnabled(false);
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        controlPanel.add(doubleDownButton);
        controlPanel.add(playAgainButton);
        controlPanel.add(quitGameButton);

        // Add action listeners
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerHits();
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerStands();
            }
        });

        doubleDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDoublesDown();
            }
        });

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewRound();
            }
        });

        quitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showThanksForPlaying();
            }
        });

        pack();

        // Add the gear icon button to the control panel
        ImageIcon gearIcon = new ImageIcon("Gear-icon.png");
        Image originalImage = gearIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JButton optionsButton = new JButton(resizedIcon);
        optionsButton.setPreferredSize(new Dimension(50, 50));
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "OPTIONS");
            }
        });
        controlPanel.add(optionsButton);

        // Add panels to the main game panel
        mainGamePanel.add(dealerPanel, BorderLayout.WEST);
        mainGamePanel.add(playerPanel, BorderLayout.EAST);
        mainGamePanel.add(statusPanel, BorderLayout.CENTER);
        mainGamePanel.add(controlPanel, BorderLayout.SOUTH);

        // Create the BackgroundPanel and add the main game panel to it
        BackGroundPanel backgroundPanel = new BackGroundPanel(backgroundImage);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainGamePanel, BorderLayout.CENTER);

        // Add the BackgroundPanel to the card panel
        cardPanel.add(backgroundPanel, "GAME");

        // Create the options menu and add it to the card panel
        OptionsMenu optionsMenu = new OptionsMenu(cardPanel, cardLayout, backgroundPanel);
        cardPanel.add(optionsMenu, "OPTIONS");

        // Add the card panel to the frame
        add(cardPanel);

        // Display the game frame
        setVisible(true);

        // Initialize the game state and start the first round
        startNewRound();

        // Set up the frame size and visibility
        setSize(950, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Add the buttons with random images
        addRandomImageButtons();
    }


    // Method to add buttons with random images
    private void addRandomImageButtons() {
        // Random number generator
        Random rand = new Random();

        // List to store available card indices
        ArrayList<Integer> availableIndices = new ArrayList<>();
        for (int i = 0; i < GridBagCardDisplay.CARD_IMAGES.length; i++) {
            availableIndices.add(i);
        }

        // Create and add buttons with random images
        for (int i = 0; i < 5; i++) {
            int randomIndex = rand.nextInt(availableIndices.size());
            int imageIndex = availableIndices.remove(randomIndex);
            String imagePath = GridBagCardDisplay.CARD_IMAGES[imageIndex];
            ImageIcon icon = new ImageIcon(imagePath);
            JButton button = new JButton(icon);
            add(button);
        }
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    // Method to start a new round
    protected void startNewRound() {
        // Initialize the game state
        deck = new Deck();
        deck.shuffle();
        player = new Player("Player");
        dealer = new Player("Dealer");

        // Reset dealerFirstCardHidden to true at the beginning of each round
        dealerFirstCardHidden = true;

        // Prompt for initial bet
        int playerBet = promptForBet();

        // Deduct initial bet from the pot
        playerAccount.deductFromPot(playerBet);

        // Set the initial bet
        playerAccount.setBet(playerBet);

        // Update bet label after setting the bet
        SwingUtilities.invokeLater(() -> playerBetLabel.setText("Player's Bet: $" + playerAccount.getBet()));

        // Deal initial cards to player and dealer
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        player.addCard(deck.dealCard());

        // Reset the GUI components for a new round
        SwingUtilities.invokeLater(() -> {
            dealerCardPanel.removeAll();
            playerCardPanel.removeAll();

            // Revalidate panels
            dealerCardPanel.revalidate();
            playerCardPanel.revalidate();
            dealerCardPanel.repaint();
            playerCardPanel.repaint();

            // Update hands and status
            updateHandLabels();
            statusLabel.setText("Hit or Stand?");
        });

        // Check for immediate Blackjack
        if (player.calculateScore() == 21) {
            // Player has Blackjack, directly check dealer's hand
            endRound();
            return;
        }

        // Check if dealer's revealed card is an Ace
        if (dealer.getHand().get(1).getValue(0) == 11) {
            // Prompt for insurance bet
            insuranceBetHandler.promptInsuranceBet();
            return;
        }

        // Reset buttons
        playAgainButton.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleDownButton.setEnabled(true);

        // Shows the original hand label
        playerHandLabel.setVisible(true);

        // Reset current hand to 0
        currentHand = 0;

        // Display initial hands
        updateHandLabels();
        statusLabel.setText("Hit or Stand?");
    }

    // Update the updateCardPanel method to display dealer's and player's cards in separate panels
    private void updateCardPanel(JPanel cardPanel, ArrayList<Card> hand, boolean hideFirstCard) {
        cardPanel.removeAll(); // Clear previous images
        for (int i = 0; i < hand.size(); i++) {
            if (i == 0 && hideFirstCard) {
                // Add a JLabel with a placeholder image for the hidden card
                ImageIcon originalIcon = new ImageIcon("cards/card back blue.png");
                Image scaledImage = originalIcon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH); // Resize image
                JLabel label = new JLabel(new ImageIcon(scaledImage));
                cardPanel.add(label);
            } else {
                // Add a JLabel with the card image to the panel
                Card card = hand.get(i);
                ImageIcon originalIcon = new ImageIcon(card.getImagePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(60, 90, Image.SCALE_SMOOTH); // Resize image
                JLabel label = new JLabel(new ImageIcon(scaledImage));
                cardPanel.add(label);
            }
        }
        cardPanel.revalidate(); // Refresh panel to show new images
        cardPanel.repaint();
    }

    protected void resumeRound() {
        // Reset buttons
        playAgainButton.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleDownButton.setEnabled(true);

        // Shows the original hand label
        playerHandLabel.setVisible(true);

        // Reset current hand to 0
        currentHand = 0;

        // Display hands
        updateHandLabels();
        statusLabel.setText("Do you want to hit or stand?");
    }

    // Method to prompt the player for an initial bet
    protected int promptForBet() {
        playerBet = 0;

        // Initialize the bet frame
        JFrame betFrame = new JFrame("Current Bet");
        betFrame.setSize(200, 100);
        betFrame.setLayout(new BorderLayout());
        JLabel betAmountLabel = new JLabel("Current Bet: $0", SwingConstants.CENTER);
        betFrame.add(betAmountLabel, BorderLayout.CENTER);
        betFrame.setAlwaysOnTop(true);
        betFrame.setLocation(this.getX(), this.getY());
        betFrame.setVisible(true);

        JFrame potFrame = new JFrame("Current Pot");
        potFrame.setSize(200, 100);
        potFrame.setLayout(new BorderLayout());
        JLabel potAmountLabel = new JLabel("Current Pot: $" + playerAccount.getPot(), SwingConstants.CENTER);
        potFrame.add(potAmountLabel, BorderLayout.CENTER);
        potFrame.setAlwaysOnTop(true);
        potFrame.setLocation(this.getX(), this.getY() + 100);
        potFrame.setVisible(true);

        // Initialize the bet dialog
        JDialog betDialog = new JDialog(this, "Bet Amount", true);
        betDialog.setLayout(new GridLayout(3, 3));
        betDialog.setSize(400, 300);
        betDialog.setLocation(this.getX() + 200, this.getY());

        // Create buttons for bet amounts
        JButton button1 = createBetButton(1, betAmountLabel);
        JButton button5 = createBetButton(5, betAmountLabel);
        JButton button10 = createBetButton(10, betAmountLabel);
        JButton button25 = createBetButton(25, betAmountLabel);
        JButton button50 = createBetButton(50, betAmountLabel);
        JButton button100 = createBetButton(100, betAmountLabel);

        // Create reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            playerBet = 0;
            betAmountLabel.setText("Current Bet: $" + playerBet);
        });

        // Create confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            betDialog.dispose();
            betFrame.dispose();
        });

        // Create repeat button
        JButton repeatButton = new JButton("Repeat Bet");
        repeatButton.addActionListener(e -> {
            playerBet = lastBet;
            betAmountLabel.setText("Current Bet: $" + playerBet);
            betDialog.dispose();
            betFrame.dispose();
        });

        // Add buttons to dialog
        betDialog.add(button1);
        betDialog.add(button5);
        betDialog.add(button10);
        betDialog.add(button25);
        betDialog.add(button50);
        betDialog.add(button100);
        betDialog.add(resetButton);
        betDialog.add(confirmButton);
        betDialog.add(repeatButton);

        betDialog.setVisible(true);

        // Update the last bet amount after the dialog is closed
        lastBet = playerBet;

        potFrame.setVisible(false);

        return playerBet;
    }

    // Method to create bet buttons
    private JButton createBetButton(int amount, JLabel betAmountLabel) {
        JButton button = new JButton("$" + amount);
        button.addActionListener(e -> {
            playerBet += amount;
            betAmountLabel.setText("Current Bet: $" + playerBet);
        });
        return button;
    }

    // Method for handling player's hit action
    protected void playerHits() {
        player.addCard(deck.dealCard());
        updateHandLabels();
        doubleDownButton.setEnabled(false);
        // Check for bust
        if (player.calculateScore() > 21) {
            statusLabel.setText("Bust! Dealer wins.");
            dealerFirstCardHidden = false; // Reveal dealer's first card
            endRound();
        }
    }

    // Method for handling player's stand action
    protected void playerStands() {
        dealerFirstCardHidden = false;

        // Let the dealer play
        while (dealer.calculateScore() < 17 && dealer.calculateScore() < player.calculateScore()) {
            dealer.addCard(deck.dealCard());
        }
        // Update and check winner
        updateHandLabels();
        checkWinner();
    }

    protected void playerDoublesDown() {
        // Double the player's current bet
        int originalBet = playerAccount.getBet();
        int doubledBet = originalBet * 2;

        // Check if the player has enough in their pot
        if (playerAccount.getPot() >= doubledBet) {
            // Update the player's bet and deduct it from the pot
            playerAccount.setBet(originalBet);
            playerAccount.deductBet();

            // Deal one more card to the player
            player.addCard(deck.dealCard());
            updateHandLabels();

            // End player's turn immediately after doubling down
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);

            // Check if the player busts
            if (player.calculateScore() > 21) {
                // Player busts; lose the doubled bet
                statusLabel.setText("Bust! You lose your doubled bet.");
                dealerFirstCardHidden = false; // Reveal dealer's first card

                // End the round
                endRound();
            } else {
                // If the player does not bust, proceed with the dealer's turn
                playerStands();
                // After the dealer's turn, handle the outcomes
                if (dealer.calculateScore() > 21) {
                    // Dealer busts; player wins
                    statusLabel.setText("Dealer busts! You win your doubled bet!");
                    playerAccount.addBet();
                } else if (player.calculateScore() > dealer.calculateScore()) {
                    // Player's score is higher than dealer's; player wins
                    statusLabel.setText("Player wins! You win your doubled bet!");
                    playerAccount.addBet();
                } else if (dealer.calculateScore() > player.calculateScore()) {
                    // Player score is lower than dealer's; player loses
                    statusLabel.setText("Dealer wins! You lose your doubled bet!");
                } else {
                    // If player's score is equal to dealer's score, push bet
                    statusLabel.setText("It's a tie! Your doubled bet is pushed back.");
                    playerAccount.pushBet();
                }
            }
            // End the round
            endRound();
        } else {
            statusLabel.setText("Not enough funds in the pot to double down.");
        }
    }

    // Method to check the result and update pot accordingly
    protected void checkWinner() {
        // Calculate scores
        int dealerScore = dealer.calculateScore();
        int playerScore = player.calculateScore();

        // Determine the outcome of a BlackJack
        if (playerScore == 21 && player.getHand().size() == 2) {
            // Player has Blackjack, dealer does not
            statusLabel.setText("Player wins with Blackjack! 3:2 payout.");
            playerAccount.addBlackJack(); // Payout 3:2 ratio
        } else if (dealerScore == 21 && dealer.getHand().size() == 2 && playerScore == 21 && player.getHand().size() == 2) {
            // Both player and dealer have Blackjack
            statusLabel.setText("Push! Both have Blackjack.");
            playerAccount.pushBet();
        }

        // Determines the pot of a hand accordingly
        if (dealerScore > 21) {
            // Dealer busts, player wins
            statusLabel.setText("Dealer busts! Player wins.");
            playerAccount.addBet(); // Add the original bet and winnings
            playerAccount.addBet();
        } else if (playerScore > dealerScore) {
            // Player's score is higher than dealer's score, player wins
            statusLabel.setText("Player wins!");
            playerAccount.addBet(); // Add the original bet and winnings
            playerAccount.addBet();
        } else if (dealerScore > playerScore) {
            // Dealer's score is higher than player's score, dealer wins
            statusLabel.setText("Dealer wins!");
        } else {
            // Tie scenario, push bet back to player
            statusLabel.setText("It's a tie! Bet has been pushed.");
            playerAccount.pushBet();
        }

        // End the round and update the pot label
        endRound();
    }

    // Method to update the hand labels
    public void updateHandLabels() {
        dealerHandLabel.setText("Dealer's Hand: " + (dealerFirstCardHidden ? "?" : dealer.calculateScore()));
        playerHandLabel.setText("Player's Hand: " + player.calculateScore());

        // Update the player card panels
        updateCardPanel(dealerCardPanel, dealer.getHand(), dealerFirstCardHidden);
        updateCardPanel(playerCardPanel, player.getHand(), false);
    }

    private void showThanksForPlaying() {
        // Create a new JFrame for the thank-you screen
        JFrame thankYouFrame = new JFrame("Thanks For Playing!");

        // Set the frame layout
        thankYouFrame.setLayout(new BorderLayout());

        // Create a JLabel with the thank-you message
        JLabel thankYouLabel = new JLabel("Thanks For Playing!", SwingConstants.CENTER);

        // Set the font size of the thank-you message
        thankYouLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Add the thank-you label to the frame
        thankYouFrame.add(thankYouLabel, BorderLayout.CENTER);

        // Set the frame size and location
        thankYouFrame.setSize(300, 100);
        thankYouFrame.setLocationRelativeTo(null);
        thankYouFrame.setVisible(true);

        // Create a timer to close the thank-you frame and exit the program after a few seconds
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thankYouFrame.dispose(); // Close the thank-you frame
                System.exit(0); // Exit the program
            }
        });

        // Start the timer
        timer.setRepeats(false); // Only execute once
        timer.start();
    }

    // Method to end the round
    protected void endRound() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleDownButton.setEnabled(false);
        playAgainButton.setEnabled(true);
        quitGameButton.setEnabled(true);
        potLabel.setText("Your Pot: $" + playerAccount.getPot());

        if (playerAccount.getPot() <= 0) {
            statusLabel.setText("Game Over: You have run out of money.");
            showThanksForPlaying();
        } else {
            potLabel.setText("Your Pot: $" + playerAccount.getPot());
        }
    }
}