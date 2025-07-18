package Blocks;

import Game.Player;
import Structure.Structures;
import Units.Unit;

import java.awt.*;

public class ForestBlock extends Block {

    public ForestBlock(int x, int y) {
        super(x, y, "Forest Block", Color.GREEN);
    }

    public ForestBlock(int x, int y, Player player, Structures structure, Unit unit) {
        super(x, y, "Forest Block", new Color(102, 187, 106), player, structure, unit);
    }
}
