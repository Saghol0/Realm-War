package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Spearman extends Unit {
    public Spearman(JLabel uLabel) {
        super("Spearman", 2, 2 * 10, 5 * 2, 2, loadImage(),100,200,4
        );
    }

    public static Image loadImage() {
        URL url = Spearman.class.getResource("/Image/spearman.png");
        if (url == null) {
            System.err.println("spearman icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
