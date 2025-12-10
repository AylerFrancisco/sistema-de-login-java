package view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {

    private final Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/IMG/login.png")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
