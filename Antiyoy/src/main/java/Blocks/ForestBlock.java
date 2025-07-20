package Blocks;

import Game.Player;
import Structure.Farm;
import Structure.Structures;
import Units.Unit;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ForestBlock extends Block {

//    public ForestBlock(int x, int y) {
//        super(x, y, "Forest Block", Color.GREEN);
//    }

    public ForestBlock(int x, int y, Player player, Structures structure, Unit unit) {
        super(x, y, "Forest Block", new Color(102, 187, 106), player, structure, unit,loadImage());
    }

    public static Image loadImage() {
        URL url = Farm.class.getResource("/Image/tree.png");
        if (url == null) {
            System.err.println("Farm icon not found!");
            return null;
        }
        return new ImageIcon(url).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
    }
}
