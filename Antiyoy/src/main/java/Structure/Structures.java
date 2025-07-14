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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(int maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public int getBuildCost() {
        return buildCost;
    } // پیش‌فرض برای ساخت

    public void setBuildCost(int buildCost) {
        this.buildCost = buildCost;
    }
}
