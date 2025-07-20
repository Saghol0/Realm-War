package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Knight extends Unit {
    public Knight(JLabel uLabel) {
        super("Knight", 4, 4 * 10, 4*5, 4, loadImage(),300,400);
    }

    public static Image loadImage() {
        URL url = Knight.class.getResource("/Image/knight.png");
        if (url == null) {
            System.err.println("Knight icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
