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
                    hudPanel.addLog("✅ Unit " + unitName + " has been built.");
                    updateHUD();
                    gamePanel.repaint();
                } else {
                    hudPanel.addLog("❌ Not enough resources or conditions to build the unit.");
                }
            } else {
                hudPanel.addLog("⚠️ Please select a unit type.");
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
                    hudPanel.addLog("✅ Structure " + structureName + " has been built.");
                    updateHUD();
                    gamePanel.repaint();
                } else {
                    hudPanel.addLog("❌ Not enough resources or the selected block is occupied.");
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

        hudPanel.getButtonSELECTDataLest().addActionListener(e -> {
            GameData gameData = new GameData(hudPanel);
            gameData.SELECTable();
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
                if (block.getUnit().hasMoved) {
                    hudPanel.addLog("⚠️ This unit has already moved this turn.");
                } else {
                    moveFromBlock = block;
                    hudPanel.addLog("📦 Unit block for movement selected.");
                }
            }
        } else {
            if (block != moveFromBlock) {
                Unit unit = moveFromBlock.getUnit();
                if (unit.canMove(moveFromBlock.getX(), moveFromBlock.getY(), block.getX(), block.getY())) {
                    if (block.getUnit() == null && !(block.getStructure() instanceof Structure.Tower)) {
                        moveUnit(moveFromBlock, block, unit);
                    } else {
                        hudPanel.addLog("❌ Destination block is occupied or not movable.");
                    }
                } else {
                    JOptionPane.showMessageDialog(gamePanel, "Destination block is out of movement range.");
                }
            }
            moveFromBlock.setBorder(new LineBorder(Color.BLACK, 1));
            moveFromBlock = null;
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

    private boolean isAdjacentToOwnedBlock(Player player, Block target) {
        int x = target.getX();
        int y = target.getY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && ny >= 0 && nx < gamePanel.SIZE && ny < gamePanel.SIZE) {
                    Block neighbor = gamePanel.getBlock(nx, ny);
                    if (neighbor.getOwner() == player) return true;
                }
            }
        }
        return false;
    }

    private boolean canBuildStructure(Player player, Structures structure) {
        if (structure == null || selectedBlock == null) return false;
        if (selectedBlock.getStructure() != null || selectedBlock.getUnit() != null) {
            hudPanel.addLog("❌ Selected block is occupied.");
            return false;
        }

        if (selectedBlock.getOwner() != player && !isAdjacentToOwnedBlock(player, selectedBlock)) {
            hudPanel.addLog("❌ You can only build next to blocks you own.");
            return false;
        }

        if (player.getGold() < structure.getBuildCost()) {
            hudPanel.addLog("❌ Not enough gold to build the structure.");
            return false;
        }

        return true;
    }

    private boolean canBuildUnit(Player player, Unit unit) {
        if (unit == null || selectedBlock == null) return false;
        if (selectedBlock.getStructure() != null || selectedBlock.getUnit() != null) {
            hudPanel.addLog("❌ Selected block is occupied.");
            return false;
        }

        if (selectedBlock.getOwner() != player && !isAdjacentToOwnedBlock(player, selectedBlock)) {
            hudPanel.addLog("❌ You can only build units next to blocks you own.");
            return false;
        }

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

        if (player.getGold() < unit.costGold) {
            hudPanel.addLog("❌ Not enough gold to build the unit.");
            return false;
        }
        if (player.getFood() < unit.costFood) {
            hudPanel.addLog("❌ Not enough food to build the unit.");
            return false;
        }
        if (player.getUnitSpace() + unit.unitSpace > MAX_UNIT_SPACE) {
            hudPanel.addLog("❌ Not enough space for the unit. Max space: " + MAX_UNIT_SPACE);
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

        Player currentPlayer = players[currentPlayerIndex];
        // Reset units' movement
        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                Unit unit = block.getUnit();
                if (block.getOwner() == currentPlayer && unit != null) {
                    unit.resetMovement();
                }
            }
        }

        currentPlayerIndex = 1 - currentPlayerIndex;
        hudPanel.addLog("🔄 Turn switched to " + players[currentPlayerIndex].getName() + ".");
        updateHUD();
        gamePanel.repaint();
    }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood());
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void moveUnit(Block fromBlock, Block toBlock, Unit unit) {
        if (fromBlock == null || toBlock == null) {
            hudPanel.addLog("⚠️ Please select both source and destination blocks.");
            return;
        }
        if (fromBlock.getUnit() == null) {
            hudPanel.addLog("⚠️ There is no unit in the source block.");
            return;
        }
        if (fromBlock.getOwner() != getCurrentPlayer()) {
            hudPanel.addLog("❌ You can only move units from blocks you own.");
            return;
        }
        if (unit.hasMoved) {
            hudPanel.addLog("❌ This unit has already moved this turn.");
            return;
        }
        // Destination block must be empty or only have Tower structure (if desired behavior)
        if (toBlock.getUnit() != null) {
            hudPanel.addLog("❌ Destination block is occupied.");
            return;
        }
        if (toBlock.getStructure() != null && !(toBlock.getStructure() instanceof Structure.Tower)) {
            hudPanel.addLog("❌ You cannot move to blocks with structures other than Tower.");
            return;
        }

        toBlock.setUnit(unit);
        fromBlock.setUnit(null);
        unit.hasMoved = true;

        if (toBlock.getOwner() != getCurrentPlayer()) {
            toBlock.setOwner(getCurrentPlayer());
        }

        hudPanel.addLog("✅ Unit moved successfully.");
        gamePanel.repaint();
    }
}
