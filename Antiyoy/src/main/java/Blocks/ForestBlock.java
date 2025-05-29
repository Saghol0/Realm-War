package Blocks;

import Structure.Structures;
import src.main.java.*;
import java.awt.*;

public class ForestBlock extends Block {
    private static final int FOOD_GENERATION = 4;
    public ForestBlock(int x, int y, Player player, Structures Structure) {
        super(x,y,"Forest Block", Color.GREEN, player, Structure );
    }


}
