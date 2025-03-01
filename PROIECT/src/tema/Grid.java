package tema;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private int width, length;
    private Character cplayer;
    private int poz1, poz2;

    private Grid(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public static Grid generateMapHarcodata(Character player) {
        int length = 5;
        int width = 5;
        Grid grid = new Grid(length, width);
        for (int i = 0; i < length; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Cell(j, i, CellEntityType.VOID));
            }
            grid.add(row);
        }
        //conform CERINTEI
        grid.get(0).get(0).setVisited(true);
        player.currHealth = player.maxHealth;
        player.currMana = player.maxMana;
        grid.poz1 = 0;
        grid.poz2 = 0;

        grid.get(0).set(3, new Cell(3, 0, CellEntityType.SANCTUARY));
        grid.get(2).set(0, new Cell(0, 1, CellEntityType.SANCTUARY));
        grid.get(1).set(3, new Cell(3, 1, CellEntityType.SANCTUARY));
        grid.get(4).set(3, new Cell(4, 3, CellEntityType.SANCTUARY));
        grid.get(3).set(4, new Cell(4, 2, CellEntityType.ENEMY));
        grid.get(4).set(4, new Cell(4, 4, CellEntityType.PORTAL));
        
        grid.cplayer = player;
        return grid;
    }

    //metoda pt generare random
    public static Grid generateRandomMap(int length, int width, Character player) {
        if (length < 3 || width < 3) {
            throw new IllegalArgumentException("The dimensions are too small.");
        }
        if (length > 10 || width > 10) {
            throw new IllegalArgumentException("Map dimensions must be max 10x10.");
        }
        Grid grid = new Grid(length, width);
        for (int i = 0; i < length; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Cell(j, i, CellEntityType.VOID));
            }
            grid.add(row);
        }

        Random random = new Random();
        int p1 = random.nextInt(width);
        int p2 = random.nextInt(length);
        grid.get(p2).get(p1).setVisited(true);
        player.currHealth = player.maxHealth;
        player.currMana = player.maxMana;
        grid.poz1 = p1;
        grid.poz2 = p2;
        grid.cplayer = player;

        placeEntity(grid, CellEntityType.PORTAL, 1);
        placeEntity(grid, CellEntityType.SANCTUARY, 2);
        placeEntity(grid, CellEntityType.ENEMY, 4);

        return grid;
    }

    private static void placeEntity(Grid grid, CellEntityType type, int cnt) {
        Random rand = new Random();
        int placed = 0;
        int pos = 0;
        while (placed < cnt && pos < 1000) {
            pos++;
            int x = rand.nextInt(grid.width);
            int y = rand.nextInt(grid.length);
            Cell cell = grid.get(y).get(x);
            if (!(x == grid.poz1 && y == grid.poz2) && cell.getType() == CellEntityType.VOID) {
                cell.setType(type);
                placed++;
            }
        }
        if(placed < cnt) {
            throw new IllegalStateException("Failed to place all entities.");
        }
    }

    public void printMap() {
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = this.get(y).get(x);
                char s;
                if (x == poz1 && y == poz2) {
                    s = 'P';
                } else {
                    if (!cell.isVisited()) {
                        s = 'N';
                    } else {
                        switch (cell.getType()) {
                            case ENEMY:
                                s = 'E';
                                break;
                            case SANCTUARY:
                                s = 'S';
                                break;
                            case PORTAL:
                                s = 'F';
                                break;
                            case VOID:
                            default:
                                s = 'V';
                                break;
                        }
                    }
                }
                System.out.print(s + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void goNorthTerminal() throws ImpossibleMove {
        if (poz2 - 1 < 0) {
            throw new ImpossibleMove("Cannot move north.");
        }
        movePlayerToInTerminal(poz1, poz2 - 1);
    }

    public void goSouthTerminal() throws ImpossibleMove {
        if (poz2 + 1 >= length) {
            throw new ImpossibleMove("Cannot move south.");
        }
        movePlayerToInTerminal(poz1, poz2 + 1);
    }

    public void goWestTerminal() throws ImpossibleMove {
        if (poz1 - 1 < 0) {
            throw new ImpossibleMove("Cannot move west.");
        }
        movePlayerToInTerminal(poz1 - 1, poz2);
    }

    public void goEastTerminal() throws ImpossibleMove {
        if (poz1 + 1 >= width) {
            throw new ImpossibleMove("Cannot move east.");
        }
        movePlayerToInTerminal(poz1 + 1, poz2);
    }

    public Cell getcurrCell () {
        return this.get(poz2).get(poz1);
    }

    private void movePlayerToInTerminal(int newX, int newY) {
        this.get(poz2).get(poz1).setVisited(true);
        poz1 = newX;
        poz2 = newY;
        this.get(poz2).get(poz1).setVisited(true);
        printMap();
    }

    private void movePlayerToInGUI(int newX, int newY) {
        this.get(poz2).get(poz1).setVisited(true);
        poz1 = newX;
        poz2 = newY;
        this.get(poz2).get(poz1).setVisited(true);
    }

    public void goNorthGUI() throws ImpossibleMove {
        if (poz2 - 1 < 0) {
            throw new ImpossibleMove("Cannot move north.");
        }
        movePlayerToInGUI(poz1, poz2 - 1);
    }

    public void goSouthGUI() throws ImpossibleMove {
        if (poz2 + 1 >= length) {
            throw new ImpossibleMove("Cannot move south.");
        }
        movePlayerToInGUI(poz1, poz2 + 1);
    }

    public void goWestGUI() throws ImpossibleMove {
        if (poz1 - 1 < 0) {
            throw new ImpossibleMove("Cannot move west.");
        }
        movePlayerToInGUI(poz1 - 1, poz2);
    }

    public void goEastGUI() throws ImpossibleMove {
        if (poz1 + 1 >= width) {
            throw new ImpossibleMove("Cannot move east.");
        }
        movePlayerToInGUI(poz1 + 1, poz2);
    }

    public int getPoz1() {
        return poz1;
    }
    public int getPoz2() {
        return poz2;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}