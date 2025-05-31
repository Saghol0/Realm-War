package src.main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame  extends JFrame {
//        Image iconFrame = new ImageIcon(getClass().getResource("Icon.jpg")).getImage();
Image iconFrame = new ImageIcon(getClass().getResource("/Image/Icon.jpg")).getImage();

    public GameFrame() {
        setTitle("Antiyoy War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(iconFrame);

        add(new GamePanel());
        setSize(800, 800);
//        pack();
        setVisible(true);
    }
}
