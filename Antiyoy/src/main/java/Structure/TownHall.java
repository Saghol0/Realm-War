package Structure;

import javax.swing.*;
import java.awt.*;

public class TownHall extends Structures {
    private int goldProduction;
    private int foodProduction;
    private int unitSpace;

    public Image getIconFrame() {
        return iconFrame;
    }

    private Image iconFrame = new ImageIcon(getClass().getResource("town-hall.png")).getImage();

    public TownHall() {
        super("Town Hall",120,15,1,1);
        this.goldProduction =25 ;
        this.foodProduction =10 ;
        this.unitSpace =1 ;
    }

    public int getGoldProduction()
    { return goldProduction; }
    public int getFoodProduction()
    { return foodProduction; }
    public int getUnitSpace()
    { return unitSpace; }
}
