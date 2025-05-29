package src.main.java;

import Blocks.Block;
import Blocks.EmptyBlock;
import Blocks.ForestBlock;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    final int SIZE = 8;
    private final Block[][] block;
    private final Player[] players;
    private int currentPlayer = 0;
    private JLabel results;

    public GamePanel() {
        block = new Block[SIZE][SIZE];
        players = new Player[]{
                new Player("Player 1",Color.RED),
                new Player("Player 2",Color.BLUE)
        };

        setPreferredSize(new Dimension(700, 800));
        setLayout(new BorderLayout());
        initGrid();

    }

    public void initResults() {
        results = new JLabel();
        add(results, BorderLayout.SOUTH);
        updateUIData();
    }

    public void initGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b;
                if ((i == 0 && j == 0) || (i == SIZE - 1 && j == SIZE - 1)) {
                    b = new ForestBlock(i, j, null);//badan bardashte mishe
                } else {

                    if (Math.random() < 0.4) {
                        b = new ForestBlock(i, j, null);
                    } else {
                        b = new EmptyBlock(i, j, null);
                    }

                }
                int fx = i;
                int fy = j;
                b.addActionListener(e -> handleClick(fx, fy));
                block[i][j] = b;
                gridPanel.add(b);
            }

        }
        add(gridPanel, BorderLayout.CENTER);
    }

    public void handleClick(int x, int y) {


    }

    public void updateUIData() {
        Player p = players[currentPlayer];
        results.setText(p.getName() + " is turn |" + p.getGold() + "|" + p.getFood() + "|" + p.getUnitSpace());

    }

    public void switchTurn() {
//        setCurrentPlayer(currentPlayer);
        currentPlayer = 1-currentPlayer;
        updateUIData();
    }

//    public void setCurrentPlayer(int currentPlayer) {
//        this.currentPlayer = 1 - currentPlayer;
//    }
}