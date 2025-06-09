package Units;

import javax.swing.*;
import java.awt.*;

public  class Unit {
    protected String name;
    protected int health;
    protected int attackPower;
<<<<<<< HEAD
    //    protected int movementRange;//mahhdode harekat
=======
//    protected int movementRange;//mahhdode harekat
>>>>>>> 49ac476277f303d4c389cd827d0139467d0479ab
//    protected int attackRange;//mahdode hamle
    protected int costGold;
    protected int costFood;
    protected int unitSpace;
    protected Image icon;

    public Unit(String name, int health, int attackPower, int movementRange, int attackRange, int costGold, int costFood, int unitSpace, Image icon) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
//        this.movementRange = movementRange;
//        this.attackRange = attackRange;
        this.costGold = costGold;
        this.costFood = costFood;
        this.unitSpace = unitSpace;
        this.icon = Unit.this.icon;
    }

    public Unit(String swordman) {
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
    public Image getIcon() { return icon; }
<<<<<<< HEAD
}
=======
}
>>>>>>> 49ac476277f303d4c389cd827d0139467d0479ab
