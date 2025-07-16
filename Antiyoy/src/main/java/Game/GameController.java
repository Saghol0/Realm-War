package Game;

import Blocks.Block;
import Structure.Structures;
import Structure.Tower;
import Units.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Timer;

public class GameController {

    private static final int MAX_UNIT_SPACE = 100;
    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;
    private  Player[] players;
    private Block selectedBlock = null;
    private int currentPlayerIndex = 0;
    private Unit selectedUnit = null;
    private Structures selectedStructures = null;
    private Block moveFromBlock = null;
    private boolean gameEnded = false;
    private  javax.swing.Timer timer;
    private int TimeForTurn=30;
    private int TimeForGetGoldAndFoolPlayer=3;


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

    public GameController(GamePanel gamePanel, HUDPanel hudPanel, Player[] players) {
        this.gamePanel = gamePanel;
        this.hudPanel = hudPanel;
        this.players = players;
        setupListeners();
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
    private void payUnitMaintenanceCost(Player player) {
        int totalGoldCost = 0;
        int totalFoodCost = 0;

        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                Unit unit = block.getUnit();
                if (unit != null && block.getOwner() == player) {
                    totalGoldCost += unit.costGold;
                    totalFoodCost += unit.costFood;
                }
            }
        }

        player.addGold(-totalGoldCost);
        player.addFood(-totalFoodCost);

        hudPanel.addLog(player.getName() + " paid " + totalGoldCost + " gold and " + totalFoodCost + " food for unit maintenance.");
    }

    private void checkAndRemoveUnitsIfResourcesNegative(Player player) {
        if (player.getGold() >= 0 && player.getFood() >= 0) {
            return;
        }

        int recoveredGold = 0;
        int recoveredFood = 0;
        int recoveredUnitSpace = 0;

        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                if (block.getOwner() == player) {
                    Unit unit = block.getUnit();
                    if (unit != null) {
                        recoveredGold += unit.costGold;
                        recoveredFood += unit.costFood;
                        recoveredUnitSpace += unit.unitSpace;

                        block.setUnit(null);
                    }
                }
            }
        }

        player.addGold(recoveredGold);
        player.addFood(recoveredFood);
        player.addUnitSpace(-recoveredUnitSpace);

        hudPanel.addLog("❌ Resources went negative! All units were removed and their resources were refunded.");
        updateHUD();
    }


    public void endTurn() {
            payUnitMaintenanceCost(getCurrentPlayer());
            checkAndRemoveUnitsIfResourcesNegative(getCurrentPlayer());
            activeMoveUnit(); // ریست فلگ moved یونیت‌ها

            moveFromBlock = null;
            if (selectedBlock != null) {
                selectedBlock.setBorder(new LineBorder(Color.BLACK, 1));
                selectedBlock = null;
            }

            currentPlayerIndex = 1 - currentPlayerIndex;
            hudPanel.addLog("Turn ended. It's now " + players[currentPlayerIndex].getName() + "'s turn.");
            updateHUD();
        }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood());
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public Player getOpponentPlayer() {
        return players[(currentPlayerIndex + 1) % players.length];
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
        if (gameEnded) {
            hudPanel.addLog("⚠️ Game has ended. No more moves allowed.");
            return;
        }
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
        if (gameEnded) {
            hudPanel.addLog("⚠️ Game has ended. No more merges allowed.");
            return;
        }
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

    private void activeMoveUnit() {
        Player currentPlayer = players[currentPlayerIndex];
        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                if (block.getOwner() == currentPlayer && block.getUnit() != null) {
                    block.getUnit().setMoved(false);
                }
            }
        }
    }

    public void attackUnitToUnit(Block fromBlock, Block toBlock) {
        if (gameEnded) {
            hudPanel.addLog("⚠️ Game has ended. No more attacks allowed.");
            return;
        }
        Unit attacker = fromBlock.getUnit();
        Unit defender = toBlock.getUnit();

        defender.setHealth(defender.getHealth() - attacker.getAttackPower());
        if (defender.getHealth() <= 0) {
            hudPanel.addLog("Unit " + toBlock.getOwner().getName() + " was killed.");
            toBlock.setUnit(attacker);
            toBlock.setOwner(getCurrentPlayer());
            fromBlock.setUnit(null);
            attacker.setMoved(true);
        } else {
            hudPanel.addLog("Unit " + toBlock.getOwner().getName() + " was attacked. \n" + "Health: " + defender.getHealth());
        }
    }

    public void attackUnitToStructure(Block fromBlock, Block toBlock) {
        if (gameEnded) {
            hudPanel.addLog("⚠️ Game has ended. No more attacks allowed.");
            return;
        }
        toBlock.getStructure().setDurability(toBlock.getStructure().getDurability() - fromBlock.getUnit().getAttackPower());
        if (toBlock.getStructure().getDurability() <= 0) {
            hudPanel.addLog(toBlock.getStructure().getName() + " " + toBlock.getOwner().getName() + " destroyed.");
            toBlock.setStructure(null);
            toBlock.setOwner(getCurrentPlayer());
            toBlock.setStructure(fromBlock.getStructure());
            fromBlock.setStructure(null);
            fromBlock.getUnit().setMoved(true);
            checkIfGameEnded();
        } else {
            hudPanel.addLog("Structure" + toBlock.getOwner().getName() + " was attacked. \n" + "Durability:" + toBlock.getStructure().getDurability());
        }
    }

    public boolean checkIfGameEnded() {
        Player currentPlayer = getCurrentPlayer();
        Player opponentPlayer = getOpponentPlayer();

        for (int i = 0; i < gamePanel.SIZE; i++) {
            for (int j = 0; j < gamePanel.SIZE; j++) {
                Block block = gamePanel.getBlock(i, j);
                Structures structure = block.getStructure();
                if (structure != null && structure.getName().equals("Town Hall")) {
                    if (block.getOwner() != opponentPlayer) {
                        hudPanel.addLog("🎉 " + currentPlayer.getName() + " won the game! Enemy Town Hall has been captured.");
                        JOptionPane.showMessageDialog(null, currentPlayer.getName() + " won the game!");
                        gameEnded = true;
                        hudPanel.disableInteractionAfterGameEnd();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void refreshBlockListeners() {
        for (int i = 0; i < gamePanel.getSIZE(); i++) {
            for (int j = 0; j < gamePanel.getSIZE(); j++) {
                Block b = gamePanel.getBlock(i, j);
                int finalI = i, finalJ = j;
                b.removeActionListener(b.getActionListeners()[0]);
                b.addActionListener(e -> handleBlockClick(gamePanel.getBlock(finalI, finalJ)));
            }
        }
    }

    private void setupListeners() {
        hudPanel.getEndTurnButton().addActionListener(e -> {
            TimeForGetGoldAndFoolPlayer=3;
            TimeForTurn=30;
            hudPanel.getTimerTurnEnd().setForeground(new Color(190, 190, 190));
            hudPanel.getTimerTurnEnd().setText("Your Turn : "+ TimeForTurn);

            endTurn();
        });

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
            GameData gameData = new GameData(hudPanel);
            gameData.SELECTable();
        });

        hudPanel.getButtonSaveGame().addActionListener(e -> {
            GameSandL gameSandL=new GameSandL(hudPanel);
            gameSandL.DropTable();
            gameSandL.SaveGame(gamePanel.getBlocks(),gamePanel.getSIZE(),currentPlayerIndex);
        });

        hudPanel.getButtonLoadGame().addActionListener(e -> {
            GameSandL gameSandL=new GameSandL(hudPanel);
            Block[] [] blocks=gameSandL.LoadGame(gamePanel.getBlocks());
            gamePanel.loadGame(blocks);
            gamePanel.setHudPanel(hudPanel);
            gamePanel.setController(this);
             this.players= new Player[] {
                    blocks[0][0].getOwner(),
                    blocks[9][9].getOwner()
            };
            this.refreshBlockListeners();
            if(gameSandL.getNoBat()>0){
                currentPlayerIndex=1;
            }else currentPlayerIndex=0;
            updateHUD();
            TimeForGetGoldAndFoolPlayer=3;
            TimeForTurn=30;
        });
        timer = new javax.swing.Timer(1000,_ -> {
            if(TimeForTurn<10){
                hudPanel.getTimerTurnEnd().setForeground(Color.RED);
            }
            hudPanel.getTimerTurnEnd().setText("Your Turn : "+ TimeForTurn--);

            hudPanel.getTimerForget().setText("Getting resources up : "+ TimeForGetGoldAndFoolPlayer--);
            if(TimeForGetGoldAndFoolPlayer<0){
                collectResources();
                TimeForGetGoldAndFoolPlayer=3;
            }

            if (TimeForTurn<0){
                TimeForGetGoldAndFoolPlayer=3;
                endTurn();
                TimeForTurn=30;
                hudPanel.getTimerTurnEnd().setText("Your Turn : "+ TimeForTurn);
                hudPanel.getTimerTurnEnd().setForeground(new Color(190, 190, 190));
            }
        });
        timer.start();





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
            hudPanel.addLog("📦 Selected unit block for movement, merging, and attack");
        }
    } else {
        if (block != moveFromBlock) {
            Unit unit = moveFromBlock.getUnit();
            if (unit != null && unit.canMove(moveFromBlock.getGridX(), moveFromBlock.getGridY(), block.getGridX(), block.getGridY())) {
                if (block.getOwner() == getCurrentPlayer() || block.getOwner() == null) {
                    if (block.getUnit() != null && block.getOwner() == getCurrentPlayer()) {
                        if (moveFromBlock.getUnit().getName().equals(block.getUnit().getName())) {
                            unitMerge(moveFromBlock, block);
                            moveFromBlock = null;
                        } else {
                            JOptionPane.showMessageDialog(null, "You can't put it on that block because it's a unit.");
                            // moveFromBlock = null; // اختیاریه: بسته به UX
                        }
                    } else if (block.getUnit() == null) {
                        moveUnit(moveFromBlock, block, unit);
                        moveFromBlock = null;
                    }
                } else {
                    if (block.getUnit() != null) {
                        attackUnitToUnit(moveFromBlock, block);
                        moveFromBlock = null;
                    } else if (block.getStructure() != null) {
                        attackUnitToStructure(moveFromBlock, block);
                        moveFromBlock = null;
                    } else {
                        JOptionPane.showMessageDialog(null, "Error");
                        // moveFromBlock = null; // اختیاریه
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, the selected block is out of range.");
                // moveFromBlock = null; // اختیاریه
            }
        } else {
            selectedBlock.setBorder(new LineBorder(Color.BLACK, 1));
            moveFromBlock = null;
        }
    }
}
}
