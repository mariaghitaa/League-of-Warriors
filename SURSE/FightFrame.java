package tema;

import javax.swing.*;
import java.awt.*;

public class FightFrame extends JFrame {
    private MainPageGUI mainPage;
    private Game game;
    private Character player;
    private Enemy enemy;

    private JLabel playerImg, enemyImg;
    private JLabel plyHealth, plyMana;
    private JLabel enemyHealth, enemyMana;
    private JButton attackBtn, abilityBtn;
    ;

    public FightFrame(MainPageGUI mainPage, Game game, Enemy enemy) {
        super("Battle");
        this.mainPage = mainPage;
        this.game = game;
        this.player = game.getCurrCharacter();
        this.enemy = enemy;

        game.generateAbilities(player);
        game.generateAbilities(enemy);

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        setLayout(new GridLayout(1, 3, 20, 20));
        //panou jucator
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerImg = new JLabel(new ImageIcon("src/imagini/" + game.getCurrCharacter().getRole() + ".png"), SwingConstants.CENTER);

        JPanel playerInfo = new JPanel(new GridLayout(2, 1));
        plyHealth = new JLabel("Health: ");
        plyMana = new JLabel("Mana: ");
        playerInfo.add(plyHealth);
        playerInfo.add(plyMana);
        playerPanel.add(playerImg, BorderLayout.CENTER);
        playerPanel.add(playerInfo, BorderLayout.SOUTH);

        //panou cu butoane(mijloc)
        JPanel buttonsPanel = new JPanel(new GridLayout(2,1));
        attackBtn = new JButton("ATTACK");
        abilityBtn = new JButton("ABILITY");
        abilityBtn.setBounds(50,100,95,30);
        abilityBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 50));
        attackBtn.setBounds(50,100,95,30);
        attackBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 50));
        buttonsPanel.add(attackBtn);
        buttonsPanel.add(abilityBtn);

        //panou inamic
        JPanel enemyPanel = new JPanel(new BorderLayout());
        enemyImg = new JLabel(new ImageIcon("src/imagini/Enemy.png"), SwingConstants.CENTER);
        JPanel enemyStats = new JPanel(new GridLayout(2, 1));
        enemyHealth = new JLabel("Health: ");
        enemyMana = new JLabel("Mana: ");
        enemyStats.add(enemyHealth);
        enemyStats.add(enemyMana);

        enemyPanel.add(enemyImg, BorderLayout.CENTER);
        enemyPanel.add(enemyStats, BorderLayout.SOUTH);

        add(playerPanel);
        add(buttonsPanel);
        add(enemyPanel);

        refreshEnemyInfo();
        refreshPlayerInfo();

        attackBtn.addActionListener(e -> playerNormalAttack());
        abilityBtn.addActionListener(e -> useAbility());
    }

    private void playerNormalAttack() {
        //player vs inamic
        game.playerAttackEnemy(player, enemy);
        refreshEnemyInfo();

        //verifi inamic mort
        if (!enemy.alive()) {
            JOptionPane.showMessageDialog(this, "Enemy Defeated!");
            game.getCurrCharacter().increaseEnemyKilled();
            dispose();
            mainPage.refreshMap();
            return;
        }
        //inamic vs player
        game.enemyAttackPlayer(enemy, player);
        refreshPlayerInfo();

        if (!player.alive()) {
            JOptionPane.showMessageDialog(this, "You Died! Game Over.");
            dispose();
            mainPage.dispose();
            new FinalFrame(game.getCurrCharacter(), game);
            return;
        }
    }

    private void useAbility() {
        if (player.abilities.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No abilities available!");
            return;
        }
        if (player.abilities.isEmpty())
            game.generateAbilities(player);

        new AbilityFrameGUI(game, this, enemy);
    }

    void refreshPlayerInfo() {
        plyHealth.setText("Health: " + game.getCurrCharacter().getCurrentHealth());
        plyMana.setText("Mana: " + game.getCurrCharacter().getCurrentMana());
    }

    void refreshEnemyInfo() {
        enemyHealth.setText("Health: " + enemy.getCurrentHealth());
        enemyMana.setText("Mana: " + enemy.getCurrentMana());
    }

    public MainPageGUI getMainPage() {
        return mainPage;
    }
}
