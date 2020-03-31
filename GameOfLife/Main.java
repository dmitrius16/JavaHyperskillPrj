package life;
import java.util.Scanner;
import java.util.Random;
public class Main {
    private static Random rnd;
    private static char[][] grid;
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        String[] usrPar = inp.nextLine().split("\\s+");
        int sizeGrid = Integer.parseInt(usrPar[0]);
        int seed = Integer.parseInt(usrPar[1]);
        Universe uni = new Universe(sizeGrid, seed);
        UniStageGenerator gen = new UniStageGenerator(uni);
        gen.calcNeighboor();
        uni.outputState();
    }

}

class Universe {
    private Random rnd;
    private boolean[][] grid;

    public int getDimmension() {return grid.length;}

    public Universe(int sizeDim, int seed) {
        grid = new boolean[sizeDim][sizeDim];
        rnd = new Random(seed);
        for(int i = 0; i < sizeDim; i++) {
            for(int j = 0; j < sizeDim; j++) {
                grid[i][j] = rnd.nextBoolean();
            }
        }
    }

    public void outputState() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] ? 'O' : ' ');
            }
            System.out.println();
        }
    }
}

class UniStageGenerator {
    private boolean[][] nxtStage;
    private UniCoord curCoord;
    private UniCoord neighborCoord;
    public UniStageGenerator(Universe universe) {
        int dim = universe.getDimmension();
        nxtStage = new boolean[dim][dim];
        curCoord = new UniCoord(0,0,dim);
        neighborCoord = new UniCoord(curCoord);

    }

    public void nextStage(Universe universe) {

    }

    public int calcNeighboor() {

        int x = curCoord.prevCoord(curCoord.getX());    // как то так надо
        neighborCoord.setCoord(x,);
        /*
        System.out.println(curCoord);
        getNeighborSouth(curCoord);
        System.out.println(curCoord);

         */
        return 0;
    }

    private UniCoord getNeighbor_S(UniCoord coord) {
        neighborCoord.setCoord(coord);
        neighborCoord.nextY();
        return neighborCoord;
    }

    private UniCoord getNeighbor_SW(UniCoord coord) {
        neighborCoord.setCoord(coord);
        neighborCoord.nextY();
        neighborCoord.prevX();
        return neighborCoord;

    }

    private UniCoord getNeighbor_SE(UniCoord coord) {
        neighborCoord.setCoord(coord);
        neighborCoord.nextY();
        neighborCoord.nextX();
        return neighborCoord;
    }

    private UniCoord getNeighbor_W(UniCoord coord) {
        neighborCoord.setCoord(coord);
        neighborCoord.prevX();
        return neighborCoord;
    }

    private UniCoord getNeighbor_E(UniCoord coord) {
        neighborCoord.setCoord(coord);
        neighborCoord.nextX();
        return neighborCoord;
    }

    private UniCoord getNeighbor_NW(UniCoord coord)
}

class UniCoord {
    private int x;
    private int y;
    private int dim;

    private int prevCoord(int coord) {
        if(coord == 0)
            coord = (dim - 1);
        else
            coord--;
        return coord;
    }

    private int nextCoord(int coord) {
        coord = (coord + 1) % dim;
        return coord;
    }

    public UniCoord(int x, int y,int dim) {
        this.x = x;
        this.y = y;
        this.dim = dim;
    }

    public UniCoord(UniCoord coord) {
        setCoord(coord);
        this.dim = coord.dim;
    }

    public void setCoord(UniCoord coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    int getX() {return x;}
    int getY() {return y;}


    int nextX() {
        this.x = nextCoord(this.x);
        return this.x;
    }
    int nextY() {
        this.y = nextCoord(this.y);
        return this.y;
    }
    int prevX() {
        this.x = prevCoord(this.x);
        return this.x;
    }
    int prevY() {
        this.y = prevCoord(this.y);
        return this.y;
    }
    @Override
    public String toString() {
        return String.format("(x - %d, y - %d)",x, y);
    }
}
