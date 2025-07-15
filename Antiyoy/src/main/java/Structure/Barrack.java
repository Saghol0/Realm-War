package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Barrack extends Structures {
    private int unitSpace;
    private int buildCost;

    public Barrack() {
        super("Barrack", 200, 5, 1, 3, loadImage(),15);
        this.unitSpace = 2;
        this.buildCost = 15;
    }

    public static Image loadImage() {
        URL url = Barrack.class.getResource("/Image/barracks.png");
        if (url == null) {
            System.err.println("Barrack icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        unitSpace += 5;
    }

    public int getUnitSpace() {
        return unitSpace;
    }

    @Override
    public int getBuildCost() {
        return buildCost;
    }
}
