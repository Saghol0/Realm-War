package Block;

public class VoidBlock extends Block.Block {
    public VoidBlock() {
        super("Void Block");
    }
    @Override
    public int generateResources() {
        return 0;
    }
}
