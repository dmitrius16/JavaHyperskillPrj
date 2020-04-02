package life;

public class UniverseNextStageGen {

    private boolean[][] nxtStage;
    private GridCell cell;
    private Universe universe;

    public UniverseNextStageGen(Universe universe) {
        int dim = universe.getDimension();
        cell = new GridCell(dim);
        nxtStage = new boolean[dim][dim];
        this.universe = universe;
    }

    public void calcNextStage() {
        cell.setStartPos();
        do {
            int val = calcNeighboors();
            Coord curPos = cell.getCurPos();
            nxtStage[curPos.getY()][curPos.getX()] = (val == 2 || val == 3);
        } while (cell.moveNext());
    }

    public void setNextStage() {
        cell.setStartPos();
        do {
            Coord curPos = cell.getCurPos();
            universe.setCellState(curPos, getNextCellState(curPos));
        } while (cell.moveNext());
    }

    private boolean getNextCellState(Coord coord) {
        return nxtStage[coord.getY()][coord.getX()];
    }

    private int calcNeighboors() {
        int res = 0;
        Coord neibCoord = cell.getNeighbour_E();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_SE();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_S();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_SW();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_W();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_NW();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_N();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        neibCoord = cell.getNeighbour_NE();
        res += (universe.getCellState(neibCoord) ? 1 : 0);
        return res;
    }
}
