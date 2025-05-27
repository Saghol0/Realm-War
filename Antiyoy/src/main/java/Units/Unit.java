package units;

public abstract class Unit {
    protected String name;
    protected int health;
    protected int attackPower;
    protected int movementRange;
    protected int attackRange;
    protected int costGold;
    protected int costFood;
    protected int unitSpace;

    public Unit(String name, int health, int attackPower, int movementRange, int attackRange, int costGold, int costFood, int unitSpace) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
        this.movementRange = movementRange;
        this.attackRange = attackRange;
        this.costGold = costGold;
        this.costFood = costFood;
        this.unitSpace = unitSpace;
    }

    public void attack(Unit target) {
        System.out.println(name + " attacks " + target.getName() + " for " + attackPower + " damage!");
        target.takeDamage(attackPower);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            System.out.println(name + " has been defeated!");
        }
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
}
