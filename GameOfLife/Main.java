package life;
import java.util.Scanner;

public class Main {
    private static char[][] grid;
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        String[] usrPar = inp.nextLine().split("\\s+");
        int sizeGrid = Integer.parseInt(usrPar[0]);
        int seed = Integer.parseInt(usrPar[1]);
        int numGeneration = Integer.parseInt(usrPar[2]);
        Universe uni = new Universe(sizeGrid, seed);
        UniverseNextStageGen stageGen = new UniverseNextStageGen(uni);

        int cnt = 1;

        System.out.println("Stage 0:");
        uni.outputState();

        while(numGeneration >= 0) {
            stageGen.calcNextStage();
            stageGen.setNextStage();
            System.out.printf("Stage %d:\n",cnt);

            uni.outputState();

            numGeneration--;
                 cnt++;
        }
       //uni.outputState();
    }

}

