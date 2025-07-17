package Game;


import Blocks.Block;
import Blocks.EmptyBlock;
import Blocks.ForestBlock;
import Structure.TownHall;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public final int SIZE = 10;
    private Block[][] block;
    private  Player[] players;
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
                    b = new EmptyBlock(i, j, players[0], new TownHall(),null);
                } else if (i == SIZE - 1 && j == SIZE - 1) {
                    b = new EmptyBlock(i, j, players[1], new TownHall(),null);
                }
                else if ((0<=i &&1>=i) && (0<=j && 1>=j) ) {
                    b = new EmptyBlock(i, j, players[0], null,null);
                } else if ((SIZE-2<=i && SIZE-1>=i)&& (SIZE-2<=j && SIZE-1>=j)) {
                    {
                        b = new EmptyBlock(i, j, players[1], null,null);
                    }
                } else {
                    if (Math.random() < 0.4) {
                        b = new ForestBlock(i, j, null, null,null);
                    } else {
                        b = new EmptyBlock(i, j, null, null,null);
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

    public Block[][] getBlocks(){
        return block;
    }
    public int getSIZE(){
        return SIZE;
    }

    public void setHudPanel(HUDPanel hudPanel){
        this.hudPanel=hudPanel;
    }

    public void loadGame(Block[][] blocks){
        this.block = blocks;
        this.players= new Player[] {
                blocks[0][0].getOwner(),
                blocks[9][9].getOwner()
        };
        removeAll();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Block b = block[i][j];
                if(b.getOwner()!=null){
                    if(b.getOwner().getName().equals( blocks[0][0].getOwner().getName())){
                        b.setOwner(blocks[0][0].getOwner());
                    }
                    if(b.getOwner().getName().equals( blocks[9][9].getOwner().getName())){
                        b.setOwner(blocks[9][9].getOwner());
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

    public GameController getController(){
        return controller;
    }


}
