package Structure;

import javax.swing.*;
import java.awt.*;

public class Tower extends Structures {
    private int buildCost;
    private Image iconFrame = new ImageIcon(getClass().getResource("tower.png")).getImage();

    public int getBuildCost() {
        return buildCost;
    }

    public Image getIconFrame() {
        return iconFrame;
    }

    public Tower() {
        super("Tower",50,5,1,3);
        this.buildCost = 25;
    }
    public void levelUp() {
        super.levelUp();

    }
}
