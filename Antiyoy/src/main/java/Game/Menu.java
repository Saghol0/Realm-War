package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
    public Menu() {
        setTitle("Realm War");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new BackgroundPanel());


        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(150, 0, 150, 0));


        JLabel titleLabel = new JLabel("Welcome to Realm War");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 50));
        titleLabel.setForeground(Color.white);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        menuPanel.add(titleLabel);

        JButton newGameBtn = createFancyButton("New Game");
        JButton loadGameBtn = createFancyButton("Load Game");
        JButton exitBtn = createFancyButton("Exit");
        newGameBtn.addActionListener(e -> {
            new GameFrame();
            dispose();
        });
        loadGameBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Load Game Clicked");
        });
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });
        menuPanel.add(newGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(loadGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(exitBtn);

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(menuPanel);
        setVisible(true);
    }

    private JButton createFancyButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 30, 30));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        button.setMaximumSize(new Dimension(300, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);


        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
                button.setForeground(Color.YELLOW);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 30, 30));
                button.setForeground(Color.WHITE);
                button.setCursor(Cursor.getDefaultCursor());
            }

        });
        return button;
    }
}