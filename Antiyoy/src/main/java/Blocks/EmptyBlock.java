package Blocks;

import Structure.Structures;
import src.main.java.Player;

import java.awt.*;

public class EmptyBlock extends Block {
    public EmptyBlock(int x, int y, Player player, Structures structure) {
        super(x, y, "Empty Block", Color.DARK_GRAY, player, structure);
    }
}
