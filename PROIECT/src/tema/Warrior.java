package tema;

import java.util.Vector;

public class Warrior extends Character {
    public Warrior(String name, int experience, int lvl) {
        super(name, experience, lvl);
        this.imuneFire = true;
        this.imuneIce = false;
        this.imuneEarth = false;
    }

    @Override
    public void receiveDamage(int damage) {
        int bonus = (dexterity + charisma) / 5;
        int chance = 50 + bonus;
        if (chance > 90) chance = 90;
        int rol = new java.util.Random().nextInt(100);
        if (rol < chance) {
            damage /= 2;
            System.out.println(name + " partially avoided damage, reducing it by half!");
        }
        currHealth -= damage;
        if (currHealth < 0) currHealth = 0;
        System.out.println(name + " received " + damage + " damage. Current health: " + currHealth);
    }

    @Override
    public int getDamage() {
        //primar = Strength
        int b = (strength * 2) + ((dexterity + charisma) / 2);
        boolean doubleD = new java.util.Random().nextBoolean();
        if (doubleD) {
            b *= 2;
            System.out.println(name + " hits with double damage!");
        }
        return b;
    }
}
