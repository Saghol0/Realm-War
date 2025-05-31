package src.main.java;

import java.awt.*;

public class Player {
    private String name;
    private int gold = 10;
    private int food = 0;
    private int unitSpace = 0;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public int getUnitSpace() { return unitSpace; }


    public Color getColor() {
        return color;
    }
    public void addGold(int amount) { gold += amount; }
    public void addFood(int amount) { food += amount; }
    public void addUnitSpace(int amount) { unitSpace += amount; }

}
