package Structure;

import java.awt.*;

public class Structures {
    protected String name;
    protected int durability;
    protected int maintenanceCost;
    protected int level;
    protected int maxLevel;
    protected Image icon;
    protected int buildCost;
    protected int health;
    public Structures(String name, int durability, int maintenanceCost, int level, int maxLevel, Image icon, int buildCost) {
        this.name = name;
        this.durability = durability;
        this.maintenanceCost = maintenanceCost;
        this.level = level;
        this.maxLevel = maxLevel;
        this.icon = icon;
        this.buildCost = buildCost;
    }

    public void levelUp() {
        if (level < maxLevel) {
            level++;
            durability += 10;
            maintenanceCost += 5;
            System.out.println(name + " has been upgraded to level " + level);
        } else {
            System.out.println(name + " has already reached max level.");
        }
    }

    // Getters
    public String getName() { return name; }
    public int getDurability() { return durability; }
    public int getMaintenanceCost() { return maintenanceCost; }
    public int getLevel() { return level; }
    public int getMaxLevel() { return maxLevel; }
    public Image getIcon() { return icon; }
    public int getBuildCost() { return buildCost ; } // پیش‌فرض برای ساخت
}
