package Units;

import Structure.Farm;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Knight extends Unit {
    public Knight() {
        super("Knight",40,40,4,4,50,3,1,loadImage());
    }
    private static Image loadImage() {
        URL url = Farm.class.getResource("Image/knight.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}