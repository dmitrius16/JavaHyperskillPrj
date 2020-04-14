package life;

import java.util.Random;

public class Universe {
    private Random rnd;
    private int stageNum;
    private int liveCell;
    private boolean[][] grid;

    public int getDimension() {return grid.length;}

    public Universe(int sizeDim, long seed) {
        grid = new boolean[sizeDim][sizeDim];
        rnd = new Random(seed);
        for(int i = 0; i < sizeDim; i++) {
            for(int j = 0; j < sizeDim; j++) {
                boolean state = rnd.nextBoolean();
                grid[i][j] = state;
                if(state)
                    liveCell++;
            }
        }
        stageNum = 1;
    }
    // it's debug constructor, clear it after finished project
    public Universe(int sizeDim, String[] pattern) {
        grid = new boolean[sizeDim][sizeDim];
        for(int i = 0; i < sizeDim; i++) {
            for(int j = 0; j < sizeDim; j++) {
                if( i < pattern.length) {
                    if(j < pattern[i].length()) {
                        boolean state = pattern[i].charAt(j) == 'O' ? true : false;
                        grid[i][j] = state;
                        if(state)
                            liveCell++;
                    } else
                        grid[i][j] = false;
                } else {
                    grid[i][j] = false;
                }
            }
        }
        stageNum = 1;
    }
    public void outputState() {
        System.out.printf("Generation #%d\n", stageNum);
        System.out.printf("Alive: %d\n",liveCell);
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

    public void setNextStageParam(int liveCell) {
        this.liveCell = liveCell;
        stageNum++;
    }

    public int getLiveCell() {
        return liveCell;
    }

    public void setCellState(Coord coord, boolean state) {
        grid[coord.getY()][coord.getX()] = state;
    }
}
