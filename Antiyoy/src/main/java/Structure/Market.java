package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Market extends Structures {
    private int goldProduction;
    private int buildCost;

    public Market() {
        super("Market", 100, 5, 1, 3, loadImage(),15);
        this.goldProduction = 10;
        this.buildCost = 20;
    }

    private static Image loadImage() {
        URL url = Market.class.getResource("/Image/market .png");
        if (url == null) {
            System.err.println("Market icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        goldProduction += 5;
    }

    public int getGoldProduction() {
        return goldProduction;
    }

    @Override
    public int getBuildCost() {
        return buildCost;
    }
}
