package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Farm extends Structures {
    private int foodProduction;

    public Farm() {
        super("Farm", 100, 5, 1, 3, loadImage(),15);
        this.foodProduction = 5;
    }

    public static Image loadImage() {
        URL url = Farm.class.getResource("/Image/farm.png");
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
