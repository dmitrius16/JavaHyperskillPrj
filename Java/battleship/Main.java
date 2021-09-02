package battleship;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Field field;
    Scanner scanner;
    public static void main(String[] args) {
        initGame();


    }
    public static void outputErrMsg(Ship ship, Field.FieldErrCode errCode) {
        String errStr = "Error!";
        String tryAgainStr = " Try again:";
        String msg = "";
        switch (errCode) {
            case ERR_COORD_FORMAT:
                msg = " Wrong coordinate format!";
                break;
            case ERR_WRONG_SHIP_LEN:
                msg = " Wrong length of the " + ship.getName() + "!";
                break;
            case ERR_SHIP_TOO_CLOSE:
                msg = " You placed it too close to another one.";
                break;
            case ERR_WRONG_SHIP_LOCATION:
                msg = " Wrong ship location!";
                break;
            default:
                msg = " Undefined error";
                break;
        }
        System.out.println(errStr + msg + tryAgainStr);
    }

    public static void outFieldAndRequestCoord(Ship ship) {
        field.outputField();
        System.out.println("Enter the coordinates of the " +
                ship.getName() + " (" +
                ship.getShipLen() + " cells):" );
    }
    public static boolean initGame() {
        ShipType[] shipTypes = {ShipType.AIRCRAFT_CARRIER,
                                ShipType.BATTLESHIP,
                                ShipType.SUBMARINE,
                                ShipType.CRUISER,
                                ShipType.DESTROYER};

        Ship[] ships = new Ship[shipTypes.length];

        Scanner scanner = new Scanner(System.in);
        field = new Field();

        for (int i = 0; i < ships.length; i++) {
            ships[i] = new Ship(shipTypes[i]);
        }
        int indShip = 0;

        outFieldAndRequestCoord(ships[indShip]);
        while(true) {

            String userCoord = scanner.nextLine();

            Field.FieldErrCode errCode = field.addShip(ships[indShip], userCoord);   //here we must analyse user input
            if (errCode == Field.FieldErrCode.SUCCESS) {
                if (indShip < ships.length - 1)
                    indShip++;
                else {
                    field.outputField();
                    break;
                }

                outFieldAndRequestCoord(ships[indShip]);

            } else {
                outputErrMsg(ships[indShip] ,errCode);
            }
            /*
            if (userCoord.equals("exit")) {
                break;
            }*/
        }
        return true;
    }
}
