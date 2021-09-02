package battleship;

public enum ShipType {
    AIRCRAFT_CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private ShipType(String name, int length) {
        shipLen = length;
        shipName = name;
    }

    public String getName() {return shipName;}
    public int getShipLen() {return shipLen;}

    private String shipName;
    private int shipLen;
}