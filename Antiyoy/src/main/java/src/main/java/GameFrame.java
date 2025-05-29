package src.main.java;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameFrame  extends JFrame {
    public GameFrame() {
        setTitle("Antiyoy War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(600,400);
        pack();
        add(new GamePanel());
        setVisible(true);
        Image iconFrame = new ImageIcon(getClass().getResource("Icon.jpg")).getImage();
        setIconImage(iconFrame);
    }
}
