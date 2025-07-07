package src.main.java;

import Blocks.Block;
import Blocks.EmptyBlock;
import Blocks.ForestBlock;
import Structure.TownHall;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public final int SIZE = 10;
    private final Block[][] block;
    private final Player[] players;
    private HUDPanel hudPanel;
    private GameController controller;

    public GamePanel() {
        block = new Block[SIZE][SIZE];
        players = new Player[]{
                new Player("Player 1", Color.RED),
                new Player("Player 2", Color.BLUE)
        };

        setPreferredSize(new Dimension(900, 850));
        setLayout(new BorderLayout());

        initGrid();
        initHUD();
    }

    private void initGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b;

                if (i == 0 && j == 0) {
                    b = new EmptyBlock(i, j, players[0], new TownHall());
                } else if (i == SIZE - 1 && j == SIZE - 1) {
                    b = new EmptyBlock(i, j, players[1], new TownHall());
                } else {
                    if (Math.random() < 0.4) {
                        b = new ForestBlock(i, j, null, null);
                    } else {
                        b = new EmptyBlock(i, j, null, null);
                    }
                }

                int fx = i, fy = j;
                b.addActionListener(e -> handleClick(fx, fy));
                block[i][j] = b;

                gridPanel.add(b);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initHUD() {
        hudPanel = new HUDPanel();
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    private void handleClick(int x, int y) {
        if (controller != null) {
            controller.handleBlockClick(block[x][y]);
        }
    }

    public Block getBlock(int x, int y) {
        return block[x][y];
    }
}
