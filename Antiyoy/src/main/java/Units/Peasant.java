package Units;

import Structure.Farm;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Peasant extends Unit {
    public Peasant() {
        super("Peasant",10,10,1,1,5,1,1,loadImage());
    }
    private static Image loadImage() {
        URL url = Farm.class.getResource("Image/peasant.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}