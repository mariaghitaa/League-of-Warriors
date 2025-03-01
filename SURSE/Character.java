package tema;
import java.util.*;

public abstract class Character extends Entity {
    protected String name;
    protected int experience;
    protected int lvl;
    protected int strength;
    protected int charisma;
    protected int dexterity;
    protected int enemyKilled = 0;


    public Character(String name, int experience, int lvl) {
        super();
        this.name = name;
        this.experience = experience;
        this.lvl = lvl;

        this.strength = 15 + lvl;
        this.charisma = 15 + lvl;
        this.dexterity = 15 + lvl;

        this.maxHealth = 150 + lvl * 15;
        this.currHealth = maxHealth;
        this.maxMana = 50 + lvl * 5;
        this.currMana = maxMana;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public String getName() {
        return name;
    }
    public int getExperience() {
        return experience;
    }
    public int getlvl() {
        return lvl;
    }

    public int increaseLevel(){
        this.lvl++;
        return this.lvl;
    }

    public void accumulateExperience(int experience) {
        this.experience += experience;
        //lvl up conditie: la fiecare 100 experienta se adauga un nivel
        while(this.experience >= 100) {
            this.experience -= 100;
            this.lvlUp();
        }
    }

    void lvlUp() {
        this.lvl++;
        strength += 25;
        charisma += 25;
        dexterity += 25;
        maxHealth += 80;
        currHealth = maxHealth;
        maxMana += 70;
        currMana = maxMana;
        System.out.println(name + "level up! New level character: " + lvl);
    }

    @Override
    public void receiveDamage(int damage) {
        Random rand = new Random();
        boolean reduce = rand.nextBoolean();
        if (reduce) {
            damage /= 2; //50% injumataste damageul
            System.out.println(name + "Damage reduced by 50%!");
        }
        currHealth -= damage;
        if (currHealth < 0) currHealth = 0;
        System.out.println(name + " received " + damage + " damage. Current health: " + currHealth);
    }

    @Override
    public int getDamage() {
        int dmg = (strength + charisma + dexterity) / 3;
        Random rand = new Random();
        boolean doubleD= rand.nextBoolean();
        if(doubleD) {
            dmg *= 2; //50% dubleaza damageul
            System.out.println(name + "damage doubled hits");
        }
        return dmg;
    }

    public void restoreHealthMana() {
        System.out.println("You found a sanctuary! Restoring health and mana...");
        int oldHealth = this.getCurrentHealth();
        int oldMana = this.getCurrentMana();

        if (oldHealth < this.getMaxHealth() || oldMana < this.getMaxMana()) {
            int healVal = new Random().nextInt(100) + 50;
            int manaVal = new Random().nextInt(100) + 50;

            this.regenerateHealth(healVal);
            this.regenerateMana(manaVal);

            int newHealth = this.getCurrentHealth();
            int newMana = this.getCurrentMana();

            System.out.println("Restored health from: " + oldHealth + " -> to: " + newHealth);
            System.out.println("Restored mana from: " + oldMana + " -> to: " + newMana);
        } else {
            System.out.println("Health and mana are already at maximum. No restoration needed.");
        }
    }

    public void increaseEnemyKilled() {
        this.enemyKilled++;
    }

    public int getEnemyKilled() {
        return this.enemyKilled;
    }

}
