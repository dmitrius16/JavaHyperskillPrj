package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOfLife extends JFrame {
    private JLabel alive;
    private JLabel generation;
    private Timer timer;
    private UniverseStageGenerator uniStageGen;
    public GameOfLife(UniverseStageGenerator universeStage,int numCellsX,int numCellsY) {
        super("Game of Life");
        this.uniStageGen = universeStage;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));

        generation = new JLabel("Generation #1");
        generation.setName("GenerationLabel");

        alive = new JLabel("Alive:");
        alive.setName("AliveLabel");

        info.add(generation);
        info.add(alive);

        add(info);
        GameOfLifePanel panel = new GameOfLifePanel(uniStageGen.getUniverse(),numCellsX, numCellsY);
        add(panel);

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alive.setText(String.format("Alive: %d",uniStageGen.getUniverse().getLiveCell()));
                generation.setText(String.format("Generation #%d",uniStageGen.getUniverse().getStageNum()));
                universeStage.calcNextStage();
                universeStage.setNextStage();
                if(universeStage.getUniverse().getStageNum() == 40)
                    timer.stop();
            }
        });
        timer.start();
    }
}

class GameOfLifePanel extends JPanel {
    private int recWidth;
    private int recHeight;
    private int numXcells;
    private int numYcells;
    private Universe universe;
    private final int width = 300;
    private final int height = 300;

    GameOfLifePanel(Universe universe,int numCellsX, int numCellsY) {
        this.numXcells = numCellsX;
        this.numYcells = numCellsY;
        recWidth = width / (numCellsX - 1);
        recHeight = height/ (numCellsY - 1);
        this.universe = universe;
        setBorder(BorderFactory.createLineBorder(Color.red));
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Coord coord = new Coord();
        for (int curY = 0,posY = 0; curY < height; curY += recHeight, posY++) {
            for (int curX = 0,posX = 0; curX < width; curX += recWidth,posX++) {
                coord.setXY(posX,posY);
                boolean state = universe.getCellState(coord);
                if(state) {
                    g.setColor(Color.BLACK);
                    g.fillRect(curX, curY, recWidth, recWidth);
                }
                g.drawRect(curX,curY,recWidth,recWidth);
                repaint();
            }
        }

    }

    public Dimension getPreferredSize() {
        return new Dimension(width,height);
    }
}
