package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame {
    private GameFrame gameFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public Menu() {
        setTitle("Realm War");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        mainPanel.add(menuPanel(), "menuPanel");
        mainPanel.add(gameSelectorPanel(), "gameSelectorPanel");
        mainPanel.add(createGame(2), "2player");
        mainPanel.add(createGame(4), "4player");

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(mainPanel, BorderLayout.CENTER);

        setContentPane(bgPanel);
        cardLayout.show(mainPanel, "menuPanel");

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

    public JPanel menuPanel() {
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
        JButton matchHistory = createFancyButton("Match History");
        JButton exitBtn = createFancyButton("Exit");

        newGameBtn.addActionListener(e -> cardLayout.show(mainPanel, "gameSelectorPanel"));
        loadGameBtn.addActionListener(e -> {
        GameFrame gameFrame = new GameFrame(new Player[]{new Player("null",Color.BLACK)},10);
        gameFrame.getGamePanel().getController().loadGameForMenu();
        dispose();
        });
        matchHistory.addActionListener(e -> new GameData(new HUDPanel()).SELECTable());
        exitBtn.addActionListener(e -> System.exit(0));

        menuPanel.add(newGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(loadGameBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(matchHistory);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(exitBtn);

        return menuPanel;
    }

    public JPanel gameSelectorPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(150, 0, 150, 0));

        JLabel label = new JLabel("Choose your game type");
        label.setFont(new Font("Georgia", Font.BOLD, 24));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn2 = createFancyButton("2 Player");
        JButton btn4 = createFancyButton("4 Player");
        JButton back = createFancyButton("Back");

        btn2.addActionListener(e -> cardLayout.show(mainPanel, "2player"));
        btn4.addActionListener(e -> cardLayout.show(mainPanel, "4player"));
        back.addActionListener(e -> cardLayout.show(mainPanel, "menuPanel"));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btn2);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btn4);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(back);

        return panel;
    }
    public JPanel createGame(int numberPlayer) {
        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Please choose your name:");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0)); // فاصله بالا پایین
        gamePanel.add(titleLabel);

        JPanel playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField[] fields = new JTextField[numberPlayer];

        for (int i = 0; i < numberPlayer; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setOpaque(false);
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            rowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel l = new JLabel("Player " + (i + 1) + ": ");
            l.setFont(new Font("Georgia", Font.PLAIN, 18));
            l.setForeground(Color.WHITE);

            JTextField f = new JTextField();
            f.setMaximumSize(new Dimension(200, 30));
            f.setPreferredSize(new Dimension(200, 30));
            f.setAlignmentX(Component.LEFT_ALIGNMENT);

            rowPanel.add(Box.createHorizontalStrut(10));
            rowPanel.add(l);
            rowPanel.add(Box.createHorizontalStrut(10));
            rowPanel.add(f);
            rowPanel.add(Box.createHorizontalStrut(10));

            playerPanel.add(rowPanel);
            playerPanel.add(Box.createVerticalStrut(15));

            fields[i] = f;
        }
        JPanel sizesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sizesPanel.setOpaque(false);

        JLabel sizesLabel = new JLabel("Sizes:");
        sizesLabel.setFont(new Font("Georgia", Font.PLAIN, 18));
        sizesLabel.setForeground(Color.WHITE);

        JComboBox<Integer> unitSelector = new JComboBox<>(new Integer[]{10,20});
        unitSelector.setSelectedIndex(0);

        sizesPanel.add(sizesLabel);
        sizesPanel.add(unitSelector);
        playerPanel.add(sizesPanel);
        gamePanel.add(playerPanel);

        JButton startBtn = createFancyButton("Start Game");

        startBtn.addActionListener(e -> {
            for (JTextField f : fields) {
                if (f.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
            }
            Player[] players = new Player[numberPlayer];
            Color[] colors = {Color.RED, Color.BLUE, Color.CYAN, Color.PINK};
            for (int i = 0; i < numberPlayer; i++) {
                players[i]=new Player(fields[i].getText(), colors[i]);
            }
            int SIZE = (int) unitSelector.getSelectedItem();
            gameFrame = new GameFrame(players,SIZE);
            dispose();
        });

        JButton backBtn = createFancyButton("Back");
        backBtn.setMaximumSize(new Dimension(150, 40));
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.setMargin(new Insets(10, 20, 10, 20));
        backBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "gameSelectorPanel");
        });



        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(startBtn);
        gamePanel.add(Box.createVerticalStrut(30));
        gamePanel.add(backBtn);
        gamePanel.add(Box.createVerticalStrut(30));

        return gamePanel;
    }

}
