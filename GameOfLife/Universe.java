package life;

import java.util.Random;

public class Universe {
    private Random rnd;
    private boolean[][] grid;

    public int getDimension() {return grid.length;}

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

    public boolean getCellState(Coord coord) {
        return grid[coord.getY()][coord.getX()];
    }

    public void setCellState(Coord coord, boolean state) {
        grid[coord.getY()][coord.getX()] = state;
    }
}
