package Blocks;

import Structure.Structures;
import Units.Unit;
import src.main.java.Player;
import java.awt.*;

public class ForestBlock extends Block {
    public ForestBlock(int x, int y, Player player, Structures structure, Unit unit) {
        super(x, y, "Forest Block", Color.GREEN, player, structure,unit);
    }
}
