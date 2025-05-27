package structure;

public class Barrack extends structure.Structure {
    private int unitSpace;
    public Barrack() {
        super("Barrack",,,,);
        this.unitSpace = 3 ;
    }
    @Override
    public void levelUp() {
        super.levelUp();
        unitSpace =+ ;
    }
    public int getUnitSpace() {
        return unitSpace;
    }
}
