package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TownHall extends Structures {
    private static Image icon;
    private int goldProduction;
    private int foodProduction;
    private int unitSpace;

    public TownHall() {
        super("Town Hall", 300, 15, 1, 1, loadImage(),0);
        this.goldProduction = 25;
        this.foodProduction = 10;
        this.unitSpace = 0;
    }

    public static Image loadImage() {
        if (icon == null) {
            URL url = TownHall.class.getResource("/Image/town-hall.png");
            if (url == null) {
                System.err.println("TownHall icon not found!");
                icon = null;
            } else {
                icon = new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            }
        }
        return icon;
    }

    public int getGoldProduction() {
        return goldProduction;
    }

    public int getFoodProduction() {
        return foodProduction;
    }

    public int getUnitSpace() {
        return unitSpace;
    }

    @Override
    public int getBuildCost() {
        return 0;
    }
}
