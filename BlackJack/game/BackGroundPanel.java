import javax.swing.*;
import java.awt.*;

public class BackGroundPanel extends JPanel {
    private Image backgroundImage;

    public BackGroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint(); // Repaint the panel to apply the new background image
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
