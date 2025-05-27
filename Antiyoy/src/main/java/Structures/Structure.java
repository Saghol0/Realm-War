package structure;

public abstract class Structure {
    protected String name;
    protected int durability;
    protected int maintenanceCost;
    protected int level;
    protected int maxLevel;
    protected int buildCost;

    public Structure(String name, int durability, int maintenanceCost, int level, int maxLevel, int buildCost) {
        this.name = name;
        this.durability = durability;
        this.maintenanceCost = maintenanceCost;
        this.level = level;
        this.maxLevel = maxLevel;
        this.buildCost = buildCost;
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
