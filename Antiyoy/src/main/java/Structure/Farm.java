package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Farm extends Structures {
    private int foodProduction;
    private int buildCost;

    public Farm() {
        super("Farm", 50, 5, 1, 3, loadImage());
        this.foodProduction = 5;
        this.buildCost = 15;
    }

    private static Image loadImage() {
        URL url = Farm.class.getResource("/resources/Image/farm.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }

    @Override
    public void levelUp() {
        super.levelUp();
        foodProduction += 5;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    @Override
    public int getBuildCost() {
        return buildCost;
    }
}
