package Structure;

import javax.swing.*;
import java.awt.*;

public class Market extends Structures {
    private int goldProduction;
    private int buildCost;
    private Image iconFrame = new ImageIcon(getClass().getResource("market .png")).getImage();
    public Market() {
        super("Market",50,5,1,3);
        this.goldProduction =10 ;
        this.buildCost =20 ;
    }
    @Override
    public void levelUp() {
        super.levelUp();
        goldProduction +=5 ;
    }
    public int getGoldProduction() {
      return goldProduction;
    }
    public void setGoldProduction(int goldProduction) {
        this.goldProduction = goldProduction;
    }

    public Image getIconFrame() {
        return iconFrame;
    }
}
