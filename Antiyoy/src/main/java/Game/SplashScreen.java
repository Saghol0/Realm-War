package Game;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public SplashScreen() {

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new BorderLayout());

        BackgroundPanel bgPanel = new BackgroundPanel("Image/kasra.jpg");
        bgPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Loading Game...", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bgPanel.add(title, BorderLayout.SOUTH);



        setContentPane(bgPanel);


        setSize(600, 400);
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
