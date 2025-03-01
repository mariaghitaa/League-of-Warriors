package tema;

public class Mage extends Character {
    public Mage(String name, int experience, int lvl) {
        super(name, experience, lvl);
        this.imuneIce = true;
        this.imuneFire = false;
        this.imuneEarth = false;
    }

    @Override
    public void receiveDamage(int damage) {
        //carisma reduce damageul
        int bonus = charisma / 10;
        int chance = 50 + bonus;
        if (chance > 25) chance = 25;

        int rol = new java.util.Random().nextInt(100);
        if (rol < chance) {
            damage /= 2;
            System.out.println(name + " reduce damage by half!");
        }

        currHealth -= damage;
        if (currHealth < 0) currHealth = 0;
        System.out.println(name + " received " + damage + " damage. Current health: " + currHealth);
    }

    @Override
    public int getDamage() {
        // primar = Charisma
        int b = (charisma * 3) + ((strength * 2 + dexterity) / 3);
        boolean doubleD = new java.util.Random().nextBoolean();
        if (doubleD) {
            b *= 2;
            System.out.println(name + " the damage is doubled!");
        }
        return b;
    }
}


