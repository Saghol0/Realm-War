package src.main.java;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    private final JLabel playerLabel;
    private final JLabel goldLabel;
    private final JLabel foodLabel;
    private final JTextArea logArea;
    private final JComboBox<String> unitSelector;
    private final JComboBox<String> structureSelector;
    private final JButton endTurnButton;


    public HUDPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 640));
        setBackground(new Color(220, 220, 220));
        JPanel infoPanel = new JPanel(new GridLayout(7, 1));
        playerLabel = new JLabel("Player:");
        goldLabel = new JLabel("Gold:");
        foodLabel = new JLabel("Food:");
        infoPanel.add(playerLabel);
        infoPanel.add(goldLabel);
        infoPanel.add(foodLabel);
        unitSelector = new JComboBox<>(new String[]{"None", "Peasant", "Knight", "SpearMan", "SwordMan"});
        structureSelector = new JComboBox<>(new String[]{"None", "Farm", "Market", "Tower", "Barrack"});
        unitSelector.addActionListener(e -> {
            String selectedItem = (String) unitSelector.getSelectedItem();
            if (!selectedItem.equals("None")) {
                structureSelector.setSelectedItem("None");
            }
        });
        structureSelector.addActionListener(e -> {
            String selectedItem = (String) structureSelector.getSelectedItem();
            if (!selectedItem.equals("None")) {
                unitSelector.setSelectedItem("None");
            }
        });
        infoPanel.add(new JLabel("Select Unit:"));
        infoPanel.add(unitSelector);
        infoPanel.add(new JLabel("Select Structure:"));
        infoPanel.add(structureSelector);
        endTurnButton = new JButton("End Turn");
        logArea = new JTextArea(10, 20);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(infoPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(endTurnButton, BorderLayout.SOUTH);

    }
    public void updatePlayerInfo(String playerName, int gold, int food) {
        playerLabel.setText("Player: " + playerName);
        goldLabel.setText("Gold: " + gold);
        foodLabel.setText("Food: " + food);
    }
    public void addLog(String message) {
        logArea.append(message + "\n");
    }


}
