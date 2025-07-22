package Game;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen() {

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BorderLayout());

        BackgroundPanel bgPanel = new BackgroundPanel("Image/loading.png");
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);
        setSize(500, 500);
        setLocationRelativeTo(null);
    }
    public void showSplash(int duration) {
        setVisible(true);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
    }
}
