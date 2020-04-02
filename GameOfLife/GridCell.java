package life;

public class GridCell {
    private Coord currentPos;
    private Coord neighbour;
    int dim;

    public GridCell(int dim){
        this.dim = dim;
        currentPos = new Coord();
        neighbour = new Coord();
    }

    public void setStartPos() {
        currentPos.moveToOrigin();
        neighbour.moveToOrigin();
    }

    Coord getCurPos() {
        return currentPos;
    }

    public Coord getNeighbour_S() {
        neighbour.setCoord(currentPos);
        int y = neighbour.getY();
        y = (y + 1) % dim;
        neighbour.setY(y);
        return neighbour;
    }

    public Coord getNeighbour_SW() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        int y = neighbour.getY();
        x = x > 0 ? x - 1 : dim - 1;
        y = (y + 1) % dim;
        neighbour.setXY(x,y);
        return neighbour;
    }

    public Coord getNeighbour_SE() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        int y = neighbour.getY();
        y = (y + 1) % dim;
        x = (x + 1) % dim;
        neighbour.setXY(x,y);
        return neighbour;
    }

    public Coord getNeighbour_W() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        x = x > 0 ? x - 1 : dim - 1;
        neighbour.setX(x);
        return neighbour;
    }

    public Coord getNeighbour_E() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        x = (x + 1) % dim;
        neighbour.setX(x);
        return neighbour;
    }

    public Coord getNeighbour_NW() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        int y = neighbour.getY();
        x = x > 0 ? x - 1 : dim - 1;
        y = y > 0 ? y - 1 : dim - 1;
        neighbour.setXY(x,y);
        return neighbour;
    }

    public Coord getNeighbour_N() {
        neighbour.setCoord(currentPos);
        int y = neighbour.getY();
        y = y > 0 ? y - 1 : dim - 1;
        neighbour.setY(y);
        return neighbour;
    }

    public Coord getNeighbour_NE() {
        neighbour.setCoord(currentPos);
        int x = neighbour.getX();
        int y = neighbour.getY();
        x = (x + 1) % dim;
        y = y > 0 ? y - 1 : dim - 1;
        neighbour.setXY(x,y);
        return neighbour;
    }

    public boolean moveNext() {
        boolean res = true;
        int x = currentPos.getX();
        int y = currentPos.getY();
        x++;
        if(x == dim) {
            x = 0;
            y++;
        }
        if(y == dim) {
            res = false;
        } else {
            currentPos.setXY(x,y);
            neighbour.setXY(x,y);
        }
        return res;
    }

}
