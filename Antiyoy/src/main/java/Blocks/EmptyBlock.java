package Blocks;

import src.main.java.Player;

import java.awt.*;

public class EmptyBlock extends Block {
    private static final int goldGeneration = 2;

    public EmptyBlock(int x, int y, Player player) {
        super(x, y, "Empty Block", Color.GRAY);
    }
//    @Override
//    public int generateResources() {
//        return isOwned ? goldGeneration : 0 ;
//    }
}