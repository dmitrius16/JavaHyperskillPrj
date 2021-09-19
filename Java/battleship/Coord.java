package battleship;

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord coord) {
        setCoord(coord);
    }

    public Coord() {
        this(0, 0);
    }

    public void setCoord(Coord coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveToOrigin() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {return x;}
    public int getY() {return y;}

    //
    public Coord getNeighbor_NE() {
        int _x = this.x - 1;
        int _y = this.y - 1;
        return new Coord(_x, _y);
    }

    public Coord getNeighbor_N() {
        int _y = this.y - 1;
        return new Coord(this.x, _y);
    }

    public Coord getNeighbor_NW() {
        int _x = this.x + 1;
        int _y = this.y - 1;
        return new Coord(_x, _y);
    }

    public Coord getNeighbor_W() {
        int _x = this.x + 1;
        return new Coord(_x, this.y);
    }

    public Coord getNeighbor_SW() {
        int _x = this.x + 1;
        int _y = this.y + 1;
        return new Coord(_x, _y);
    }

    public Coord getNeighbor_S() {
        int _y = this.y + 1;
        return new Coord(this.x, _y);
    }

    public Coord getNeighbor_SE() {
        int _x = this.x - 1;
        int _y = this.y + 1;
        return new Coord(_x, _y);
    }

    public Coord getNeghbor_E() {
        int _x = this.x - 1;
        return new Coord(_x, this.y);
    }


    @Override
    public String toString() {
        return String.format("(x: %d, y: %d)",x, y);
    }
}
