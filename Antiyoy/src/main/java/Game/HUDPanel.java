package Game;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    private final JLabel playerLabel;
    private final JLabel goldLabel;
    private final JLabel foodLabel;
    private final JLabel unitSpaceLabel;
    private final JLabel TimerTurnEnd;
    private final JLabel TimerForget;
    private final JTextArea logArea;
    private final JComboBox<String> unitSelector;
    private final JComboBox<String> structureSelector;
    private final JButton endTurnButton;
    private final JButton buildStructuresButton;
    private final JButton buildUnitButton;
    private final JButton buttonSELECTDataLest;
    private final JButton buttonSaveGame;
    private final JButton buttonLoadGame;
    private final JButton levelUpButton;  // دکمه جدید لول آپ
    private final JButton exitButton;



    public HUDPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 640));
        setBackground(new Color(220, 220, 220));

        JPanel infoPanel = new JPanel(new GridLayout(17, 1));  // افزایش تعداد ردیف‌ها

        TimerTurnEnd = new JLabel("Your Turn : 30");
        TimerForget = new JLabel("Getting resources up : 15");
        playerLabel = new JLabel("Player:");
        goldLabel = new JLabel("Gold:");
        foodLabel = new JLabel("Food:");
        unitSpaceLabel = new JLabel("Unit Space:");

        TimerTurnEnd.setFont(new Font("Arial", Font.PLAIN, 24));

        infoPanel.add(TimerTurnEnd);
        infoPanel.add(TimerForget);
        infoPanel.add(playerLabel);
        infoPanel.add(goldLabel);
        infoPanel.add(foodLabel);
        infoPanel.add(unitSpaceLabel);

        unitSelector = new JComboBox<>(new String[]{
                "None",
                "Peasant          (Gold:10 + Food:5 + Space:1)",
                "SpearMan      (Gold:20 + Food:10 + Space:2)",
                "SwordMan     (Gold:30 + Food:15 + Space:3)",
                "Knight            (Gold:40 + Food:20 + Space:4)"
        });

        structureSelector = new JComboBox<>(new String[]{
                "None", "Farm", "Market", "Barrack", "Tower"
        });

        endTurnButton = new JButton("End Turn");
        buildStructuresButton = new JButton("Build Structures");
        buildUnitButton = new JButton("Build Units");
        levelUpButton = new JButton("Level Up");  // مقداردهی دکمه لول آپ

        buttonSELECTDataLest = new JButton("View All Machs");
        buttonSaveGame = new JButton("Save Game");
        buttonLoadGame = new JButton("Load Game");
        exitButton = new JButton("Exit");

        // افزودن بخش انتخاب یونیت
        infoPanel.add(new JLabel("Select Unit:"));
        infoPanel.add(unitSelector);
        infoPanel.add(buildUnitButton);

        // افزودن بخش انتخاب ساختار
        infoPanel.add(new JLabel("Select Structure:"));
        infoPanel.add(structureSelector);
        infoPanel.add(buildStructuresButton);

        // افزودن دکمه لول آپ
        infoPanel.add(levelUpButton);

        // سایر دکمه‌ها
        infoPanel.add(buttonSELECTDataLest);

        JPanel panelSL = new JPanel();
        panelSL.add(buttonSaveGame);
        panelSL.add(buttonLoadGame);

        infoPanel.add(panelSL);
        infoPanel.add(exitButton);
        logArea = new JTextArea(10, 20);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);

        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(endTurnButton, BorderLayout.SOUTH);
    }

    public void updatePlayerInfo(String playerName, int gold, int food, int usedUnitSpace, int maxUnitSpace) {
        playerLabel.setText("Player: " + playerName);
        goldLabel.setText("Gold: " + gold);
        foodLabel.setText("Food: " + food);
        unitSpaceLabel.setText("Unit Space: " + usedUnitSpace + " / " + maxUnitSpace);

    }

    public void disableInteractionAfterGameEnd() {
        endTurnButton.setEnabled(false);
        buildUnitButton.setEnabled(false);
        buildStructuresButton.setEnabled(false);
        unitSelector.setEnabled(false);
        structureSelector.setEnabled(false);
        levelUpButton.setEnabled(false); 
    }

    public void addLog(String message) {
        logArea.append(message + "\n");
    }


    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public JComboBox<String> getUnitSelector() {
        return unitSelector;
    }

    public JComboBox<String> getStructureSelector() {
        return structureSelector;
    }

    public JButton getBuildStructuresButton() {
        return buildStructuresButton;
    }

    public JButton getBuildUnitButton() {
        return buildUnitButton;
    }

    public JButton getButtonSELECTDataLest() {
        return buttonSELECTDataLest;
    }

    public JButton getButtonSaveGame() {
        return buttonSaveGame;
    }

    public JButton getButtonLoadGame() {
        return buttonLoadGame;
    }

    public JLabel getTimerTurnEnd() {
        return TimerTurnEnd;
    }

    public JLabel getTimerForget() {
        return TimerForget;
    }

    public JButton getLevelUpButton() {
        return levelUpButton;
    }
    public JButton getExitButton() {
        return exitButton;
    }
}
