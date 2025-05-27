package Block;

public class EmptyBlock extends Block.Block {
    private static final int Gold_Grneration = 2;
    public EmptyBlock(){
        super("Empty block");
    }
    @Override
    public int generateResources() {
        return isOwned ? Gold_Grneration : 0 ;
    }
}