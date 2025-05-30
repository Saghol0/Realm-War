package Structure;

import javax.swing.*;
import java.awt.*;

public class Tower extends Structures {
    private int buildCost;

    public int getBuildCost() {
        return buildCost;
    }

//    public Image getIconFrame() {
//        return iconFrame;
//    }

    public Tower() {
        super("Tower",50,5,1,3,new ImageIcon(Tower.class.getResource("tower.png")).getImage());
        this.buildCost = 25;
    }
    public void levelUp() {
        super.levelUp();

    }
}
