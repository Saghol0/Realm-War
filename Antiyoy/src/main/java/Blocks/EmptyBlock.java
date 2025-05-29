package Blocks;

import Structure.Structures;
import src.main.java.Player;

import java.awt.*;

public class EmptyBlock extends Block {
    private static final int goldGeneration = 2;

    public EmptyBlock(int x, int y, Player player, Structures Structure) {
        super(x, y, "Empty Block",Color.darkGray,player,Structure );
    }

}