package tema;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class AbilityFrameGUI extends JFrame {
    private Game game;
    private FightFrame fightFrame;
    private Enemy enemy;
    private Character player;
    private List<Spell> abilities;

    public AbilityFrameGUI(Game game, FightFrame fightFrame, Enemy enemy) {
        super("Select Ability");
        this.game = game;
        this.fightFrame = fightFrame;
        this.enemy = enemy;
        this.player = game.getCurrCharacter();
        this.abilities = player.getAbilities();

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Select an ability:", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel abilitiesPanel = new JPanel(new GridLayout(2, 3, 10, 10)); //2 linii 3 coloane
        for (Spell spell : abilities) {
            JPanel spellCard = new JPanel(new BorderLayout());
            spellCard.setBorder(BorderFactory.createLineBorder(Color.PINK, 4));

            String img = "src/imagini/" + spell.getClass().getSimpleName()+ ".png";
            JLabel spellImg = new JLabel();
            spellImg.setHorizontalAlignment(SwingConstants.CENTER);

            ImageIcon icon = new ImageIcon(img);
            spellImg.setIcon(icon);
            spellCard.add(spellImg, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(3, 1));
            JLabel nameLabel = new JLabel("Name: " + spell.getClass().getSimpleName(), SwingConstants.CENTER);
            JLabel manaLabel = new JLabel("Mana Cost: " + spell.getManaCost(), SwingConstants.CENTER);
            JLabel dmgLabel = new JLabel("Damage: " + spell.getDamage(), SwingConstants.CENTER);
            infoPanel.add(nameLabel);
            infoPanel.add(manaLabel);
            infoPanel.add(dmgLabel);
            spellCard.add(infoPanel, BorderLayout.SOUTH);

            JButton selectBtn = new JButton("SELECT");
            selectBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 20));
            selectBtn.addActionListener(e -> selectAbility(spell));
            spellCard.add(selectBtn, BorderLayout.EAST);

            abilitiesPanel.add(spellCard);
        }

        add(abilitiesPanel, BorderLayout.CENTER);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn, BorderLayout.SOUTH);
    }

    private void selectAbility(Spell spell) {
        if (player.getCurrentMana() < spell.getManaCost()) {
            JOptionPane.showMessageDialog(this, "Not enough mana for " + spell.getClass().getSimpleName(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        canUseAb(spell);
        dispose();
        fightFrame.refreshPlayerInfo();
        fightFrame.refreshEnemyInfo();
        if (!enemy.alive()) {
            JOptionPane.showMessageDialog(fightFrame, "Enemy Defeated!");
            fightFrame.dispose();
            fightFrame.getMainPage().refreshMap();
            return;
        }
        enemyAttack();

        if (!player.alive()) {
            JOptionPane.showMessageDialog(fightFrame, "You Died! Game Over.");
            fightFrame.dispose();
            new CharacterSelectionGUI(game);
        }
    }

    private void enemyAttack() {
        enemyAttackPlayer(enemy, player);
        fightFrame.refreshPlayerInfo();
        if (!player.alive())
            return;
    }

    private void enemyAttackPlayer(Enemy enemy, Character player) {
        int dmg = enemy.getDamage();
        player.receiveDamage(dmg);
    }

    private void canUseAb(Spell spell) {
        spell.visit(enemy);
        player.currMana -= spell.getManaCost();
        player.getAbilities().remove(spell);
    }

}

