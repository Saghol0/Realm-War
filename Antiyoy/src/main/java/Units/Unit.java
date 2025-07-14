package Units;

import javax.swing.*;
import java.awt.*;

public abstract class Unit extends Component {
    protected String name;
    public int rank;
    protected int movementRange = 3;  // محدوده حرکت به صورت پیش‌فرض ۳
    public int costGold;
    public int costFood;
    public int unitSpace;
    public Image icon;
    protected Boolean moved   = false;
    protected int health;
    protected int attackPower;
    public Unit(String name, int rank, int costGold, int costFood, int unitSpace, Image icon,int attackPower,int health) {
        this.name = name;
        this.rank = rank;
        this.costGold = costGold;
        this.costFood = costFood;
        this.unitSpace = unitSpace;
        this.icon = icon;
        this.health = health;
        this.attackPower=attackPower;
    }

    // --- قابلیت حرکت ---
    public boolean canMove(int fromX, int fromY, int toX, int toY) {
        if (moved)
        {
            JOptionPane.showMessageDialog(null,"Your unit cannot move because it has already moved once.");
            return false;}
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        return dx + dy <= movementRange; // Manhattan distance
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public int getCostGold() {
        return costGold; }

    public int getCostFood() {
        return costFood;
    }

    public int getUnitSpace() {
        return unitSpace;
    }

    public Image getIcon() {
        return icon;
    }

    public Boolean getMoved() {
        return moved;
    }

    public void setMoved(Boolean moved) {
        this.moved = moved;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setMovementRange(int movementRange) {
        this.movementRange = movementRange;
    }

    public void setCostGold(int costGold) {
        this.costGold = costGold;
    }

    public void setCostFood(int costFood) {
        this.costFood = costFood;
    }

    public void setUnitSpace(int unitSpace) {
        this.unitSpace = unitSpace;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
}