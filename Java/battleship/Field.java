package battleship;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Field {
    private final int sizeField = 10;
    List<Ship> shipsList;
    private char[][] myField;
    private char[] cellNeighborVal;
    private boolean fogOfWarEnable;
    private final char fogOfWar = '~';
    private final char shipPart = 'O';
    private final char propablyShipPart = 'U';
    private final char missShot = 'M';
    private final char hitShot = 'X';

    public Field() {
        myField = new char[sizeField][sizeField];
        cellNeighborVal = new char[9];
        for (int i = 0; i < sizeField; i++) {
            Arrays.fill(myField[i], '~');
        }
        shipsList = new LinkedList<>();
        fogOfWarEnable = false;
    }

    public void enableFogOfWar() {fogOfWarEnable = true;}
    public void disableFogOfWar() {fogOfWarEnable = false;}

    public FieldErrCode addShip(Ship ship, String coord) {
        // 1 - check input coord, here we can run into two errors: 1 - error format, 2 - out of field
        // 2 - check if we can place ship into coord: here may occur 3 type errors
        // 3 - place
        // check for NULL
        // checkFormat
        // get ShipCoord
        // checkOutOfField
        FieldErrCode err = FieldErrCode.ERR_UNDEFINED;
        if (ship != null && coord != null) {
            if (checkShipCoords(coord)) {
                Ship.ShipCoord shipCoord = convertStrToShipCoord(coord);
                if (shipCoord.isHorizontalPlacement() != shipCoord.isVerticalPlacement()) {
                    if (ship.getShipLen() == shipCoord.getLen()) {
                        // check location relatively other ships
                        if (tryLocateShips(shipCoord)) {
                            ship.setShipCoord(shipCoord);
                            shipsList.add(ship);
                            err = FieldErrCode.SUCCESS;
                        } else {
                            err = FieldErrCode.ERR_SHIP_TOO_CLOSE;
                        }
                    } else {
                        err = FieldErrCode.ERR_WRONG_SHIP_LEN;
                    }
                } else {
                    err = FieldErrCode.ERR_WRONG_SHIP_LOCATION;
                }
            } else {
                err = FieldErrCode.ERR_COORD;
            }
        }
        return err;
    }

    public void outputField() {

        char stSym = 'A';
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < sizeField; i++) {
            System.out.print(stSym + " ");
            for (int j = 0; j < sizeField; j++) {
                if (fogOfWarEnable && myField[i][j] == shipPart) {
                    System.out.print(fogOfWar + " ");
                } else {
                    System.out.print(myField[i][j] + " ");
                }
            }
            System.out.println();
            stSym += 1;
        }
    }

    public ShotStatus makeShot(String coord) {
        if (!checkCoord(coord)) {
            return ShotStatus.WRONG_COORDINATE;
        }
        Coord shotCoord = convertStrToCoord(coord);
        ShotStatus res = ShotStatus.MISSED;
        for (Ship ship : shipsList) {
            if (ship.isHitted(shotCoord)) {
                res = ShotStatus.HIT;
                myField[shotCoord.getY()][shotCoord.getX()] = hitShot;
                if (ship.shipIsKilled()) {
                    res = ShotStatus.HIT_SHIP_DESTROYED;
                }
                break;
            }
        }

        if (res == ShotStatus.MISSED) {
            myField[shotCoord.getY()][shotCoord.getX()] = missShot;
        }

        return res;
    }

    public boolean isAllShipsSank() {
        boolean res = true;
        for (Ship ship : shipsList) {
            res = res && ship.shipIsKilled();
        }
        return res;
    }

    private boolean tryLocateShips(Ship.ShipCoord shipCoords) {
        Coord coord = new Coord(shipCoords.getHeadCoord().getX(), shipCoords.getHeadCoord().getY());
        int cur_X = coord.getX();
        int cur_Y = coord.getY();
        boolean success = true;
        for (int i = 0; i < shipCoords.getLen(); i++) {
            if (shipCoords.isHorizontalPlacement()) {
                coord.setX(cur_X + i);
            } else {
                coord.setY(cur_Y + i);
            }
            if (isCellFree(coord)) {
                myField[coord.getY()][coord.getX()] = propablyShipPart;
            } else {
                success = false;
                break;
            }
        }

        coord.setXY(cur_X, cur_Y);        //clear all M
        for (int i = 0; i < shipCoords.getLen(); i++) {
            if (shipCoords.isHorizontalPlacement()) {
                coord.setX(cur_X + i);
            } else {
                coord.setY(cur_Y + i);
            }
            if (!success) {
                if (myField[coord.getY()][coord.getX()] == propablyShipPart) {
                    myField[coord.getY()][coord.getX()] = fogOfWar;
                }
            } else {
                if (myField[coord.getY()][coord.getX()] == propablyShipPart) {
                    myField[coord.getY()][coord.getX()] = shipPart;
                }
            }
        }

        return success;
    }

    private boolean checkCoord(String coord) {
        boolean res = true;
        try {
            res = res && (coord.charAt(0) >= 'A' && coord.charAt(0) <= 'J');
            int num = Integer.parseInt(coord.substring(1));
            res = res && (num >= 1 && num <= sizeField);

        } catch (NumberFormatException e) {
            res = false;
        }
        return res;
    }

    private boolean checkShipCoords(String coord) {
        String[] temp = coord.split("\\s+");
        boolean res = false;
        if (temp.length == 2) {
            res = checkCoord(temp[0]) && checkCoord(temp[1]);            
        }
        return res;
    }

    private Coord convertStrToCoord(String coord) {
        int y = coord.charAt(0) - 'A';
        int x = Integer.parseInt(coord.substring(1)) - 1;
        return new Coord(x, y);
    }

    private Ship.ShipCoord convertStrToShipCoord(String coord) {
        String[] temp = coord.split("\\s+");
        Coord head = convertStrToCoord(temp[0]);
        Coord tail = convertStrToCoord(temp[1]);
        if (tail.getX() < head.getX() ||
            tail.getY() < head.getY()) {
            Coord tempCoord = head;
            head = tail;
            tail = tempCoord;
        }
        return new Ship.ShipCoord(head, tail);
    }

    private boolean isCoordInField(Coord coord) {
        return coord.getX() >= 0 && coord.getX() < sizeField &&
               coord.getY() >= 0 && coord.getY() < sizeField;
    }

    private boolean isCellFree(Coord coord) {
        cellNeighborVal[0] = getValFieldCell(coord);
        Coord neighborCoord = coord.getNeighbor_W();
        cellNeighborVal[1]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_NW();
        cellNeighborVal[2]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_N();
        cellNeighborVal[3]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_NE();
        cellNeighborVal[4]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeghbor_E();
        cellNeighborVal[5]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_SE();
        cellNeighborVal[6]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_S();
        cellNeighborVal[7]  = getValFieldCell(neighborCoord);
        neighborCoord = coord.getNeighbor_SW();
        cellNeighborVal[8] = getValFieldCell(neighborCoord);

        boolean res = true;
        for (int i = 0; i < cellNeighborVal.length; i++) {
            res = res && (cellNeighborVal[i] == fogOfWar || cellNeighborVal[i] == propablyShipPart);
        }
        return res;
    }

    private char getValFieldCell(Coord coord) {
        char val = '~'; //
        if (isCoordInField(coord)) {
            val = myField[coord.getY()][coord.getX()];
        }
        return val;
    }

    static public enum FieldErrCode {
        SUCCESS,
        ERR_COORD,   // here all error if string name err and if col name err
        ERR_WRONG_SHIP_LEN,
        ERR_WRONG_SHIP_LOCATION,
        ERR_SHIP_TOO_CLOSE,
        ERR_UNDEFINED;
    }

    static public enum ShotStatus {
        MISSED,
        HIT,
        HIT_SHIP_DESTROYED,
        WRONG_COORDINATE;
    }
}
