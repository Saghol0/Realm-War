package Units;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Peasant extends Unit {
    public Peasant() {
        super(
            "peasant",
            4,
            1 * 10,   // هزینه طلا متناسب با رنک
            2 + 1,         // هزینه غذا متناسب با رنک
            1,                // فضای یونیت (یک واحد)
    loadImage()
        );
    }

private static Image loadImage() {
    URL url = Peasant.class.getResource("/Image/peasant.png");
    if (url == null) {
        System.err.println("peasant icon not found!");
        return null;
    }
    return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
}
}
