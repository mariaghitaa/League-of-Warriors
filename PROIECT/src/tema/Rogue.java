package tema;

public class Rogue extends Character {
    public Rogue(String name, int experience, int lvl) {
        super(name, experience, lvl);
        this.imuneEarth = true;
        this.imuneIce = false;
        this.imuneFire = false;
    }

    @Override
    public void receiveDamage(int damage) {
        int bonus = dexterity / 5;
        int chance = 40 + bonus;
        if (chance > 80) chance = 80;

        int rol = new java.util.Random().nextInt(100);
        if (rol < chance) {
            damage /= 2;
            System.out.println(name + " halve damage!");
        }
        currHealth -= damage;
        if (currHealth < 0) currHealth = 0;
        System.out.println(name + " received " + damage + " damage. Current health: " + currHealth);
    }

    @Override
    public int getDamage() {
        //primar = Dexterity
        int b = (dexterity * 2) + ((strength * 2 + charisma) / 3);
        boolean doubleD = new java.util.Random().nextBoolean();
        if (doubleD) {
            b *= 2;
            System.out.println(name + " doubles the damage!");
        }
        return b;
    }
}

