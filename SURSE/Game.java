package tema;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Game {
    private static Game instance = null;

    private ArrayList<Account> accounts;
    private Account currAccount;
    private Grid gameGrid;
    private Character currCharacter;
    private int gameLevel = 1;

    private boolean hardcodat = false;
    private boolean playagain = false;

    private Game(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public static Game getInstance(ArrayList<Account> accounts) {
        if (instance == null) {
            //lazy initialization
            instance = new Game(accounts);
        }
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public Grid getGameGrid() {
        return gameGrid;
    }

    public Character getCurrCharacter() {
        return currCharacter;
    }

    public Account getCurrAccount() {
        return currAccount;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("*******************************************");
        System.out.println("       Welcome to  League of Warriors!     ");
        System.out.println("*******************************************");
        System.out.println();

        System.out.println("Choose the way you want to run the game. Press:");
        System.out.println("1: for terminal");
        System.out.println("2: for GUI");

        String mode = sc.nextLine();
        if (mode.equals("1")) {
            runTerminal(sc);
        } else if (mode.equals("2")) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginGUI(accounts);
                }
            });
        } else {
            try {
                throw new InvalidCommandException("Invalid command, please try again.");
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void runTerminal(Scanner sc) {
        System.out.println("Enter your email: ");
        String email = sc.nextLine();
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        currAccount = null;

        for (Account acc : accounts) {
            if (acc.getInformation().getCredentials().getEmail().equals(email) &&
                    acc.getInformation().getCredentials().getPassword().equals(password)) {
                currAccount = acc;
                break;
            }
        }

        if (currAccount == null) {
            System.out.println("Email or password is incorrect!");
            return;
        }

        while (true) {
            if (playagain)
                break;

            // selectarea personajului
            System.out.println(" Select one character:");
            ArrayList<Character> chars = currAccount.getCharacters();
            for (int i = 0; i < chars.size(); i++) {
                System.out.println((i + 1) + ". " + chars.get(i).getName() + " (" + chars.get(i).getClass().getSimpleName() + ")");
            }

            String line = sc.nextLine();
            if (line.equals("QUIT")) {
                System.out.println("Close...!");
                break;
            }

            int choice;

            try {
                System.out.println("Choice the character's number: ");
                choice = Integer.parseInt(line) - 1;
                if (choice < 0 || choice >= chars.size()) {
                    System.out.println("Invalid character number! Try again.");
                    continue;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            currCharacter = chars.get(choice);
            System.out.println("Selected character: " + currCharacter.getName());
            System.out.println("      *level: " + currCharacter.getlvl());
            System.out.println("      *experience: " + currCharacter.getExperience());
            System.out.println("      *health: " + currCharacter.getCurrentHealth());
            System.out.println("      *mana: " + currCharacter.getCurrentMana());
            System.out.println("      *charisma: " + currCharacter.getCharisma() * gameLevel);
            System.out.println("      *strength: " + currCharacter.getStrength() * gameLevel);
            System.out.println("      *dexterity: " + currCharacter.getDexterity() * gameLevel);

            //generare harta
            System.out.println("1. harta harcodata");
            System.out.println("2. Normal game");

            String mapChoice = sc.nextLine();

            if (mapChoice.equals("1")) {
                hardcodat = true;
                gameGrid = Grid.generateMapHarcodata(currCharacter);
            } else {
                hardcodat = false;
                Random r = new Random();
                int length = 3 + r.nextInt(8);
                int width = 3 + r.nextInt(8);
                gameGrid = Grid.generateRandomMap(length, width, currCharacter);
            }

            System.out.println("Map generated...");
            gameGrid.printMap();
            gameLevel = 1;
            boolean gameover = gameLoop(sc);
            if (playagain)
                break;
            if (gameover)
                System.out.println("back to menu...");
            else
                System.out.println("back to menu...");
        }
    }

    private boolean gameLoop(Scanner sc) {
        while (true) {
            if (!currCharacter.alive()) {
                System.out.println("GAME OVER");
                currAccount.incrementgamesNumber();
                return true;
            }

            Cell currCell = gameGrid.getcurrCell();

            if (currCell.getType() == CellEntityType.ENEMY) {
                Enemy enemy = new Enemy(); //creeaza un inamic
                System.out.println("An enemy appears!");
                System.out.println("Enemy Name: " + enemy.getEnemyName());
                System.out.println("     ->health: " + enemy.getCurrentHealth() + "/" + enemy.maxHealth);
                System.out.println("     ->mana: " + enemy.getCurrentMana() + "/" + enemy.maxMana);
                fight(enemy, sc);

                if (currCharacter.alive()) {
                    //castig points random
                    int points = new Random().nextInt(21) + 10; // 10-30 points
                    System.out.println("You gained " + points + " points!");
                    currCharacter.accumulateExperience(points);
                    System.out.println("-----Current experience: " + currCharacter.getExperience());
                    currCell.setType(CellEntityType.VOID);
                    currCell.setVisited(true);
                    gameGrid.printMap();
                } else {
                    System.out.println("You died! GAME OVER");
                    currAccount.incrementgamesNumber();
                    return true;
                }
            } else if (currCell.getType() == CellEntityType.SANCTUARY) {
                System.out.println("You found a sanctuary! Restoring health and mana...");
                int oldHealth = currCharacter.getCurrentHealth();
                int oldMana = currCharacter.getCurrentMana();

                if (oldHealth < currCharacter.getMaxHealth() || oldMana < currCharacter.getMaxMana()) {
                    int healVal = new Random().nextInt(100) + 50;
                    int manaVal = new Random().nextInt(100) + 50;

                    currCharacter.regenerateHealth(healVal);
                    currCharacter.regenerateMana(manaVal);

                    int newHealth = currCharacter.getCurrentHealth();
                    int newMana = currCharacter.getCurrentMana();

                    System.out.println("Restored health from: " + oldHealth + " -> to: " + newHealth);
                    System.out.println("Restored mana from: " + oldMana + " -> to: " + newMana);
                } else {
                    System.out.println("Health and mana are already at maximum. No restoration needed.");
                }

                currCell.setVisited(true);
                currCell.setType(CellEntityType.VOID);
                gameGrid.printMap();

            } else if (currCell.getType() == CellEntityType.PORTAL) {
                System.out.println("You found the portal! Map completed.");
                int points = currCharacter.getlvl() * 5;
                System.out.println("You gain " + points + " growing the expericence...");
                currCharacter.accumulateExperience(points);
                //creste nivelul, reseteaza starea jocului
                gameLevel++;
                currAccount.incrementgamesNumber();
                System.out.println("Number of maps completed: " + currAccount.getgamesNumber());
                System.out.println("Going to next level...");
                currCharacter.increaseLevel();

                if (hardcodat) {
                    return false;
                } else {
                    //resetam viata si mana
                    currCharacter.currHealth = currCharacter.maxHealth;
                    currCharacter.currMana = currCharacter.maxMana;
                    System.out.println("Your health and mana are fully restored!");
                    System.out.println("      *level: " + currCharacter.getlvl());
                    System.out.println("      *experience: " + currCharacter.getExperience());
                    System.out.println("      *health: " + currCharacter.getCurrentHealth());
                    System.out.println("      *mana: " + currCharacter.getCurrentMana());
                    System.out.println("      *charisma: " + currCharacter.getCharisma() * gameLevel);
                    System.out.println("      *strength: " + currCharacter.getStrength() * gameLevel);
                    System.out.println("      *dexterity: " + currCharacter.getDexterity() * gameLevel);
                    Random rr = new Random();
                    int length = 3 + rr.nextInt(8);
                    int width = 3 + rr.nextInt(8);
                    gameGrid = Grid.generateRandomMap(length, width, currCharacter);
                    System.out.println("New map generated >>> Game level " + gameLevel + " ...");
                    gameGrid.printMap();
                }
            } else {
                System.out.println("Choose direction (NORTH,SOUTH,EAST,WEST or QUIT):");
                String cmd = sc.nextLine().toUpperCase();
                if (cmd.equals("QUIT")) {
                    System.out.println("Exiting game...");
                    playagain = true;
                    return false;
                }
                try {
                    switch (cmd) {
                        case "NORTH":
                            gameGrid.goNorthTerminal();
                            break;
                        case "SOUTH":
                            gameGrid.goSouthTerminal();
                            break;
                        case "EAST":
                            gameGrid.goEastTerminal();
                            break;
                        case "WEST":
                            gameGrid.goWestTerminal();
                            break;
                        default:
                            throw new InvalidCommandException("Invalid move command");
                    }
                } catch (InvalidCommandException | ImpossibleMove ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    //logica luptei
    private void fight(Enemy enemy, Scanner sc) {
        // reseteaza abilitatile (3-6 random)
        generateAbilities(currCharacter);
        generateAbilities(enemy);
        System.out.println("------------------------------------------");
        System.out.println("              BATTLE START!               ");
        System.out.println("------------------------------------------");
        System.out.println("PLAYER: " + currCharacter.getName() +
                ", health: " + currCharacter.getCurrentHealth() + "/" + currCharacter.maxHealth +
                ", mana: " + currCharacter.getCurrentMana() + "/" + currCharacter.maxMana +
                ", level: " + currCharacter.getlvl() +
                ", experience: " + currCharacter.getExperience() +
                ", strength: " + currCharacter.getStrength() * gameLevel +
                ", dexterity: " + currCharacter.getDexterity() * gameLevel +
                ", charisma: " + currCharacter.getCharisma() * gameLevel);
        System.out.println();
        System.out.println("ENEMY: " + enemy.getEnemyName() +
                ", health: " + enemy.getCurrentHealth() + "/" + enemy.maxHealth +
                ", mana: " + enemy.getCurrentMana() + "/" + enemy.maxMana);
        System.out.println();

        Entity player = currCharacter;
        Entity opponent = enemy;

        while (player.alive() && opponent.alive()) {
            System.out.println("Choose your action:");
            System.out.println("1. Normal attack");
            System.out.println("2. Use an ability");
            System.out.print("Enter your choice: ");
            String action = sc.nextLine().toUpperCase();
            System.out.println();

            if (action.equals("2") && !player.abilities.isEmpty()) {
                System.out.println("Available abilities:");
                for (int i = 0; i < player.abilities.size(); i++) {
                    Spell sp = player.abilities.get(i);
                    System.out.println((i + 1) + ". " + sp.getClass().getSimpleName() +
                            " (Damage: " + sp.getDamage() + ", Mana cost: " + sp.getManaCost() + ")");
                }
                System.out.print("Enter ability number: ");
                String abl_ch = sc.nextLine();
                int abl_idx;
                try {
                    abl_idx = Integer.parseInt(abl_ch) - 1;
                    if (abl_idx < 0 || abl_idx >= player.abilities.size()) {
                        System.out.println("Invalid ability number, using normal attack instead.");
                        opponent.receiveDamage(player.getDamage());
                    } else {
                        Spell ability = player.abilities.get(abl_idx);
                        if (player.getCurrentMana() < ability.getManaCost()) {
                            System.out.println("Not enough mana for " + ability.getClass().getSimpleName() + ", using normal attack instead.");
                            opponent.receiveDamage(player.getDamage());
                        } else {
                            System.out.println("You use " + ability.getClass().getSimpleName());
                            ability.visit(opponent);
                            player.currMana -= ability.getManaCost();
                            player.abilities.remove(ability);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, using normal attack instead.");
                    opponent.receiveDamage(player.getDamage());
                }
            } else {
                System.out.println("You choose a normal attack.");
                opponent.receiveDamage(player.getDamage());
            }

            if (!opponent.alive()) {
                System.out.println("The enemy has been defeated!");
                break;
            }

            System.out.println();
            System.out.println("Enemy's turn:");
            boolean used = false;
            if (!opponent.abilities.isEmpty()) {
                Random r = new Random();
                for (int knt = 0; knt < opponent.abilities.size(); knt++) {
                    int idx = r.nextInt(opponent.abilities.size());
                    Spell enemy_sp = opponent.abilities.get(idx);
                    if (opponent.getCurrentMana() >= enemy_sp.getManaCost()) {
                        System.out.println("Enemy uses " + enemy_sp.getClass().getSimpleName() + " ability!");
                        enemy_sp.visit(player);
                        opponent.currMana -= enemy_sp.getManaCost();
                        opponent.abilities.remove(enemy_sp);
                        used = true;
                        break;
                    }
                }
            }
            if (!used) {
                System.out.println("Enemy performs a NORMAL attack.");
                player.receiveDamage(opponent.getDamage());
            }

            System.out.println("------------------------------------------");
            System.out.println("New round status:");
            System.out.println("PLAYER: Health: " + player.getCurrentHealth() + "/" + player.maxHealth +
                    ", Mana: " + player.getCurrentMana() + "/" + player.maxMana);
            System.out.println("ENEMY:  Health: " + opponent.getCurrentHealth() + "/" + opponent.maxHealth +
                    ", Mana: " + opponent.getCurrentMana() + "/" + opponent.maxMana);
            System.out.println("------------------------------------------");
        }

        if (!player.alive()) {
            System.out.println("You have been defeated!");
        } else {
            System.out.println("You have defeated the enemy!");
        }
        System.out.println("==========================================");
    }

    void generateAbilities(Entity entity) {
        entity.abilities.clear();
        Random rand = new Random();
        int abilityCount = 3 + rand.nextInt(4); //3-6

        ArrayList<Spell> generated = new ArrayList<>();

        generated.add(new Fire(10 + rand.nextInt(11), 5 + rand.nextInt(6)));
        generated.add(new Ice(10 + rand.nextInt(11), 5 + rand.nextInt(6)));
        generated.add(new Earth(10 + rand.nextInt(11), 5 + rand.nextInt(6)));

        while (generated.size() < abilityCount) {
            int type = rand.nextInt(3);
            if (type == 0) generated.add(new Fire(10 + rand.nextInt(11), 5 + rand.nextInt(6)));
            else if (type == 1) generated.add(new Ice(10 + rand.nextInt(11), 5 + rand.nextInt(6)));
            else generated.add(new Earth(10 + rand.nextInt(11), 5 + rand.nextInt(6)));
        }
        entity.abilities.addAll(generated);
    }

    public void setCurrAccount(Account currAccount) {
        this.currAccount = currAccount;
    }

    public void setCurrCharacter(Character c) {
        this.currCharacter = c;
    }

    public void setGameGrid(Grid arrayLists) {
        this.gameGrid = arrayLists;
    }


    public void playerAttackEnemy(Character player, Enemy enemy) {
        //loveste inamicul cu un atac normal
        int dmgPlayer = player.getDamage();
        enemy.receiveDamage(dmgPlayer);
    }

    public void enemyAttackPlayer(Enemy enemy, Character player) {
        //inamicul loveste jucatorul
        int dmgEnemy = enemy.getDamage();
        player.receiveDamage(dmgEnemy * 2);
    }

    public void gotoNextLevel() {
        gameLevel++;
        currCharacter.currHealth = currCharacter.maxHealth;
        currCharacter.currMana   = currCharacter.maxMana;

        int length = 3 + new Random().nextInt(8);
        int width  = 3 + new Random().nextInt(8);
        gameGrid   = Grid.generateRandomMap(length, width, currCharacter);

        int points = currCharacter.getlvl() * 5;
        currCharacter.accumulateExperience(points);
        currAccount.incrementgamesNumber();
        currCharacter.increaseLevel();
    }
}