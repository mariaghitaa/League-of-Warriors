package tema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class CharacterSelectionGUI extends JFrame {
    private Game game;

    public CharacterSelectionGUI(Game game) {
        super("Characters");
        this.game = game;

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Select a character from your account:", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));

        ArrayList<Character> chars = game.getCurrAccount().getCharacters();

        for (Character c : chars) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

            JLabel icon = new JLabel(new ImageIcon("src/imagini/" + c.getRole() + ".png"));
            icon.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(icon, BorderLayout.CENTER);

            JLabel nameL = new JLabel(c.getName(), SwingConstants.CENTER);
            card.add(nameL, BorderLayout.SOUTH);

            JButton selectBtn = new JButton("SELECT");
            selectBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 4));
            selectBtn.addActionListener(e -> selectCharacter(c));
            card.add(selectBtn, BorderLayout.NORTH);

            panel.add(card);
        }

        add(panel, BorderLayout.CENTER);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        cancelBtn.setBackground(Color.LIGHT_GRAY);
        cancelBtn.addActionListener(e -> dispose());
        add(cancelBtn, BorderLayout.SOUTH);
    }

    private void selectCharacter(Character selectedC) {
        game.setCurrCharacter(selectedC);

        Random r = new Random();
        int length = 3 + r.nextInt(8);
        int width = 3 + r.nextInt(8);
        Grid grid = Grid.generateRandomMap(length, width, selectedC);
        game.setGameGrid(grid);
        dispose();
        new MainPageGUI(game);
    }
}


