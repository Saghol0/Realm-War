package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Knight extends Unit {
    public Knight(JLabel uLabel) {
        super(
                "Knight",
                4,
                4 * 10,   // هزینه طلا متناسب با رنک
                2 + 4,         // هزینه غذا متناسب با رنک
                4,                // فضای یونیت (یک واحد)
                loadImage()
        );
    }

    private static Image loadImage() {
        URL url = Knight.class.getResource("/Image/knight.png");
        if (url == null) {
            System.err.println("Knight icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
