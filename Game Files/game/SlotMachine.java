import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

class SlotMachine {
    private JFrame frame;
    private JLabel[] slots;
    private JLabel messageLabel, balanceLabel, winImageLabel;
    private JButton spinButton, changeBetButton;
    private final String[] symbols = {"🍎", "🍊", "🍋", "🍒", "🍇", "🍉", "🍓", "🍌", "🍍", "🍐"};
    private int balance;
    private int betAmount;
    private Image backgroundImage;

    public SlotMachine() {
        try {
            backgroundImage = ImageIO.read(new File("Game Files/backgrounds/SlotBackground.png")); // Load your background image here
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Slot Machine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        balance = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your total amount:"));
        betAmount = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount for each spin:"));

        BackGroundPanel mainPanel = new BackGroundPanel(backgroundImage);
        mainPanel.setLayout(new BorderLayout());

        JPanel slotsPanel = new JPanel();
        slotsPanel.setOpaque(false);
        slotsPanel.setLayout(new GridLayout(3, 3, 5, 5)); // Adjusted spacing to 5 pixels

        Font slotFont = new Font("Arial", Font.PLAIN, 48);

        slots = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            slots[i] = new JLabel(symbols[0]);
            slots[i].setFont(slotFont);
            slots[i].setPreferredSize(new Dimension(80, 80)); // Adjusted preferred size
            slotsPanel.add(slots[i]);
        }

        spinButton = new JButton("Spin");
        spinButton.addActionListener(new SpinHandler());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinButton);

        changeBetButton = new JButton("Change Bet");
        changeBetButton.addActionListener(new ChangeBetHandler());
        buttonPanel.add(changeBetButton);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        balanceLabel = new JLabel("Balance: $" + balance);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        winImageLabel = new JLabel();
        winImageLabel.setHorizontalAlignment(JLabel.CENTER);

        mainPanel.add(slotsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(messageLabel, BorderLayout.NORTH);
        mainPanel.add(balanceLabel, BorderLayout.WEST);
        mainPanel.add(winImageLabel, BorderLayout.EAST);

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
            for (int i = 0; i < 9; i++) {
                slots[i].setText(symbols[rand.nextInt(symbols.length)]);
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
        // Adjust the win conditions based on the new 9-slot layout
        // For simplicity, let's check for any row or column having the same symbols

        // Check rows
        for (int i = 0; i < 9; i += 3) {
            if (slots[i].getText().equals(slots[i + 1].getText()) && slots[i].getText().equals(slots[i + 2].getText())) {
                processWin(slots[i].getText());
                return;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (slots[i].getText().equals(slots[i + 3].getText()) && slots[i].getText().equals(slots[i + 6].getText())) {
                processWin(slots[i].getText());
                return;
            }
        }

        messageLabel.setText("You lose!");
        if (balance < betAmount) {
            spinButton.setEnabled(false);
            messageLabel.setText("Game Over! Insufficient funds.");
        }
    }

    public void processWin(String symbol) {
        if (symbol.equals("🍒")) {
            messageLabel.setText("YOU WIN!! Triple Winnings!!");
            balance += betAmount * 3;
        } else if (symbol.equals("🍋")) {
            messageLabel.setText("YOU WIN!! Quadruple Winnings!!");
            balance += betAmount * 4;
        } else if (symbol.equals("🍓")) {
            messageLabel.setText("YOU WIN!! Quintuple Winnings!!");
            balance += betAmount * 5;
        } else {
            messageLabel.setText("YOU WIN!!");
            balance += betAmount * 2;
        }
        updateBalance();

        ImageIcon originalIcon = new ImageIcon("confetti.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        winImageLabel.setIcon(scaledIcon);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("monkey.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JLabel slot : slots) {
                    slot.setText(symbols[0]);
                }
                messageLabel.setText("");
                winImageLabel.setIcon(null);
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
