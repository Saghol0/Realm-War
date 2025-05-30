package Structure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Farm extends Structures {
//    private Image ImageIcon = new ImageIcon() ;
//private static Image iconFrame = new ImageIcon(getClass().getResource("farm.png")).getImage();
//    public Image getIconFrame() {
//        return iconFrame;
//    }
    private int foodProduction;
    private int buildCost;
    public Farm() {
        super("Farm",50,5,1,3,new  ImageIcon(Farm.class.getResource("farm.png")).getImage());
        this.foodProduction =5 ;
        this.buildCost =15;
    }
    @Override
    public void levelUp() {
        super.levelUp();
        foodProduction +=5 ;
    }
    public int getfoodProduction() {
        return foodProduction;
    }
}
