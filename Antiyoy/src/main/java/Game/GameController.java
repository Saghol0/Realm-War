package Game;

import Blocks.Block;
import Structure.Structures;
import Units.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameController {
    private Block selectedBlock = null;
    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;
    private final Player[] players;
    private int currentPlayerIndex = 0;
    private Unit selectedUnit = null;
    private Structures selectedStructures = null;

    public GameController(GamePanel gamePanel, HUDPanel hudPanel, Player[] players) {
        this.gamePanel = gamePanel;
        this.hudPanel = hudPanel;
        this.players = players;

        setupListeners();
        updateHUD();
    }

    private void setupListeners() {
        hudPanel.getEndTurnButton().addActionListener(e -> endTurn());

        hudPanel.getBuildUnitButton().addActionListener(e -> {
            String unitName = (String) hudPanel.getUnitSelector().getSelectedItem();
            if (!"None".equals(unitName)) {
                selectedUnit = createUnitByName(unitName);
                if (canBuildUnit(getCurrentPlayer(), selectedUnit)) {
                    payForUnit(getCurrentPlayer(), selectedUnit);
                    selectedBlock.setUnit(selectedUnit);
                    hudPanel.addLog(unitName + " Added successfully.");
                } else {
                    hudPanel.addLog("you dont have enough money.");
                }
            } else {
                hudPanel.addLog("Please select your unit.");
            }
        });
        hudPanel.getBuildStructuresButton().addActionListener(e -> {
            String StructureName = (String) hudPanel.getStructureSelector().getSelectedItem();
            if (!"None".equals(StructureName)) {
                selectedStructures = createStructureByName(StructureName);
                if (canBuildStructure(getCurrentPlayer(), selectedStructures)) {
                    payForStructure(getCurrentPlayer(), selectedStructures);
                    selectedBlock.setStructure(selectedStructures);
                    hudPanel.addLog(StructureName + " Added successfully.");
                } else {
                    hudPanel.addLog("you dont have enough money.");
                }
            } else {
                hudPanel.addLog("Please select your structure.");
            }
        });
        hudPanel.getUnitSelector().addActionListener(e -> {
            String selectedItem = (String) hudPanel.getUnitSelector().getSelectedItem();
            if (!selectedItem.equals("None")) {
                hudPanel.getStructureSelector().setSelectedItem("None");
            }
        });

        hudPanel.getStructureSelector().addActionListener(e -> {
            String selectedItem = (String) hudPanel.getStructureSelector().getSelectedItem();
            if (!selectedItem.equals("None")) {
                hudPanel.getUnitSelector().setSelectedItem("None");
            }
        });
    }

    public void handleBlockClick(Block block) {
        if (selectedBlock != null) {
            selectedBlock.setBorder(new LineBorder(Color.BLACK, 1));
        }
        block.setBorder(new LineBorder(Color.BLACK, 5));
        selectedBlock = block;
    }


    private Unit createUnitByName(String unitName) {
        JLabel unitLabel = new JLabel();
        switch (unitName) {
            case "Peasant          (Gold:10 + Food:3 + Space:1)":
                return new Peasant(unitLabel);
            case "Knight            (Gold:40 + Food:6 + Space:4)":
                return new Knight(unitLabel);
            case "SpearMan      (Gold:20 + Food:4 + Space:2)":
                return new Spearman(unitLabel);
            case "SwordMan     (Gold:30 + Food:5 + Space:3)":
                return new Swordman(unitLabel);
            default:
                return null;
        }
    }

    private Structures createStructureByName(String structureName) {
        switch (structureName) {
            case "Farm":
                return new Structure.Farm();
            case "Market":
                return new Structure.Market();
            case "Tower":
                return new Structure.Tower();
            case "Barrack":
                return new Structure.Barrack();
            default:
                return null;
        }
    }

    private boolean canBuildStructure(Player player, Structures structure) {
        return player.getGold() >= structure.getBuildCost();
    }

    private boolean canBuildUnit(Player player, Unit unit) {
        return player.getGold() >= unit.costGold && player.getFood() >= unit.costFood && player.getUnitSpace() + unit.unitSpace <= 100;
    }

    private void payForStructure(Player player, Structures structure) {//هزینه ساخت ساختمون
        player.addGold(-structure.getBuildCost());
        updateHUD();
    }

    private void payForUnit(Player player, Unit unit) {
        player.addGold(-unit.costGold);
        player.addFood(-unit.costFood);
        player.addUnitSpace(unit.unitSpace);
        updateHUD();
    }

    private void collectResources() {
        Player currentPlayer = players[currentPlayerIndex];
        int goldGain = 0;
        int foodGain = 0;

        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                if (block.getOwner() == currentPlayer && block.getStructure() != null) {
                    Structures s = block.getStructure();
                    if (s instanceof Structure.Farm) {
                        foodGain += ((Structure.Farm) s).getFoodProduction();
                    } else if (s instanceof Structure.Market) {
                        goldGain += ((Structure.Market) s).getGoldProduction();
                    } else if (s instanceof Structure.TownHall) {
                        goldGain += ((Structure.TownHall) s).getGoldProduction();
                        foodGain += ((Structure.TownHall) s).getFoodProduction();
                    }
                }
            }
        }

        currentPlayer.addGold(goldGain);
        currentPlayer.addFood(foodGain);
        hudPanel.addLog(currentPlayer.getName() + " collected " + goldGain + " gold and " + foodGain + " food.");
    }

    public void endTurn() {
        collectResources();
        currentPlayerIndex = 1 - currentPlayerIndex;
        hudPanel.addLog("Turn ended. Now it's " + players[currentPlayerIndex].getName() + "'s turn.");
        updateHUD();
    }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood());
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }
}
