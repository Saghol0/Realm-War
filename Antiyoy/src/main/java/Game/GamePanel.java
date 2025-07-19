package Game;


import Blocks.Block;
import Blocks.EmptyBlock;
import Blocks.ForestBlock;
import Structure.TownHall;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    public final int SIZE = 10;
    private Block[][] block;
    private Player[] players;
    private HUDPanel hudPanel;
    private GameController controller;

    public GamePanel(Player[] players) {
        block = new Block[SIZE][SIZE];
        this.players = players;
        setPreferredSize(new Dimension(900, 850));
        setLayout(new BorderLayout());

        initGrid(players.length);
        initHUD();

    }

    private void initGrid(int length) {
        if (length == 2) initGrid2Player();
        if (length == 4) initGrid4Player();
    }


    private void initHUD() {
        hudPanel = new HUDPanel();
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }

    public void setHudPanel(HUDPanel hudPanel) {
        this.hudPanel = hudPanel;
    }

    public Player[] getPlayers() {
        return players;
    }

    private void handleClick(int x, int y) {
        if (controller != null) {
            controller.handleBlockClick(block[x][y]);
        }
    }

    public Block getBlock(int x, int y) {
        return block[x][y];
    }

    public Block[][] getBlocks() {
        return block;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void loadGame(Block[][] blocks, List<Player> playerList) {
        this.block = blocks;
        this.players = playerList.toArray(new Player[0]);
        removeAll();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b = block[i][j];
                if (b.getOwner() != null) {
                    if(blocks[0][0].getStructure()!=null && blocks[0][0].getStructure().getName().equals("Town Hall")) {
                        if (b.getOwner().getName().equals(blocks[0][0].getOwner().getName())) {
                            b.setOwner(blocks[0][0].getOwner());
                        }
                    }
                    if(blocks[9][9].getStructure()!=null && blocks[9][9].getStructure().getName().equals("Town Hall")) {
                        if (b.getOwner().getName().equals(blocks[9][9].getOwner().getName())) {
                            b.setOwner(blocks[9][9].getOwner());
                        }
                    }
                    if(blocks[0][9].getStructure()!=null && blocks[0][9].getStructure().getName().equals("Town Hall")) {
                        if (b.getOwner().getName().equals(blocks[0][9].getOwner().getName())) {
                            b.setOwner(blocks[0][9].getOwner());
                        }
                    }
                    if(blocks[9][0].getStructure()!=null && blocks[9][0].getStructure().getName().equals("Town Hall")) {
                        if (b.getOwner().getName().equals(blocks[9][0].getOwner().getName())) {
                            b.setOwner(blocks[9][0].getOwner());
                        }
                    }
                }

                int fx = i, fy = j;
                b.addActionListener(e -> handleClick(fx, fy));

                gridPanel.add(b);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public GameController getController() {
        return controller;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void initGrid2Player() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b;
                if (i == 0 && j == 0) {
                    b = new EmptyBlock(i, j, players[0], new TownHall(), null);
                } else if (i == SIZE - 1 && j == SIZE - 1) {
                    b = new EmptyBlock(i, j, players[1], new TownHall(), null);
                } else if ((0 <= i && 1 >= i) && (0 <= j && 1 >= j)) {
                    b = new EmptyBlock(i, j, players[0], null, null);
                } else if ((SIZE - 2 <= i && SIZE - 1 >= i) && (SIZE - 2 <= j && SIZE - 1 >= j)) {
                    {
                        b = new EmptyBlock(i, j, players[1], null, null);
                    }
                } else {
                    if (Math.random() < 0.4) {
                        b = new ForestBlock(i, j, null, null, null);
                    } else {
                        b = new EmptyBlock(i, j, null, null, null);
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

    public void initGrid4Player() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));
        block = new Block[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b;

                if (i == 0 && j == 0) {
                    b = new EmptyBlock(i, j, players[0], new TownHall(), null);
                } else if (i <= 1 && j <= 1) {
                    b = new EmptyBlock(i, j, players[0], null, null);
                }

                else if (i == SIZE - 1 && j == SIZE - 1) {
                    b = new EmptyBlock(i, j, players[1], new TownHall(), null);
                } else if (i >= SIZE - 2 && j >= SIZE - 2) {
                    b = new EmptyBlock(i, j, players[1], null, null);
                }

                else if (i == 0 && j == SIZE - 1) {
                    b = new EmptyBlock(i, j, players[2], new TownHall(), null);
                } else if (i <= 1 && j >= SIZE - 2) {
                    b = new EmptyBlock(i, j, players[2], null, null);
                }

                else if (i == SIZE - 1 && j == 0) {
                    b = new EmptyBlock(i, j, players[3], new TownHall(), null);
                } else if (i >= SIZE - 2 && j <= 1) {
                    b = new EmptyBlock(i, j, players[3], null, null);
                }

                else {
                    if (Math.random() < 0.4) {
                        b = new ForestBlock(i, j, null, null, null);
                    } else {
                        b = new EmptyBlock(i, j, null, null, null);
                    }
                }

                int fx = i, fy = j;
                b.addActionListener(e -> handleClick(fx, fy));
                block[i][j] = b;
                gridPanel.add(b);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }
}
