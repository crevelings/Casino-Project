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
    private JLabel slot1, slot2, slot3, messageLabel, balanceLabel, winImageLabel;
    private JButton spinButton, changeBetButton;
    private final String[] symbols = {"🍎", "🍊", "🍋", "🍒", "🍇", "🍉", "🍓", "🍌", "🍍", "🍐"};
    private int balance;
    private int betAmount;
    private Image backgroundImage;

    public SlotMachine() {
        try {
            backgroundImage = ImageIO.read(new File("SlotBackground.png")); // Load your background image here
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
        slotsPanel.setLayout(new FlowLayout());

        Font slotFont = new Font("Arial", Font.PLAIN, 48);

        slot1 = new JLabel(symbols[0]);
        slot1.setFont(slotFont);
        slot1.setPreferredSize(new Dimension(100, 100));

        slot2 = new JLabel(symbols[0]);
        slot2.setFont(slotFont);
        slot2.setPreferredSize(new Dimension(100, 100));

        slot3 = new JLabel(symbols[0]);
        slot3.setFont(slotFont);
        slot3.setPreferredSize(new Dimension(100, 100));

        slotsPanel.add(slot1);
        slotsPanel.add(slot2);
        slotsPanel.add(slot3);

        spinButton = new JButton("Spin");
        spinButton.addActionListener(new SpinHandler());
        slotsPanel.add(spinButton);

        changeBetButton = new JButton("Change Bet");
        changeBetButton.addActionListener(new ChangeBetHandler());
        slotsPanel.add(changeBetButton);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        balanceLabel = new JLabel("Balance: $" + balance);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        winImageLabel = new JLabel();
        winImageLabel.setHorizontalAlignment(JLabel.CENTER);

        mainPanel.add(slotsPanel, BorderLayout.CENTER);
        mainPanel.add(messageLabel, BorderLayout.NORTH);
        mainPanel.add(balanceLabel, BorderLayout.SOUTH);
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
            slot1.setText(symbols[rand.nextInt(symbols.length)]);
            slot2.setText(symbols[rand.nextInt(symbols.length)]);
            slot3.setText(symbols[rand.nextInt(symbols.length)]);
            checkWin();
        }
    }

    private class ChangeBetHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            betAmount = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your new bet amount:"));
        }
    }

    public void checkWin() {
        String[] checkWin = {slot1.getText(), slot2.getText(), slot3.getText()};
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
                    slot1.setText(symbols[0]);
                    slot2.setText(symbols[0]);
                    slot3.setText(symbols[0]);
                    messageLabel.setText("");
                    winImageLabel.setIcon(null);
                    spinButton.setEnabled(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            messageLabel.setText("You lose!");
            if (balance < betAmount) {
                spinButton.setEnabled(false);
                messageLabel.setText("Game Over! Insufficient funds.");
            }
        }
    }

    public void updateBalance() {
        balanceLabel.setText("Balance: $" + balance);
    }

    public static void main(String[] args) {
        new SlotMachine();
    }
}
