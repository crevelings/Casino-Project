import javax.swing.*;
import java.awt.*;

class BackGroundPanel extends JPanel {
    private Image backgroundImage;

    public BackGroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}