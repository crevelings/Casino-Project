import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OptionsMenu extends JPanel {
    private final BackGroundPanel backgroundPanel;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final MusicPlayer musicPlayer;

    private Map<String, String> musicFiles;
    private Map<String, ImageIcon> backgroundImages;

    public OptionsMenu(JPanel cardPanel, CardLayout cardLayout, BackGroundPanel backgroundPanel) {
        this.backgroundPanel = backgroundPanel;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.musicPlayer = new MusicPlayer();

        // Set layout to GridBagLayout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);  // Spacing between buttons
        gbc.weighty = 1.0;  // Give vertical space to each button equally

        // Create the screen button
        JButton screenButton = createButton("Screen", e -> showBackgroundOptions());
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(screenButton, gbc);

        // Create the music button
        JButton musicButton = createButton("Music", e -> showMusicOptions());
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(musicButton, gbc);

        // Create the back button
        JButton backButton = createButton("Back", e -> cardLayout.show(cardPanel, "GAME"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(backButton, gbc);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 100)); // Set preferred size
        button.addActionListener(actionListener);
        return button;
    }

    private void showBackgroundOptions() {
        JDialog backgroundDialog = new JDialog();
        backgroundDialog.setTitle("Choose Background");

        JPanel backgroundSelectionPanel = new JPanel();
        backgroundSelectionPanel.setLayout(new GridLayout(2, 3, 10, 10)); // Adjust grid layout as needed

        // Load background images
        backgroundImages = getBackgroundImages();

        for (Map.Entry<String, ImageIcon> entry : backgroundImages.entrySet()) {
            String imageName = entry.getKey();
            ImageIcon imageIcon = entry.getValue();
            Image scaledImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JButton imageButton = new JButton(imageName, scaledIcon);
            imageButton.setPreferredSize(new Dimension(100, 100));
            imageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    backgroundPanel.setBackgroundImage(imageIcon.getImage()); // Update background image
                    backgroundDialog.dispose();
                }
            });
            backgroundSelectionPanel.add(imageButton);
        }

        JScrollPane scrollPane = new JScrollPane(backgroundSelectionPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // Set the preferred size of the dialog

        backgroundDialog.add(scrollPane);
        backgroundDialog.pack();
        backgroundDialog.setLocationRelativeTo(this);
        backgroundDialog.setVisible(true);
    }

    private Map<String, ImageIcon> getBackgroundImages() {
        File backgroundDirectory = new File("BlackJack/backgrounds");
        File[] files = backgroundDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        Map<String, ImageIcon> backgroundMap = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String displayName = fileName.substring(0, fileName.lastIndexOf('.'));
                ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
                backgroundMap.put(displayName, imageIcon);
            }
        }

        return backgroundMap;
    }

    private void showMusicOptions() {
        JPanel musicPanel = new JPanel(new BorderLayout());

        // Create the back button
        JButton backButton = createButton("Back", e -> cardLayout.show(cardPanel, "OPTIONS"));

        // Load music files
        musicFiles = getMusicFiles();

        // Create a combo box for music files
        JComboBox<String> musicComboBox = new JComboBox<>(musicFiles.keySet().toArray(new String[0]));

        // Create play and stop buttons
        JButton playButton = createButton("Play", e -> {
            String selectedMusic = (String) musicComboBox.getSelectedItem();
            if (selectedMusic != null) {
                System.out.println("Playing: " + musicFiles.get(selectedMusic)); // Debugging output
                musicPlayer.play(musicFiles.get(selectedMusic));
            }
        });

        JButton stopButton = createButton("Stop", e -> musicPlayer.stop());

        // Add components to the music panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(musicComboBox);
        controlPanel.add(playButton);
        controlPanel.add(stopButton);

        musicPanel.add(controlPanel, BorderLayout.CENTER);
        musicPanel.add(backButton, BorderLayout.SOUTH);

        cardPanel.add(musicPanel, "MUSIC");
        cardLayout.show(cardPanel, "MUSIC");
    }

    private Map<String, String> getMusicFiles() {
        File musicDirectory = new File("music");
        File[] files = musicDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav"));

        Map<String, String> musicMap = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String displayName = fileName.substring(0, fileName.lastIndexOf('.'));
                musicMap.put(displayName, file.getAbsolutePath());
            }
        }

        return musicMap;
    }
}
