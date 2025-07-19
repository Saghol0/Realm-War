package Game;

import java.awt.*;

public class Player {
    private String name;
    private int gold;
    private int food;
    private int usedUnitSpace;
    private int maxUnitSpace;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.gold = 50;
        this.food = 25;
        this.usedUnitSpace = 0;
        this.maxUnitSpace = 0;
    }

    public Player(String name, Color color, int gold, int food, int usedUnitSpace, int maxUnitSpace) {
        this.name = name;
        this.color = color;
        this.gold = gold;
        this.food = food;
        this.usedUnitSpace = usedUnitSpace;
        this.maxUnitSpace = maxUnitSpace;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public int getUsedUnitSpace() { return usedUnitSpace; }
    public int getMaxUnitSpace() { return maxUnitSpace; }
    public Color getColor() { return color; }

    // --- Resource Modification ---
    public void addGold(int amount) { gold += amount; }
    public void addFood(int amount) { food += amount; }
    public void addMaxUnitSpace(int amount) { maxUnitSpace += amount; }

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
        if (usedUnitSpace + amount <= maxUnitSpace) {
            usedUnitSpace += amount;
            return true;
        }
        return false;
    }

    public void releaseUnitSpace(int amount) {
        usedUnitSpace = Math.max(0, usedUnitSpace - amount);
    }

    // --- Reset ---
    public void reset() {
        this.gold = 50;
        this.food = 25;
        this.usedUnitSpace = 0;
        this.maxUnitSpace = 0;
    }

    // --- Debug / HUD String ---
    public String getStatus() {
        return String.format("%s | Gold: %d | Food: %d | Unit Space: %d/%d",
                name, gold, food, usedUnitSpace, maxUnitSpace);
    }
    public String getColorString() {
        return color.toString();
    }

    // --- Setters ---
    public void setGold(int g) { this.gold = g; }
    public void setFood(int f) { this.food = f; }
    public void setUsedUnitSpace(int u) { this.usedUnitSpace = u; }
    public void setMaxUnitSpace(int m) { this.maxUnitSpace = m; }
    public void setColor(Color color) { this.color = color; }
    public void setName(String name) { this.name = name; }
}
