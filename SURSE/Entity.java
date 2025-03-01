package tema;
import java.util.*;

public abstract class Entity implements Battle, Element<Entity> {
    protected ArrayList<Spell> abilities;
    protected int currHealth, maxHealth;
    protected int currMana, maxMana;
    protected boolean imuneFire, imuneIce, imuneEarth;

    public Entity(){
        abilities = new ArrayList<>();
    }

    public void regenerateHealth(int health) {
        this.currHealth += health;
        if (this.currHealth > this.maxHealth) {
            this.currHealth = this.maxHealth;
        }
    }

    public void regenerateMana(int mana) {
        this.currMana += mana;
        if (this.currMana > this.maxMana) {
            this.currMana = this.maxMana;
        }
    }

    public boolean alive() {
        return currHealth > 0;
    }

    public int getCurrentHealth() {
        return currHealth;
    }

    public int getCurrentMana() {
        return currMana;
    }

    public ArrayList<Spell> getAbilities() {
        return abilities;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    public String getRole() {
        return getClass().getSimpleName();
    }
}
