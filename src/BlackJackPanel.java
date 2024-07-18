import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackJackPanel extends JFrame {
    public BlackJackPanel() {
        setTitle("Welcome to BlackJack!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 200));

        JButton startButton = new JButton("Start A Game");
        panel.add(startButton);

        JButton quitButton = new JButton("Quit");
        panel.add(quitButton);

        setLayout(new BorderLayout());

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the input dialog for the initial pot
                PotInputDialog potInputDialog = new PotInputDialog(BlackJackPanel.this);
                potInputDialog.setVisible(true);

                // If the user confirmed their pot amount, start the game
                if (potInputDialog.isConfirmed()) {
                    double initialPot = potInputDialog.getInitialPot();

                    // Create and display an instance of BlackJackGame
                    PlayerPotAndBet playerAccount = new PlayerPotAndBet();
                    playerAccount.setPot((int) initialPot);

                    new BlackJackGame(playerAccount);

                    // Close the current introduction screen
                    BlackJackPanel.this.dispose();
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Quit the application
                System.out.println("Quitting application...");
                System.exit(0);
            }
        });

        JLabel imageLabel = getjLabel();

        // Add the image label to the "South" region of the main panel
        panel.add(imageLabel, BorderLayout.SOUTH);

        // Add the panel to the frame's content pane
        setContentPane(panel);

        // Pack the frame to fit the components
        pack();
        setVisible(true);
        // Center the frame on the screen
        setLocationRelativeTo(null);
    }

    private static JLabel getjLabel() {
        ImageIcon imageIcon = new ImageIcon("deck.png");

        // Get the image from the ImageIcon
        Image originalImage = imageIcon.getImage();

        // Set the desired width and height for the resized image
        int desiredWidth = 300; // Adjust to your desired width
        int desiredHeight = 170; // Adjust to your desired height

        // Resize the image using the getScaledInstance method
        Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the resized image
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // Create a JLabel for the image and set its icon to the resized image
        JLabel imageLabel = new JLabel(resizedIcon);
        return imageLabel;
    }

    public static void main(String[] args) {
        new BlackJackPanel();
    }
}