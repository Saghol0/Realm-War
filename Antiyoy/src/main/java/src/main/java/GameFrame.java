package src.main.java;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    Image iconFrame = new ImageIcon(getClass().getResource("/Image/Icon.jpg")).getImage();

    public GameFrame() {
        setTitle("Antiyoy War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 830);
        setLocationRelativeTo(null);
        setIconImage(iconFrame);
        setLayout(new BorderLayout());

        // ایجاد GamePanel و HUDPanel
        GamePanel gamePanel = new GamePanel();
        HUDPanel hudPanel = gamePanel.getHudPanel(); // فرض بر این است HUDPanel در GamePanel هست

        add(gamePanel, BorderLayout.CENTER);
        add(hudPanel, BorderLayout.EAST);

        // ایجاد و اتصال GameController
        GameController controller = new GameController(gamePanel, hudPanel, gamePanel.getPlayers());
        gamePanel.setController(controller); // اضافه کردن کنترلر به پنل بازی برای مدیریت کلیک‌ها

        setVisible(true);
    }
}
