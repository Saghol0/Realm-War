package Game;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    protected String title;

    public SplashScreen(String title) {
        this.title = title;
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BorderLayout());

        BackgroundPanel bgPanel = new BackgroundPanel(getTitle());
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
    public String getTitle() {
        return title;
    }
}
