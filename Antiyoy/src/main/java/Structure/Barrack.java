package Structure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Barrack extends Structures {
    private int unitSpace;
    private int BuildCost;
    private Image iconFrame = new ImageIcon(getClass().getResource("barracks.png")).getImage();

    public Barrack() {
        super("Barrack",50,5,1,3);
        this.unitSpace = 2 ;
        this.BuildCost = 5;
    }
    @Override
    public void levelUp() {
        super.levelUp();
        unitSpace =+5 ;
    }
    public int getUnitSpace() {
        return unitSpace;
    }
}
