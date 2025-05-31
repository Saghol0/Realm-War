package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Barrack extends Structures {
    private int unitSpace;
    private int BuildCost;

    public Barrack() {
        super("Barrack", 50, 5, 1, 3, loadImage());
        this.unitSpace = 2;
        this.BuildCost = 5;
    }

    private static Image loadImage() {
        URL url = Barrack.class.getResource("/Image/barrack.png");
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
}
