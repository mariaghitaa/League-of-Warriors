package tema;

public class Cell {
    private int x;
    private int y;
    private CellEntityType type;
    private boolean visited;
    private boolean used;

    public Cell(int x, int y, CellEntityType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.visited = (type == CellEntityType.PLAYER);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public CellEntityType getType() {
        return type;
    }

    public void setType(CellEntityType type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

}
