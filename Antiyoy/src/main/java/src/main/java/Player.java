package src.main.java;

import java.awt.*;

public class Player {
    private final String name;
    private int gold;
    private int food;
    private int unitSpace;
    private final Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.gold = 10;
        this.food = 0;
        this.unitSpace = 0;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public int getUnitSpace() { return unitSpace; }
    public Color getColor() { return color; }

    // --- Add resources ---
    public void addGold(int amount) { this.gold += amount; }
    public void addFood(int amount) { this.food += amount; }
    public void addUnitSpace(int amount) { this.unitSpace += amount; }

    // --- Spend / Consume resources ---
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public boolean consumeFood(int amount) {
        if (food >= amount) {
            food -= amount;
            return true;
        }
        return false;
    }

    public boolean useUnitSpace(int amount) {
        if (unitSpace >= amount) {
            unitSpace -= amount;
            return true;
        }
        return false;
    }

    // --- Reset (for restarting game) ---
    public void reset() {
        this.gold = 10;
        this.food = 10;
        this.unitSpace = 0;
    }

    // --- Utility method for debugging or HUD display ---
    public String getStatus() {
        return String.format("%s | Gold: %d | Food: %d | Unit Space: %d", name, gold, food, unitSpace);

    }
}
