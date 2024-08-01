import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {
    public StartMenu() {
        // Set up the frame
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Create buttons
        JButton blackjackButton = new JButton("Black Jack");
        JButton slotMachineButton = new JButton("Slot Machine");
        JButton pokerButton = new JButton("Poker");

        // Set button bounds (x, y, width, height)
        blackjackButton.setBounds(125, 50, 150, 50);
        slotMachineButton.setBounds(125, 100, 150, 50);
        pokerButton.setBounds(125, 150, 150, 50);



        // Add action listeners to buttons
        blackjackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BlackJackStart();
                dispose(); // Close the main menu
            }
        });

        slotMachineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SlotMachine();
                dispose(); // Close the main menu
            }
        });

        pokerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PokerStartScreen();
                dispose(); // Close the main menu
            }
        });

        // Add buttons to frame
        add(blackjackButton);
        add(slotMachineButton);
        add(pokerButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartMenu().setVisible(true);
            }
        });
    }
}
