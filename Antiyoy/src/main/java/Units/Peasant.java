package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Peasant extends Unit {
    public Peasant(JLabel uLabel) {
        super("Peasant", 1, 10, 5, 1, loadImage(), 50, 100);
    }
    public static Image loadImage() {
        URL url = Peasant.class.getResource("/Image/peasant.png");
        if (url == null) {
            System.err.println("peasant icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
