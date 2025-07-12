package Units;

import java.awt.*;

public abstract class Unit {
    protected String name;
    protected int rank;
    protected int movementRange = 3;
    public int costGold;
    public int costFood;
    public int unitSpace;
    public Image icon;

    public boolean hasMoved = false;

    public Unit(String name, int rank, int costGold, int costFood, int unitSpace, Image icon) {
        this.name = name;
        this.rank = rank;
        this.costGold = costGold;
        this.costFood = costFood;
        this.unitSpace = unitSpace;
        this.icon = icon;
    }

    public boolean canMove(int fromX, int fromY, int toX, int toY) {
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        return dx + dy <= movementRange;
    }

    public void resetMovement() {
        hasMoved = false;
    }

    public String getName() { return name; }
    public int getMovementRange() { return movementRange; }
    public int getCostGold() { return costGold; }
    public int getCostFood() { return costFood; }
    public int getUnitSpace() { return unitSpace; }
    public Image getIcon() { return icon; }
}
