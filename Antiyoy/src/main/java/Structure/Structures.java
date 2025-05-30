package Structure;

import javax.swing.*;
import java.awt.*;

public abstract class Structures {

    protected String name;
    protected int durability;
    protected int maintenanceCost;
    protected int level;
    protected int maxLevel;
    protected Image imageIcon;

    public Structures(String name, int durability, int maintenanceCost, int level, int maxLevel, Image imageIcon ) {
        this.name = name;
        this.durability = durability;
        this.maintenanceCost = maintenanceCost;
        this.level = level;
        this.maxLevel = maxLevel;
        this.imageIcon = imageIcon;

    }

    public void levelUp() {
        if(level < maxLevel) {
            level += 1;
            durability += 10;
            maintenanceCost += 5;
            System.out.println(name+"has been upgraded to level "+level);
        }else{
            System.out.println(name+" has already reached max level ");
        }


    }
    public String getName() { return name; }
    public int getDurability() { return durability; }
    public int getMaintenanceCost() { return maintenanceCost; }
}
