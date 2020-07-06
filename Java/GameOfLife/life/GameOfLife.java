package life;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GameOfLife extends JFrame {
    private final int sizeGrid = 100;
    private final int numberIteration = 1000;
    private boolean runGame = true;
    private UniverseStageGenerator uniStageGen = new UniverseStageGenerator(new Universe(sizeGrid));
    private JLabel alive;
    private JLabel generation;
    private Timer timer;

    public GameOfLife() {
        super("Game of Life");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        JPanel managementPanel = createManagementPanel();
        JPanel infoPanel = createInfoPanel(managementPanel);
        JPanel cellsPanel = createGameOfLifePanel();
        infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        cellsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        add(infoPanel);
        add(cellsPanel);

        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(runGame) {
                    alive.setText(String.format("Alive: %d", uniStageGen.getUniverse().getLiveCell()));
                    generation.setText(String.format("Generation #%d", uniStageGen.getUniverse().getStageNum()));
                    uniStageGen.calcNextStage();
                    uniStageGen.setNextStage();
                    if (uniStageGen.getUniverse().getStageNum() == numberIteration)
                        timer.stop();
                }
            }
        });
        timer.start();
    }

    private JPanel createManagementPanel() {
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.X_AXIS));

        JButton pauseResumeButton = new JButton("Pause/Resume");
        pauseResumeButton.setName("PlayToggleButton");

///###        pauseResumeButton.addActionListener(actionEvent -> stopStartGame(actionEvent));
        ImageIcon resetIcon = createImageIcon("../resources/pause.png");
        JButton resetButton = new JButton("Reset", resetIcon);

        /*
        resetButton.setName("ResetButton");
        try {
            Image img = ImageIO.read(new File("res/circ-arrow.png"));
            resetButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }*/
///###        resetButton.addActionListener(actionEvent -> resetGame(actionEvent));

        managementPanel.add(pauseResumeButton);
        managementPanel.add(resetButton);
        return managementPanel;
    }

    private JPanel createInfoPanel(JPanel mngmntPanel) {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        mngmntPanel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        info.add(mngmntPanel);
        generation = new JLabel("Generation #1");
        generation.setName("GenerationLabel");
        generation.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        alive = new JLabel("Alive:");
        alive.setName("AliveLabel");
        alive.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        info.add(generation);
        info.add(alive);
        return info;
    }

    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GameOfLife.class.getResource(path);
        if(imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: "+ path);
            return null;
        }
    }

    private JPanel createGameOfLifePanel() {
        JPanel panel = new GameOfLifePanel(uniStageGen.getUniverse(), sizeGrid, sizeGrid);
        return panel;
    }

    private void stopStartGame(ActionEvent evt) {
        runGame = !runGame;
    }

    private void resetGame(ActionEvent evt) {
        runGame = false;
        uniStageGen.resetUniverse();
        runGame = true;
    }
}

class GameOfLifePanel extends JPanel {
    private final int width = 800;
    private final int height = 800;
    private int recWidth;
    private int recHeight;
    private Universe universe;

    GameOfLifePanel(Universe universe, int numCellsX, int numCellsY) {
        recWidth = width / (numCellsX - 1);
        recHeight = height / (numCellsY - 1);
        this.universe = universe;
        setBorder(BorderFactory.createLineBorder(Color.red));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Coord coord = new Coord();
        for (int curY = 0, posY = 0; curY < height; curY += recHeight, posY++) {
            for (int curX = 0, posX = 0; curX < width; curX += recWidth, posX++) {
                coord.setXY(posX, posY);
                boolean state = universe.getCellState(coord);
                if (state) {
                    g.setColor(Color.BLACK);
                    g.fillRect(curX, curY, recWidth, recWidth);
                }
                g.drawRect(curX, curY, recWidth, recWidth);

            }
        }
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}
