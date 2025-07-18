package Game;
import Units.Unit;

import java.awt.*;

public class Player {
    private String name;
    private int gold;
    private int food;
    private int unitSpace;
    private Color color;
    //    private List <Unit> units;
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.gold = 100;
        this.food = 50;
        this.unitSpace = 0;
    }

    // for load MM
    public Player(String name,Color Color,int Gold,int Food,int UnitSpace){
        this.name=name;
        this.color = Color;
        this.gold=Gold;
        this.food=Food;
        this.unitSpace=UnitSpace;
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public int getUnitSpace() { return unitSpace; }
    public Color getColor() { return color; }

    // --- Add resources ---
    public void addGold(int amount) { gold += amount; }
    public void addFood(int amount) { food += amount; }
    public void addUnitSpace(int amount) { unitSpace += amount; }

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

    public void setGold(int g){
        this.gold=g;
    }
    public void setFood(int f){
        this.food=f;
    }
    public void setUnitSpace(int u){
        this.unitSpace=u;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setName(String name){
        this.name=name;
    }

}