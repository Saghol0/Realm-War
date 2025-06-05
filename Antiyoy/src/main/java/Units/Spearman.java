package Units;

import Structure.Farm;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Spearman extends Unit {
    public Spearman() {
        super("Spearman",20,20,2,2,10,1,1,loadImage());
    }
    private static Image loadImage() {
        URL url = Farm.class.getResource("Image/man.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}

