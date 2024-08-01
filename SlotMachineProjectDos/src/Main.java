import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

class SlotMachine {
    private JFrame frame;
    private JLabel[] slots = new JLabel[9];
    private JLabel messageLabel, balanceLabel;
    private JLabel winImageLabel;
    private JButton spinButton, changeBetButton;
    private final String[] symbols = {"🍎", "🍊", "🍋", "🍒", "🍇", "🍉", "🍓", "🍌", "🍍", "🍐"};
    private int balance;
    private int betAmount;

    public SlotMachine() {
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("SlotBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Slot Machine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        balance = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your total amount:"));
        betAmount = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount for each spin:"));

        BackGroundPanel mainPanel = new BackGroundPanel(backgroundImage);
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel slotsPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        slotsPanel.setOpaque(false);

        Font slotFont = new Font("Arial", Font.PLAIN, 48);

        for (int i = 0; i < slots.length; i++) {
            slots[i] = new JLabel(symbols[0], SwingConstants.CENTER);
            slots[i].setFont(slotFont);
            slots[i].setPreferredSize(new Dimension(100, 100));
            slotsPanel.add(slots[i]);
        }

        spinButton = new JButton("Spin");
        spinButton.addActionListener(new SpinHandler());

        changeBetButton = new JButton("Change Bet");
        changeBetButton.addActionListener(new ChangeBetHandler());

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        balanceLabel = new JLabel("Balance: $" + balance);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        winImageLabel = new JLabel();
        winImageLabel.setHorizontalAlignment(JLabel.CENTER);

        mainPanel.add(slotsPanel, gbc);
        mainPanel.add(spinButton, gbc);
        mainPanel.add(changeBetButton, gbc);
        mainPanel.add(messageLabel, gbc);
        mainPanel.add(balanceLabel, gbc);

        // Add winImageLabel on top of the main panel
        mainPanel.add(winImageLabel, gbc);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class SpinHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (balance < betAmount) {
                messageLabel.setText("Insufficient funds!");
                return;
            }
            balance -= betAmount;
            updateBalance();

            Random rand = new Random();
            for (JLabel slot : slots) {
                slot.setText(symbols[rand.nextInt(symbols.length)]);
            }
            checkWin();
        }
    }

    private class ChangeBetHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            betAmount = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your new bet amount:"));
        }
    }

    public void checkWin() {
        int[][] paylines = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Horizontal lines
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Vertical lines
            {0, 4, 8}, {2, 4, 6}             // Diagonal lines
        };

        for (int[] line : paylines) {
            String[] checkWin = {slots[line[0]].getText(), slots[line[1]].getText(), slots[line[2]].getText()};
            if (checkWin[0].equals(checkWin[1]) && checkWin[1].equals(checkWin[2])) {
                if (checkWin[0].equals("🍒")) {
                    messageLabel.setText("YOU WIN!! Triple Winnings!!");
                    balance += betAmount * 3;
                } else if (checkWin[0].equals("🍋")) {
                    messageLabel.setText("YOU WIN!! Quadruple Winnings!!");
                    balance += betAmount * 4;
                } else if (checkWin[0].equals("🍓")) {
                    messageLabel.setText("YOU WIN!! Quintuple Winnings!!");
                    balance += betAmount * 5;
                } else {
                    messageLabel.setText("YOU WIN!!");
                    balance += betAmount * 2;
                }
                updateBalance();
                showWinEffects();
                return;
            }
        }
        messageLabel.setText("You lose!");
        if (balance < betAmount) {
            spinButton.setEnabled(false);
            messageLabel.setText("Game Over! Insufficient funds.");
        }
    }

public void showWinEffects() {
    // Remove spin and change bet buttons
    spinButton.setVisible(false);
    changeBetButton.setVisible(false);

    // Remove slots from the screen
    for (JLabel slot : slots) {
        slot.setVisible(false);
    }

    // Display confetti image
    ImageIcon originalIcon = new ImageIcon("confetti.png");
    Image originalImage = originalIcon.getImage();
    Image scaledImage = originalImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT);
    ImageIcon scaledIcon = new ImageIcon(scaledImage);

    winImageLabel.setIcon(scaledIcon);

    // Play win sound
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("monkey.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }

    // Timer to reset the win effects after 5 seconds
    Timer timer = new Timer(5000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            messageLabel.setText("");
            winImageLabel.setIcon(null);
            spinButton.setVisible(true);
            changeBetButton.setVisible(true);
            for (JLabel slot : slots) {
                slot.setVisible(true);
            }
            spinButton.setEnabled(true);
        }
    });
    timer.setRepeats(false);
    timer.start();
}

    public void updateBalance() {
        balanceLabel.setText("Balance: $" + balance);
    }

    public static void main(String[] args) {
        new SlotMachine();
    }
}

class BackGroundPanel extends JPanel {
    private Image backgroundImage;

    public BackGroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
