package tema;

import java.util.Random;
import java.util.function.Consumer;

public class Enemy extends Entity{
    private String enemyName;

    public Enemy(){
        Random rand = new Random();
        maxHealth = 50 + rand.nextInt(51); //50-100 health
        currHealth = maxHealth;
        maxMana = 30 + rand.nextInt(21) ; //30-50 mana
        currMana = maxMana;

        imuneEarth = rand.nextBoolean();
        imuneIce = rand.nextBoolean();
        imuneFire = rand.nextBoolean();

        //generare abilitati random 3-6
        int knt = rand.nextInt(4) + 3;
        for (int i = 0; i < knt; i++) {
            int type = rand.nextInt(3);
            int dmg = rand.nextInt(11) + 10; //10-20 dmg
            int cost = rand.nextInt(6) + 5; //5-10 mana cost
            if(type == 0){
                abilities.add(new Fire(dmg, cost));
            } else if(type == 1){
                abilities.add(new Ice(dmg, cost));
            } else if(type == 2){
                abilities.add(new Earth(dmg, cost));
            }
        }
        enemyName = "Enemy_" + Math.abs(rand.nextInt());
    }

    @Override
    public void receiveDamage(int damage) {
        Random rand = new Random();
        boolean evit = rand.nextBoolean();

        if (evit) {
            System.out.println(enemyName + " avoided all damage!");
        } else {
            currHealth -= damage;
            if (currHealth < 0) currHealth = 0;
            System.out.println(enemyName + " received " + damage + " damage. Current health: " + currHealth);
        }
    }

    @Override
    public int getDamage() {
        Random rand = new Random();
        int dmg = 10 + rand.nextInt(6); //interval normal de daune de atac
        boolean doubleDmg = rand.nextBoolean();

        if (doubleDmg) {
            dmg *= 2;
            System.out.println(enemyName + " strikes with double damage!");
        }
        return dmg;
    }

    public String getEnemyName() {
        return enemyName;
    }
}
