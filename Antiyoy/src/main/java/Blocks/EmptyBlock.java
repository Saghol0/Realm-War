package Blocks;

import Game.Player;
import Structure.Structures;
import Units.Unit;

import java.awt.*;

public class EmptyBlock extends Block {



    public EmptyBlock(int x, int y, Player player, Structures structure, Unit unit) {
        super(x, y, "Empty Block", Color.DARK_GRAY, player, structure, unit,null);
    }
}
