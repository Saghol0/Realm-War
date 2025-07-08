package Blocks;

import Game.Player;
import Structure.Structures;
import java.awt.*;

public class ForestBlock extends Block {

    public ForestBlock(int x, int y) {
        super(x, y, "Forest Block", Color.GREEN);
    }

    public ForestBlock(int x, int y, Player player, Structures structure) {
        super(x, y, "Forest Block", Color.GREEN, player, structure);
    }
}
