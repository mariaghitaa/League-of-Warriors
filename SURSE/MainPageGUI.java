package tema;


import javax.swing.*;
import java.awt.*;


public class MainPageGUI extends JFrame {
    private Game game;
    private JButton[][] buton;
    private JLabel lvlL, expL, healthL, manaL;
    private JButton northBtn, southBtn, eastBtn, westBtn, quitBtn;

    private JPanel center;

    public MainPageGUI(Game game) {
        super("Main Page");
        this.game = game;

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        intializationComponents();
        setVisible(true);
    }

    private void intializationComponents() {
        getContentPane().setLayout(new BorderLayout(5,5));

        //Panou stanga - info + butoane
        JPanel leftPanel = new JPanel(new GridLayout(7,1,5,5));
        northBtn = new JButton("NORTH");
        northBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 30));
        southBtn = new JButton("SOUTH");
        southBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 30));
        eastBtn  = new JButton("EAST");
        eastBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 30));
        westBtn  = new JButton("WEST");
        westBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 30));

        leftPanel.add(northBtn);
        leftPanel.add(southBtn);
        leftPanel.add(eastBtn);
        leftPanel.add(westBtn);

        lvlL  = new JLabel("Level: ");
        lvlL.setFont(lvlL.getFont().deriveFont(Font.BOLD, 15));
        expL    = new JLabel("Experience: ");
        expL.setFont(expL.getFont().deriveFont(Font.BOLD, 15));
        healthL = new JLabel("Health: ");
        healthL.setFont(healthL.getFont().deriveFont(Font.BOLD, 15));
        manaL   = new JLabel("Mana: ");
        manaL.setFont(manaL.getFont().deriveFont(Font.BOLD, 15));

        leftPanel.add(lvlL);
        leftPanel.add(expL);
        leftPanel.add(healthL);
        leftPanel.add(manaL);

        quitBtn = new JButton("QUIT");
        quitBtn.setBorder(BorderFactory.createLineBorder(Color.PINK, 30));
        leftPanel.add(quitBtn);

        add(leftPanel, BorderLayout.WEST);

        //Panou central-harta
        Grid g = game.getGameGrid();
        int rows = g.getLength();
        int cols = g.getWidth();

        center = new JPanel(new GridLayout(rows, cols, 2, 2));

        center.setPreferredSize(new Dimension(500, 500));

        buton = new JButton[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JButton btn = new JButton();

                btn.setEnabled(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                buton[y][x] = btn;
                center.add(btn);
            }
        }
        add(center, BorderLayout.CENTER);

        northBtn.addActionListener(e -> movePlayer("NORTH"));
        southBtn.addActionListener(e -> movePlayer("SOUTH"));
        eastBtn.addActionListener(e -> movePlayer("EAST"));
        westBtn.addActionListener(e -> movePlayer("WEST"));
        quitBtn.addActionListener(e -> new FinalFrame(game.getCurrCharacter(), game));

        refreshMap();
        refreshPlayerInfo();
    }

    private void movePlayer(String direction) {
        try {
            switch (direction) {
                case "NORTH": 
                    game.getGameGrid().goNorthGUI();
                    break;
                case "SOUTH":
                    game.getGameGrid().goSouthGUI();
                    break;
                case "EAST":  
                    game.getGameGrid().goEastGUI();
                    break;
                case "WEST":  
                    game.getGameGrid().goWestGUI();
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Cell currCell = game.getGameGrid().getcurrCell();
        if (currCell.getType() == CellEntityType.ENEMY && !currCell.isUsed()) {
            Enemy enemy = new Enemy();
            new FightFrame(this, game, enemy);
            currCell.setUsed(true);
            //currCell.setType(CellEntityType.VOID);
        } else if (currCell.getType() == CellEntityType.SANCTUARY && !currCell.isUsed()) {
            JOptionPane.showMessageDialog(this, "Sanctuary found! Restoring HP and Mana...");
            game.getCurrCharacter().restoreHealthMana();
            currCell.setUsed(true);
            //currCell.setType(CellEntityType.VOID);
        } else if (currCell.getType() == CellEntityType.PORTAL) {
            JOptionPane.showMessageDialog(this, "Portal found!Going to next level...");
            dispose();
            game.gotoNextLevel();
            new MainPageGUI(game);
            return;
        }
        refreshMap();
        refreshPlayerInfo();
    }

    private ImageIcon controlSize(String path, int w, int h) {
        ImageIcon old = new ImageIcon(path);
        Image oldImg = old.getImage();
        Image newImg = oldImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void refreshMap() {
        int rows = game.getGameGrid().getLength();
        int cols = game.getGameGrid().getWidth();
        int p1   = game.getGameGrid().getPoz1(); //x
        int p2   = game.getGameGrid().getPoz2(); //y

        int panelW = center.getWidth();
        int panelH = center.getHeight();

        if (panelW <= 0) panelW = 500;
        if (panelH <= 0) panelH = 500;

        int cellW = panelW / cols;
        int cellH = panelH / rows;

        ImageIcon playerIcon    = controlSize("src/imagini/player.png", cellW, cellH);
        ImageIcon enemyIcon     = controlSize("src/imagini/enemyy.png", cellW, cellH);
        ImageIcon sanctuaryIcon = controlSize("src/imagini/sanctuary.png", cellW, cellH);
        ImageIcon portalIcon    = controlSize("src/imagini/portal.png", cellW, cellH);
        ImageIcon voidIcon      = controlSize("src/imagini/void.png", cellW, cellH);
        ImageIcon unknownIcon   = controlSize("src/imagini/unknown.png", cellW, cellH);


        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JButton btn = buton[y][x];
                Cell c = game.getGameGrid().get(y).get(x);

                if (x == p1 && y == p2) {
                    btn.setIcon(playerIcon);
                    btn.setEnabled(true);
                } else {
                    if (!c.isVisited()) {
                        btn.setIcon(unknownIcon);
                    } else {
                        switch (c.getType()) {
                            case ENEMY:
                                btn.setIcon(enemyIcon);
                                btn.setEnabled(true);
                                break;
                            case SANCTUARY:
                                btn.setIcon(sanctuaryIcon);
                                btn.setEnabled(true);
                                break;
                            case PORTAL:
                                btn.setIcon(portalIcon);
                                btn.setEnabled(true);
                                break;
                            case VOID:
                                btn.setIcon(voidIcon);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void refreshPlayerInfo() {
        lvlL.setText("Level: " + game.getCurrCharacter().getlvl());
        expL.setText("Experience: " + game.getCurrCharacter().getExperience());
        healthL.setText("Health: " + game.getCurrCharacter().getCurrentHealth());
        manaL.setText("Mana: " + game.getCurrCharacter().getCurrentMana());
    }
}
