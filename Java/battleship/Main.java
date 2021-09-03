package battleship;


import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Field field;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initGame();
        //initPredifinedGame();
        processGame();

    }
    public static void outputErrMsg(Ship ship, Field.FieldErrCode errCode) {
        String errStr = "Error!";
        String tryAgainStr = " Try again:";
        String msg = "";
        switch (errCode) {
            case ERR_COORD:
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

    public static void processGame() {
        System.out.println("The game starts!");
        field.enableFogOfWar();
        field.outputField();
        System.out.println("Take a shot!");
        while(true) {
            String hitCoord =  scanner.nextLine();
            Field.ShotStatus  shotStatus = field.makeShot(hitCoord);
            switch (shotStatus) {
                case HIT:
                    field.outputField();
                    System.out.println("You hit a ship! Try again:");
                    break;
                case HIT_SHIP_DESTROYED:
                    field.outputField();
                    if (field.isAllShipsSank()) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        return;
                    } else {
                        System.out.println("You sank a ship! Specify a new target:");
                    }
                    break;
                case MISSED:
                    field.outputField();
                    System.out.println("You missed! Try again:");
                    break;
                case WRONG_COORDINATE:
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                    break;
            }
        }
    }

    public static boolean initPredifinedGame() {
        ShipType[] shipTypes = {ShipType.AIRCRAFT_CARRIER,
                ShipType.BATTLESHIP,
                ShipType.SUBMARINE,
                ShipType.CRUISER,
                ShipType.DESTROYER};

        Ship[] ships = new Ship[shipTypes.length];
        field = new Field();
        for (int i = 0; i < ships.length; i++) {
            ships[i] = new Ship(shipTypes[i]);
        }
        String[] coords = {"F3 F7", "A1 D1", "J8 J10", "B9 D9", "I2 J2"};
        for (int i = 0; i < coords.length; i++) {
            field.addShip(ships[i], coords[i]);
        }
        field.outputField();
        return true;
    }

    public static boolean initGame() {
        ShipType[] shipTypes = {ShipType.AIRCRAFT_CARRIER,
                                ShipType.BATTLESHIP,
                                ShipType.SUBMARINE,
                                ShipType.CRUISER,
                                ShipType.DESTROYER};

        Ship[] ships = new Ship[shipTypes.length];

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
        }


        return true;
    }
}
