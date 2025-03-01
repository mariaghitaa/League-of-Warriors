package tema;


import javax.swing.*;
import java.awt.*;


public class FinalFrame extends JFrame {
    private Character currChar;
    private Game game;

    public FinalFrame(Character currChar, Game game) {
        super("Final");
        this.currChar = currChar;
        this.game = game;

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        setLayout(new BorderLayout(10, 10));

        //stanga
        JPanel img = new JPanel();
        String currCharImg = "src/imagini/" + currChar.getRole() + ".png";
        JLabel currCharImgLabel = new JLabel(new ImageIcon(currCharImg));
        currCharImgLabel.setBorder(BorderFactory.createLineBorder(Color.PINK, 4));
        img.add(currCharImgLabel);
        add(img, BorderLayout.WEST);

        //centru info
        JPanel charInfo = new JPanel(new GridLayout(6, 1, 5, 5));
        charInfo.setBackground(Color.PINK);
        charInfo.add(new JLabel("Name: " + currChar.getName()));
        charInfo.add(new JLabel("Role: " + currChar.getRole()));
        charInfo.add(new JLabel("Level: " + currChar.getlvl()));
        charInfo.add(new JLabel("Experience: " + currChar.getExperience()));
        charInfo.add(new JLabel("Killed enemies: " + currChar.getEnemyKilled()));
        add(charInfo, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        //exit
        JButton exitBtn = new JButton("EXIT");
        exitBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);

        //return buton
        JButton returnBtn = new JButton("RETURN");
        returnBtn.addActionListener(e ->{
            dispose();
            new CharacterSelectionGUI(game);
        });
        buttonPanel.add(returnBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
