package tema;

public abstract class Spell implements Visitor<Entity> {
    protected int damage;
    protected int manaCost;

    public Spell(int damage, int manaCost) {
        this.damage = damage;
        this.manaCost = manaCost;
    }

    public int getDamage() {
        return damage;
    }
    public int getManaCost() {
        return manaCost;
    }
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " -> Damage: " + damage + ", Mana Cost: " + manaCost;
    }

    @Override
    public void visit(Entity entity) {
        boolean imunne = false;
        if ((this instanceof Fire && entity.imuneFire) || (this instanceof Ice && entity.imuneIce) || (this instanceof Earth && entity.imuneEarth))
            imunne = true;
        if (imunne) {
            System.out.println("Enemy is immune to " + this.getClass().getSimpleName());
        } else {
            int total = entity.getDamage() + this.getDamage();
            System.out.println("You handled " + total + " damage to the enemy!");
            entity.receiveDamage(total);
        }
    }
}
