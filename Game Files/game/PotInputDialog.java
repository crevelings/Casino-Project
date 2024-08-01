import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PotInputDialog extends JDialog {
    private JTextField potTextField;
    private JButton okButton, cancelButton;
    private double initialPot;
    private boolean confirmed;

    public PotInputDialog(JFrame parent) {
        super(parent, "Enter Initial Pot Amount", true);
        setLayout(new GridLayout(2, 2));
        setSize(300, 100);
        setLocationRelativeTo(parent);

        // Label and text field for the initial pot amount
        JLabel potLabel = new JLabel("Initial Pot ($):");
        potTextField = new JTextField(10);
        add(potLabel);
        add(potTextField);

        // Buttons for OK and Cancel
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        add(okButton);
        add(cancelButton);

        // Action listener for the OK button
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse the input value
                    initialPot = Double.parseDouble(potTextField.getText());
                    if (initialPot < 0 || initialPot > 10000) {
                        JOptionPane.showMessageDialog(PotInputDialog.this, "Please enter a valid pot amount (0 - $10,000).");
                    } else {
                        confirmed = true;
                        dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PotInputDialog.this, "Invalid input. Please enter a numeric value.");
                }
            }
        });

        // Action listener for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
    }

    // Getter for the initial pot amount
    public double getInitialPot() {
        return initialPot;
    }

    // Getter to check if the dialog was confirmed
    public boolean isConfirmed() {
        return confirmed;
    }
}
