package Block;

public class ForestBlock extends Block.Block {
    private static final int FOOD_GENERATION = 2;
    public ForestBlock() {
        super("Forest");
    }
    @Override
    public int generateResources() {
        return isOwned ? FOOD_GENERATION : 0 ;
    }
}
