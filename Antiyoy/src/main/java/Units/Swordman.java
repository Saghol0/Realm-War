package Units;

import Structure.Farm;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Swordman extends Unit {
    public Swordman() {
        super("Swordman",30,30,3,3,20,2,1,loadImage());
    }
    private static Image loadImage() {
        URL url = Farm.class.getResource("Image/Swordman.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}