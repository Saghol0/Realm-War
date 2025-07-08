package Blocks;

import Game.Player;
import Structure.Structures;

import java.awt.*;

public class EmptyBlock extends Block {

//    public EmptyBlock(int x, int y) {
//        super(x, y, "Empty Block", Color.DARK_GRAY);
//    }

    public EmptyBlock(int x, int y, Player player, Structures structure) {
        super(x, y, "Empty Block", Color.DARK_GRAY, player, structure);
    }
}
