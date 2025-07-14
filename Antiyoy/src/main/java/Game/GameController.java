package Game;

import Blocks.Block;
import Structure.Structures;
import Structure.Tower;
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

        hudPanel.getButtonSELECTDataLest().addActionListener(e -> {
            GameData gameData= new GameData(hudPanel);
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
                moveFromBlock = block;
                hudPanel.addLog("📦 Selected unit block for move.");
            }
        } else {
            if (block != moveFromBlock) {
                Unit unit = moveFromBlock.getUnit();
                if(unit.canMove(moveFromBlock.getGridX(),moveFromBlock.getGridY(),block.getGridX(),block.getGridY())){
                    if (block.getUnit() != null) {
                        if (moveFromBlock.getUnit().getName().equals(block.getUnit().getName())) {
                            unitMerge(moveFromBlock, block);
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"You can't put it on that block because it's a unit.");
                        }
                    }

                    else {
                    moveUnit(moveFromBlock,block,unit);}
                }
                else { JOptionPane.showMessageDialog(null, "Sorry, the selected block is out of range."); }
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

    // بررسی می‌کند که آیا بلوک انتخاب شده در فاصله 1 بلوک از بلوک‌های متعلق به بازیکن است یا نه
    private boolean isAdjacentToOwnedBlock(Player player, Block block) {
        int x = block.getGridX();
        int y = block.getGridY();

        // پیمایش همسایه‌ها (8 جهت اطراف)
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;

                // بررسی محدوده گرید
                if (nx >= 0 && nx < gamePanel.SIZE && ny >= 0 && ny < gamePanel.SIZE) {
                    Block neighbor = gamePanel.getBlock(nx, ny);
                    if (neighbor.getOwner() == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canBuildStructure(Player player, Structures structure) {
        if (structure == null || selectedBlock == null) return false;
        if (selectedBlock.getStructure() != null || selectedBlock.getUnit() != null) {
            hudPanel.addLog("❌ The selected block is already occupied.");
            return false;
        }

        if (!isAdjacentToOwnedBlock(player, selectedBlock)) {
            hudPanel.addLog("❌ You can only build structures adjacent to your owned blocks.");
            return false;
        }

        if (isNearEnemyTower(selectedBlock, player)) {
            hudPanel.addLog("❌ Cannot build structures adjacent to enemy Tower.");
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

        if (!isAdjacentToOwnedBlock(player, selectedBlock)) {
            hudPanel.addLog("❌ You can only build units adjacent to your owned blocks.");
            return false;
        }

        if (isNearEnemyTower(selectedBlock, player)) {
            hudPanel.addLog("❌ Cannot build units adjacent to enemy Tower.");
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
        hudPanel.addLog( currentPlayer.getName() + " collected " + goldGain + " gold and " + foodGain + " food.");
    }
    private void activeMoveUnit(){
        Player currentPlayer = players[currentPlayerIndex];
        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                if (block.getOwner() == currentPlayer && block.getUnit() !=  null) {
                    block.getUnit().setMoved(false);
                }
            }
        }
    }

    public void endTurn() {
        collectResources();
        activeMoveUnit();
        currentPlayerIndex = 1 - currentPlayerIndex;
        hudPanel.addLog(" Turn ended. It's now " + players[currentPlayerIndex].getName() + "'s turn.");
        updateHUD();
    }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood());
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    private boolean isNearEnemyTower(Block block, Player currentPlayer) {
        int x = block.getGridX();
        int y = block.getGridY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < gamePanel.SIZE && ny >= 0 && ny < gamePanel.SIZE) {
                    Block neighbor = gamePanel.getBlock(nx, ny);
                    if (neighbor.getOwner() != currentPlayer &&
                            neighbor.getStructure() instanceof Tower) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void moveUnit(Block fromBlock, Block toBlock, Unit unit) {
        if (fromBlock == null || toBlock == null) {
            hudPanel.addLog("⚠ Please select both source and target blocks.");
            return;
        }
        if (fromBlock.getUnit() == null) {
            hudPanel.addLog("⚠️ No unit in selected block to move.");
            return;
        }
        if (toBlock.getStructure() instanceof Structure.Tower) {
            hudPanel.addLog("❌ You can only move to empty blocks or blocks with a Tower.");
            return;
        }
        if (fromBlock.getOwner() != getCurrentPlayer()) {
            hudPanel.addLog("❌ You can only move units from blocks you own.");
            return;
        }

        // اضافه کردن چک نزدیکی به تاور حریف برای یونیت های با رنک کمتر از 3
        if (unit.rank < 3 && isNearEnemyTower(toBlock, getCurrentPlayer())) {
            hudPanel.addLog("❌ Units with rank less than 3 cannot move adjacent to enemy Towers.");
            return;
        }

        Player previousOwner = toBlock.getOwner();

        toBlock.setUnit(unit);
        fromBlock.setUnit(null);

        if (toBlock.getOwner() != getCurrentPlayer()) {
            toBlock.setOwner(getCurrentPlayer());
        }

        if (!hasOwnedNeighborBlock(toBlock, getCurrentPlayer())) {
            fromBlock.setUnit(unit);
            toBlock.setUnit(null);
            toBlock.setOwner(previousOwner);  // Restore previous owner if move invalid
            hudPanel.addLog("❌ Invalid move: After moving, none of the adjacent blocks belong to you.");
            return;
        }

        hudPanel.addLog("✅ Unit moved successfully.");
        unit.setMoved(true);
    }

    private boolean hasOwnedNeighborBlock(Block block, Player player) {
        int x = block.getGridX();
        int y = block.getGridY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < gamePanel.SIZE && ny >= 0 && ny < gamePanel.SIZE) {
                    Block neighbor = gamePanel.getBlock(nx, ny);
                    if (neighbor.getOwner() == player) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void unitMerge(Block fromBlock, Block toBlock) {
        JLabel unitLabel = new JLabel();
        switch (toBlock.getUnit().getName()) {
            case "Peasant": {
                toBlock.setUnit(new Spearman(unitLabel));
                fromBlock.setUnit(null);
                hudPanel.addLog("✅ Unit merge successfully.");
                break;
            }
            case "Spearman": {
                toBlock.setUnit(new Swordman(unitLabel));
                fromBlock.setUnit(null);
                hudPanel.addLog("✅ Unit merge successfully.");
                break;
            }
            case "Swordman": {
                toBlock.setUnit(new Knight(unitLabel));
                fromBlock.setUnit(null);
                hudPanel.addLog("✅ Unit merge successfully.");
                break;
            }
            case "Knight": {
                JOptionPane.showMessageDialog(null, "Cannot merge because knight is the last level");
                break;
            }
            default:
                JOptionPane.showMessageDialog(null, "There is a problem with your code.");
        }
    }
    public void attackUnitToUnit(Block fromBlock, Block toBlock) {
        toBlock.getUnit().setHealth(toBlock.getUnit().getHealth()-fromBlock.getUnit().getAttackPower());
        if (toBlock.getUnit().getHealth() <= 0) {
            hudPanel.addLog("Unit "+toBlock.getOwner().getName() +" was killed.");
            toBlock.setUnit(null);
            toBlock.setOwner(getCurrentPlayer());
            toBlock.setUnit(fromBlock.getUnit());
            fromBlock.setUnit(null);
        }
        else {
            hudPanel.addLog("Unit "+toBlock.getOwner().getName() +" was attacked. \n"+"health:"+toBlock.getUnit().getHealth());
        }
    }
    public void attackUnitToStructure(Block fromBlock, Block toBlock) {
        toBlock.getStructure().setDurability(toBlock.getStructure().getDurability()-fromBlock.getUnit().getAttackPower());
        if (toBlock.getStructure().getDurability() <= 0) {
            hudPanel.addLog(toBlock.getStructure().getName()+" " + toBlock.getOwner().getName() +" destroyed.");
            toBlock.setStructure(null);
            toBlock.setOwner(getCurrentPlayer());
            toBlock.setStructure(fromBlock.getStructure());
            fromBlock.setStructure(null);
        }
        else {
            hudPanel.addLog("Structure"+toBlock.getOwner().getName()+" was attacked. \n"+"Durability:"+toBlock.getStructure().getDurability());
        }
    }

}