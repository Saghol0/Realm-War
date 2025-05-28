package Blocks;

import src.main.java.*;
import java.awt.*;

public class ForestBlock extends Block {
    private static final int FOOD_GENERATION = 4;

    public ForestBlock(int x,int y,Player player) {
        super(x,y,"Forest Block", Color.GREEN,);
    }

    public int generateResources() {
        return findPlayer ? FOOD_GENERATION : 0;
    }

}
