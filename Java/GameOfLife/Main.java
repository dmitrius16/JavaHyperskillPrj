package life;
import java.util.Scanner;
import java.io.IOException;
public class Main {

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e) {}
    }

    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        String[] usrPar = inp.nextLine().split("\\s+");
        int sizeGrid = Integer.parseInt(usrPar[0]);

        long seed = System.currentTimeMillis();

       ///#### int numGeneration = Integer.parseInt(usrPar[2]);
        Universe uni = new Universe(sizeGrid, seed);
        UniverseNextStageGen stageGen = new UniverseNextStageGen(uni);

        int outputGeneration = 15;
        Thread curThread = Thread.currentThread();
        while(outputGeneration >= 0) {
            uni.outputState();
            stageGen.calcNextStage();
            stageGen.setNextStage();
            outputGeneration--;
            try {
                curThread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            clearScreen();
        }
    }

}

