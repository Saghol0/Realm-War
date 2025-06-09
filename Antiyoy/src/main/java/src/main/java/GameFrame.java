package src.main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame extends JFrame {
    Image iconFrame = new ImageIcon(getClass().getResource("/Image/Icon.jpg")).getImage();

    public GameFrame() {
        setTitle("Antiyoy War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(1400, 830);
        setLocationRelativeTo(null);
        setIconImage(iconFrame);
        setLayout(new BorderLayout());
        add(new GamePanel());

        setVisible(true);
    }
}
