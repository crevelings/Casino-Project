import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerStartScreen extends JFrame {
    public PokerStartScreen() {
        setTitle("Poker Start Screen");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Add "Coming Soon" text
        JLabel comingSoonLabel = new JLabel("Poker Game Coming Soon!");
        comingSoonLabel.setFont(new Font("Arial", Font.BOLD, 20));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        comingSoonLabel.setBounds(50, 50, 300, 30);
        add(comingSoonLabel);

        // Add "Back" button
        JButton backButton = new JButton("Back");
        backButton.setBounds(150, 200, 100, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartMenu().setVisible(true);
                dispose(); // Close the Poker start screen
            }
        });
        add(backButton);

        // Set background color or add other design elements as needed
        getContentPane().setBackground(Color.LIGHT_GRAY);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PokerStartScreen();
            }
        });
    }
}
