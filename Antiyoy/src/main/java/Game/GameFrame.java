package Game;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    Image iconFrame = new ImageIcon(getClass().getResource("/Image/Icon.jpg")).getImage();
    GamePanel gamePanel;

    public GameFrame(Player[] players, int SIZE) {
        setTitle("Antiyoy War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 830);
        setLocationRelativeTo(null);
        setIconImage(iconFrame);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // ایجاد GamePanel و HUDPanel
        gamePanel = new GamePanel(players,SIZE);
        HUDPanel hudPanel = gamePanel.getHudPanel();
        hudPanel.getExitButton().addActionListener(e -> {
            int confirm= JOptionPane.showConfirmDialog(null , "Do you want to exit the game?","Exit Game",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(confirm==JOptionPane.YES_OPTION){
                new Menu();
                dispose();
            }
        });

        add(gamePanel, BorderLayout.CENTER);
        add(hudPanel, BorderLayout.EAST);

        GameController controller = new GameController(gamePanel, hudPanel, players);
        gamePanel.setController(controller);

        setVisible(true);
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }

}
