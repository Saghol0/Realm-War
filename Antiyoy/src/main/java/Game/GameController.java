package Game;

import Blocks.Block;
import Structure.Structures;
import Units.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameController {
    private static final int MAX_UNIT_SPACE = 100;

    private Block selectedBlock = null;
    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;
    private final Player[] players;
    private int currentPlayerIndex = 0;
    private Unit selectedUnit = null;
    private Structures selectedStructures = null;
    private Block moveFromBlock = null;

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
            if (selectedBlock == null) {
                hudPanel.addLog("⚠️ Please select a block first.");
                return;
            }
            String unitName = (String) hudPanel.getUnitSelector().getSelectedItem();
            if (!"None".equals(unitName)) {
                selectedUnit = createUnitByName(unitName);
                if (canBuildUnit(getCurrentPlayer(), selectedUnit)) {
                    payForUnit(getCurrentPlayer(), selectedUnit);
                    selectedBlock.setUnit(selectedUnit);
                    if (selectedBlock.getOwner() != getCurrentPlayer()) {
                        selectedBlock.setOwner(getCurrentPlayer());
                    }
                    hudPanel.addLog("✅ Unit " + unitName + " has been successfully built.");
                } else {
                    hudPanel.addLog("❌ Not enough resources or requirements not met to build this unit.");
                }
            } else {
                hudPanel.addLog("⚠️ Please select a unit.");
            }
        });

        hudPanel.getBuildStructuresButton().addActionListener(e -> {
            if (selectedBlock == null) {
                hudPanel.addLog("⚠️ Please select a block first.");
                return;
            }
            String structureName = (String) hudPanel.getStructureSelector().getSelectedItem();
            if (!"None".equals(structureName)) {
                selectedStructures = createStructureByName(structureName);
                if (canBuildStructure(getCurrentPlayer(), selectedStructures)) {
                    payForStructure(getCurrentPlayer(), selectedStructures);
                    selectedBlock.setStructure(selectedStructures);
                    if (selectedBlock.getOwner() != getCurrentPlayer()) {
                        selectedBlock.setOwner(getCurrentPlayer());
                    }
                    hudPanel.addLog("✅ Structure " + structureName + " has been successfully built.");
                } else {
                    hudPanel.addLog("❌ Not enough resources or the selected block is already occupied.");
                }
            } else {
                hudPanel.addLog("⚠️ Please select a structure.");
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
        if (moveFromBlock == null) {
            if (block.getUnit() != null && block.getOwner() == getCurrentPlayer()) {
                moveFromBlock = block;
                hudPanel.addLog("📦 Selected unit block for move.");
            }
        } else {
            if (block != moveFromBlock) {
                Unit unit = moveFromBlock.getUnit();
                if(unit.canMove(moveFromBlock.getX(),moveFromBlock.getY(),block.getX(),block.getY())){
                    moveUnit(moveFromBlock, block,unit);
                }
                else { JOptionPane.showMessageDialog(gamePanel, "Sorry, the selected block is out of range."); }
            } else if (block == moveFromBlock) {
                selectedBlock.setBorder(new LineBorder(Color.BLACK, 1));
            }
            moveFromBlock = null; // Reset after move
        }

    }

    private Unit createUnitByName(String unitName) {
        JLabel unitLabel = new JLabel();
        switch (unitName) {
            case "Peasant          (Gold:10 + Food:5 + Space:1)":
                return new Peasant(unitLabel);
            case "Knight            (Gold:40 + Food:20 + Space:4)":
                return new Knight(unitLabel);
            case "SpearMan      (Gold:20 + Food:10 + Space:2)":
                return new Spearman(unitLabel);
            case "SwordMan     (Gold:30 + Food:15 + Space:3)":
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
        if (structure == null || selectedBlock == null) return false;
        if (selectedBlock.getStructure() != null || selectedBlock.getUnit() != null) {
            hudPanel.addLog("❌ The selected block is already occupied.");
            return false;
        }

        return player.getGold() >= structure.getBuildCost();
    }

    private boolean canBuildUnit(Player player, Unit unit) {
        if (unit == null || selectedBlock == null) return false;
        if (selectedBlock.getStructure() != null || selectedBlock.getUnit() != null) {
            hudPanel.addLog("❌ The selected block is already occupied.");
            return false;
        }

        // Check for barrack presence
        boolean hasBarrack = false;
        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                if (block.getOwner() == player && block.getStructure() instanceof Structure.Barrack) {
                    hasBarrack = true;
                    break;
                }
            }
        }

        if (!hasBarrack) {
            hudPanel.addLog("❌ You must have at least one Barrack to build units.");
            return false;
        }

        // Check resources and unit space
        if (player.getGold() < unit.costGold) {
            hudPanel.addLog("❌ Not enough gold to build this unit.");
            return false;
        }
        if (player.getFood() < unit.costFood) {
            hudPanel.addLog("❌ Not enough food to build this unit.");
            return false;
        }
        if (player.getUnitSpace() + unit.unitSpace > MAX_UNIT_SPACE) {
            hudPanel.addLog("❌ Not enough unit space available. Maximum allowed: " + MAX_UNIT_SPACE);
            return false;
        }

        return true;
    }

    private void payForStructure(Player player, Structures structure) {
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
        hudPanel.addLog("💰 " + currentPlayer.getName() + " collected " + goldGain + " gold and " + foodGain + " food.");
    }

    public void endTurn() {
        collectResources();
        currentPlayerIndex = 1 - currentPlayerIndex;
        hudPanel.addLog("🔄 Turn ended. It's now " + players[currentPlayerIndex].getName() + "'s turn.");
        updateHUD();
    }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood());
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void moveUnit(Block fromBlock, Block toBlock,Unit unit) {
        if (fromBlock == null || toBlock == null) {
            hudPanel.addLog("⚠ Please select both source and target blocks.");
            return;
        }
        if (fromBlock.getUnit() == null) {
            hudPanel.addLog("⚠️ No unit in selected block to move.");
            return;

        }
        Structures structure = toBlock.getStructure();
        if (toBlock.getStructure() instanceof Structure.Tower) {
            hudPanel.addLog("❌ You can only move to empty blocks or blocks with a Tower.");
            return;
        }
        if (fromBlock.getOwner() != getCurrentPlayer()) {
            hudPanel.addLog("❌ You can only move units from blocks you own.");
            return;
        }
        toBlock.setUnit(unit);
        fromBlock.setUnit(null);
        if (toBlock.getOwner() != getCurrentPlayer()) {
            toBlock.setOwner(getCurrentPlayer());
        }
        hudPanel.addLog("✅ Unit moved successfully.");
    }
}