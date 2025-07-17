package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Swordman extends Unit {
    public Swordman(JLabel uLabel) {
        super("Swordman", 3, 3 * 10, 5 * 3, 3, loadImage(),200,300);
    }

    public static Image loadImage() {
        URL url = Swordman.class.getResource("/Image/Swordman.png");
        if (url == null) {
            System.err.println("Swordman icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
