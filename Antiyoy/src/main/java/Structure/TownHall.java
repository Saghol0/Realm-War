package Structure;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TownHall extends Structures {
    private static Image icon;  // آیکون استاتیک
    private int goldProduction;
    private int foodProduction;
    private int unitSpace;

    public TownHall() {
        super("Town Hall", 120, 15, 1, 1, loadImage());
        this.goldProduction = 25;
        this.foodProduction = 10;
        this.unitSpace = 1;
    }

    private static Image loadImage() {
        if (icon == null) {  // فقط اولین بار لود می‌کنه
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
        return 0; // ساخت‌پذیر نیست
    }
}
