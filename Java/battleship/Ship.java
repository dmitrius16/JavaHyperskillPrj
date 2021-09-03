package battleship;
import java.util.Arrays;
public class Ship {
    public static class ShipCoord {
        private Coord head;
        private Coord tail;
        public ShipCoord(Coord _head, Coord _tail) {
            head = _head;
            tail = _tail;
        }
        Coord getHeadCoord() {return head;}
        Coord getTailCoord() {return tail;}
        boolean isHorizontalPlacement() {
            return head.getY() == tail.getY();
        }
        boolean isVerticalPlacement() {
            return head.getX() == tail.getX();
        }
        int getLen() {
            int len = 0;
            if (isHorizontalPlacement()) {
                len = tail.getX() - head.getX() + 1;
            } else {
                len = tail.getY() - head.getY() + 1;
            }
            return len;
        }
    }

    private final char LiveCell = 'o';
    private final char DeadCell = 'x';
    private ShipType shipType;
    private ShipCoord shipCoord;
    private boolean killed;
    private char[] shipBody;

    public Ship(ShipType type) {
        shipType = type;
        killed = false;
        shipBody = new char[shipType.getShipLen()];
        Arrays.fill(shipBody, LiveCell);
    }
    public void setShipCoord(ShipCoord coord) {
        shipCoord = coord;
    }

    public int getShipLen() {return shipType.getShipLen();}

    public boolean isHitted(Coord coord) {
        boolean res = false;

        if (shipCoord.isHorizontalPlacement()) {
            if (shipCoord.head.getY() == coord.getY()) {
                if (coord.getX() >= shipCoord.head.getX() && coord.getX() <= shipCoord.tail.getX()) {
                    res = true;
                    int curInd = shipCoord.tail.getX() - coord.getX();
                    shipBody[curInd] = DeadCell;
                }
            }
        } else {
            if (shipCoord.head.getX() == coord.getX()) {
                if (coord.getY() >= shipCoord.head.getY() && coord.getY() <= shipCoord.tail.getY()) {
                    res = true;
                    int curInd = shipCoord.tail.getY() - coord.getY();
                    shipBody[curInd] = DeadCell;
                }
            }
        }

        //check if ship is killed
        boolean isDead = true;
        for (int i = 0; i < getShipLen(); i++) {
            isDead = isDead && (shipBody[i] == DeadCell);
        }
        killed = isDead;
        return res;
    }
    public boolean shipIsKilled() {return killed;}
    public ShipType getType() {
        return shipType;
    }
    public String getName() {return shipType.getName();}
}

