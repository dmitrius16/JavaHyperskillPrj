package battleship;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Field {
    private final int sizeField = 10;
    List<Ship> shipsList;
    private char[][] field;
    private char[] cellNeighborVal;
    private final char fogOfWar = '~';
    private final char shipPart = 'O';
    private final char propablyShipPart = 'U';
    public Field() {
        field = new char[sizeField][sizeField];
        cellNeighborVal = new char[9];
        for (int i = 0; i < sizeField; i++) {
            Arrays.fill(field[i], '~');
        }
        shipsList = new LinkedList<>();
    }

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
            if (checkCoordFormat(coord)) {
                Ship.ShipCoord shipCoord = convertStrToShipCoord(coord);
                if (shipCoord.isHorizontalPlacement() != shipCoord.isVerticalPlacement()) {
                    if (ship.getShipLen() == shipCoord.getLen()) {
                        // check location relatively other ships
                        if (tryLocateShips(shipCoord)) {
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
                err = FieldErrCode.ERR_COORD_FORMAT;
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
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
            stSym += 1;
        }
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
                field[coord.getY()][coord.getX()] = propablyShipPart;
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
                if (field[coord.getY()][coord.getX()] == propablyShipPart) {
                    field[coord.getY()][coord.getX()] = fogOfWar;
                }
            } else {
                if (field[coord.getY()][coord.getX()] == propablyShipPart) {
                    field[coord.getY()][coord.getX()] = shipPart;
                }
            }
        }

        return success;
    }

    private boolean checkCoordFormat(String coord) {
        String[] temp = coord.split("\\s+");
        boolean res = false;
        if (temp.length == 2) {
            // check alpha column
            boolean resAlpha = true;
            boolean resDigit = true;
            try {
                for (int i = 0; i < temp.length; i++ ) {
                    resAlpha = resAlpha && (temp[i].charAt(0) >= 'A' && temp[i].charAt(0) <= 'J');
                    int num = Integer.parseInt(temp[0].substring(1));
                    resDigit = resDigit && (num >= 1 && num <= sizeField);
                }
            } catch (NumberFormatException e) {
                resDigit = false;
            }
            res = resAlpha && resDigit;
        }
        return res;
    }

    private Ship.ShipCoord convertStrToShipCoord(String coord) {
        String[] temp = coord.split("\\s+");
        Coord head = new Coord();
        Coord tail = new Coord();
        Coord[] shipCoord = new Coord[]{head, tail};
        for (int i = 0; i < 2; i++) {
            int y = temp[i].charAt(0) - 'A';
            int x = Integer.parseInt(temp[i].substring(1)) - 1;
            shipCoord[i].setXY(x, y);
        }


        if (shipCoord[1].getY() < shipCoord[0].getY() ||
            shipCoord[1].getX() < shipCoord[0].getX()) {
            head = shipCoord[1];
            tail = shipCoord[0];
        }

        return new Ship.ShipCoord(head, tail);
    }

    private boolean isCoordInField(Coord coord) {
        return coord.getX()>=0 && coord.getX() < sizeField &&
               coord.getY()>=0 && coord.getY() < sizeField;
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
            val = field[coord.getY()][coord.getX()];
        }
        return val;
    }

    static public enum FieldErrCode {
        SUCCESS,
        ERR_COORD_FORMAT,   // here all error if string name err and if col name err

        ERR_WRONG_SHIP_LEN,
        ERR_WRONG_SHIP_LOCATION,
        ERR_SHIP_TOO_CLOSE,
        ERR_UNDEFINED;
    }
}
