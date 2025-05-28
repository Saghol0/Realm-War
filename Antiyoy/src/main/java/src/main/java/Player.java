package src.main.java;

public class Player {
    private String name;
    private int gold = 10;
    private int food = 0;
    private int unitSpace = 0;

    public Player(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getFood() { return food; }
    public int getUnitSpace() { return unitSpace; }

    public void addGold(int amount) { gold += amount; }
    public void addFood(int amount) { food += amount; }
    public void addUnitSpace(int amount) { unitSpace += amount; }

}
