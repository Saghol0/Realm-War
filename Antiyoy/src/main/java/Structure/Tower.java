package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Tower extends Structures {
    private int buildCost;

    public Tower() {
        super("Tower", 200, 5, 1, 3, loadImage(),15);
        this.buildCost = 25;
    }

    private static Image loadImage() {
        URL url = Tower.class.getResource("/Image/tower.png");
        if (url == null) {
            System.err.println("Tower icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }

    @Override
    public int getBuildCost() {
        return buildCost;
    }
}
