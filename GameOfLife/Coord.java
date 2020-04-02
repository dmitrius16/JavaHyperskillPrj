package life;

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

    void moveToOrigin() {
        this.x = 0;
        this.y = 0;
    }



    int getX() {return x;}
    int getY() {return y;}

    @Override
    public String toString() {
        return String.format("(x: %d, y: %d)",x, y);
    }
}
