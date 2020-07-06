package life;
import javax.swing.*;
import java.util.Scanner;
import java.io.IOException;
public class Main {
 /*
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        }
        catch (IOException | InterruptedException e) {}
    }
*/
    public static void main(String[] args) {
        int numGeneration = 100;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGameOfLifeGUI();
            }
        });
    }

    private static void showGameOfLifeGUI() {
        long seed = System.currentTimeMillis();
        /// JFrame f = new GameOfLife(new UniverseStageGenerator(new Universe(sizeGrid, seed)),sizeGrid,sizeGrid);
        JFrame f = new GameOfLife();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}

