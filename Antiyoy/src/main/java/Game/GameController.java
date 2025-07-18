package Game;

import Blocks.Block;
import Structure.*;
import Units.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private static final int MAX_UNIT_SPACE = 100;
    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;
    private Player[] players;
    private Block selectedBlock = null;
    private int currentPlayerIndex = 0;
    private Unit selectedUnit = null;
    private Structures selectedStructures = null;
    private Block moveFromBlock = null;
    private boolean gameEnded = false;
    private javax.swing.Timer timer;
    private int TimeForTurn = 30;
    private int TimeForGetGoldAndFoolPlayer = 10;
    private int TimeEndGame=0;
    private GameSandL gameSandL;
    private GameData gameData;
    private int end;


    public GameController(GamePanel gamePanel, HUDPanel hudPanel, Player[] players) {
        this.gamePanel = gamePanel;
        this.hudPanel = hudPanel;
        this.players = players;
        this.gameSandL=new GameSandL(hudPanel);
        this.gameData=new GameData(hudPanel);
        setupListeners();
        updateHUD();
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
                return new Farm();
            case "Market":
                return new Market();
            case "Tower":
                return new Tower();
            case "Barrack":
                return new Barrack();
            default:
                return null;
        }
    }

    private boolean isAdjacentToOwnedBlock(Player player, Block block) {
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
            if (hasBarrack) break;
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
        if (player.getUsedUnitSpace() + unit.unitSpace > player.getMaxUnitSpace()) {
            hudPanel.addLog("❌ Not enough unit space available.");
            return false;
        }

        return true;
    }

    private void payForStructure(Player player, Structures structure) {
        if (player.spendGold(structure.getBuildCost())) {
            updateHUD();
        } else {
            hudPanel.addLog("❌ Not enough gold to build this structure.");
        }
    }

    private void payForUnit(Player player, Unit unit) {
        boolean goldSpent = player.spendGold(unit.costGold);
        boolean foodConsumed = player.consumeFood(unit.costFood);
        boolean spaceUsed = player.useUnitSpace(unit.unitSpace);

        if (goldSpent && foodConsumed && spaceUsed) {
            updateHUD();
        } else {
            if (goldSpent) player.addGold(unit.costGold);
            if (foodConsumed) player.addFood(unit.costFood);
            if (spaceUsed) player.releaseUnitSpace(unit.unitSpace);

            hudPanel.addLog("❌ Payment failed due to insufficient resources.");
        }
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
        player.releaseUnitSpace(recoveredUnitSpace);

        hudPanel.addLog("❌ Resources went negative! All units were removed and their resources were refunded.");
        updateHUD();
    }

    public void endTurn() {
        payUnitMaintenanceCost(getCurrentPlayer());
        checkAndRemoveUnitsIfResourcesNegative(getCurrentPlayer());
        activeMoveUnit();

        moveFromBlock = null;
        if (selectedBlock != null) {
            selectedBlock.setBorder(new LineBorder(Color.BLACK, 1));
            selectedBlock = null;
        }

        currentPlayerIndex=(currentPlayerIndex+1)%players.length;
        hudPanel.addLog("Turn ended. It's now " + players[currentPlayerIndex].getName() + "'s turn.");
        updateHUD();
    }

    private void updateHUD() {
        Player currentPlayer = players[currentPlayerIndex];
        hudPanel.updatePlayerInfo(currentPlayer.getName(), currentPlayer.getGold(), currentPlayer.getFood(), currentPlayer.getUsedUnitSpace(), currentPlayer.getMaxUnitSpace());
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
        if (!attacker.getMoved()) {
            Unit defender = toBlock.getUnit();
            defender.setHealth(defender.getHealth() - attacker.getAttackPower());
            attacker.setMoved(true);
            if (defender.getHealth() <= 0 ) {
                hudPanel.addLog("Unit " + toBlock.getOwner().getName() + " was killed.");
                toBlock.setUnit(attacker);
                toBlock.setOwner(getCurrentPlayer());
                fromBlock.setUnit(null);
                attacker.setMoved(true);
            } else {
                hudPanel.addLog("Unit " + toBlock.getOwner().getName() + " was attacked. \n" + "Health: " + defender.getHealth());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Your unit has already made its move.");
        }
    }

    public void attackUnitToStructure(Block fromBlock, Block toBlock) {
        Unit attacker = fromBlock.getUnit();

        if (!attacker.getMoved()) {
            Structures structure = toBlock.getStructure();
            structure.setDurability(structure.getDurability() - attacker.getAttackPower());
            attacker.setMoved(true);
            if (structure.getDurability() <= 0) {
                hudPanel.addLog(structure.getName() + " " + toBlock.getOwner().getName() + " destroyed.");
                toBlock.setStructure(null);
                toBlock.setOwner(getCurrentPlayer());
                toBlock.setUnit(fromBlock.getUnit());
                fromBlock.setUnit(null);

                if(EndGmae()){
                    timer.stop();
                    if(players.length==4) {
                        gameData.INSERTable(players[0].getName(), players[1].getName(), players[2].getName(), players[3].getName(), players[currentPlayerIndex].getName(), TimeEndGame);
                    }else if(players.length==3){
                        gameData.INSERTable(players[0].getName(), players[1].getName(), players[2].getName(), "null", players[currentPlayerIndex].getName(), TimeEndGame);
                    }else gameData.INSERTable(players[0].getName(), players[1].getName(), "null", "null", players[currentPlayerIndex].getName(), TimeEndGame);
                    hudPanel.addLog("🎉 " + players[currentPlayerIndex].getName() + " won the game! Enemy Town Hall has been captured.");
                    JOptionPane.showMessageDialog(null, players[currentPlayerIndex].getName() + " won the game!");
                    gameSandL.EndGame();
                    gameEnded = true;
                    hudPanel.disableInteractionAfterGameEnd();
                    hudPanel.addLog("⚠️ Game has ended. No more attacks allowed.");
                    return;
                }
            } else {
                hudPanel.addLog("Structure" + toBlock.getOwner().getName() + " was attacked. \n" + "Durability:" + structure.getDurability());

            }
        } else {
            hudPanel.addLog("⚠️ Your unit has already made its move.");
        }

    }

    public boolean EndGmae() {
        end = 0;
        if(gamePanel.getBlock(0,0).getStructure()!=null) {
            if (gamePanel.getBlock(0, 0).getStructure().getName().equals("Town Hall")) {
                end++;
            }
        }
        if(gamePanel.getBlock(9,9).getStructure()!=null) {
            if (gamePanel.getBlock(9, 9).getStructure().getName().equals("Town Hall")) {
                end++;
            }
        }
        if(gamePanel.getBlock(9,0).getStructure()!=null) {
            if (gamePanel.getBlock(9, 0).getStructure().getName().equals("Town Hall")) {
                end++;
            }
        }
        if(gamePanel.getBlock(0,9).getStructure()!=null) {
            if (gamePanel.getBlock(0, 9).getStructure().getName().equals("Town Hall")) {
                end++;
            }
        }
        System.out.println(end);
        return end==1;
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

    public void loadGameForMenu(){
        Block[][] blocks = gameSandL.LoadGame(gamePanel.getBlocks());

        List<Player> playerList=new ArrayList<>();
        if (blocks[0][0].getStructure()!=null) {
            if (blocks[0][0].getStructure().getName().equals("Town Hall")) {
                playerList.add(blocks[0][0].getOwner());
            }
        }
        if (blocks[9][9].getStructure()!=null) {
            if (blocks[9][9].getStructure().getName().equals("Town Hall")) {
                playerList.add(blocks[9][9].getOwner());
            }
        }
        if (blocks[0][9].getStructure()!=null) {
            if (blocks[0][9].getStructure().getName().equals("Town Hall")) {
                playerList.add(blocks[0][9].getOwner());
            }
        }
        if (blocks[9][0].getStructure()!=null) {
            if (blocks[9][0].getStructure().getName().equals("Town Hall")) {
                playerList.add(blocks[9][0].getOwner());
            }
        }
        this.players = playerList.toArray(new Player[0]);

        gamePanel.loadGame(blocks,playerList);
        gamePanel.setHudPanel(hudPanel);
        gamePanel.setController(this);

        this.refreshBlockListeners();
        currentPlayerIndex = gameSandL.getNoBat();
        TimeEndGame=gameSandL.TimeGame();
        TimeForGetGoldAndFoolPlayer = 10;
        TimeForTurn = 30;
        updateHUD();
    }


    private void setupListeners() {
        hudPanel.getEndTurnButton().addActionListener(e -> {
            TimeForGetGoldAndFoolPlayer = 10;
            TimeForTurn = 30;
            hudPanel.getTimerTurnEnd().setForeground(new Color(190, 190, 190));
            hudPanel.getTimerTurnEnd().setText("Your Turn : " + TimeForTurn);
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
                    if (selectedStructures instanceof Barrack) {
                        Barrack barrack = (Barrack) selectedStructures;
                        getCurrentPlayer().addMaxUnitSpace(barrack.getUnitSpace());
                        updateHUD();
                    }
                    hudPanel.addLog("✅ Structure " + structureName + " has been successfully built.");
                } else {
                    hudPanel.addLog("❌ Not enough resources or the selected block is already occupied.");
                }
            } else {
                hudPanel.addLog("⚠️ Please select a structure.");
            }
        });

        // هماهنگ کردن ComboBox ها
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
            gameData.SELECTable();
        });

        hudPanel.getButtonSaveGame().addActionListener(e -> {
            gameSandL.DropTable();
            gameSandL.SaveGame(gamePanel.getBlocks(), gamePanel.getSIZE(), currentPlayerIndex,TimeEndGame);
        });

        hudPanel.getButtonLoadGame().addActionListener(e -> {
            Block[][] blocks = gameSandL.LoadGame(gamePanel.getBlocks());

            List<Player> playerList=new ArrayList<>();
            if (blocks[0][0].getStructure()!=null) {
                if (blocks[0][0].getStructure().getName().equals("Town Hall")) {
                    playerList.add(blocks[0][0].getOwner());
                }
            }
            if (blocks[9][9].getStructure()!=null) {
                if (blocks[9][9].getStructure().getName().equals("Town Hall")) {
                    playerList.add(blocks[9][9].getOwner());
                }
            }
            if (blocks[0][9].getStructure()!=null) {
                if (blocks[0][9].getStructure().getName().equals("Town Hall")) {
                    playerList.add(blocks[0][9].getOwner());
                }
            }
            if (blocks[9][0].getStructure()!=null) {
                if (blocks[9][0].getStructure().getName().equals("Town Hall")) {
                    playerList.add(blocks[9][0].getOwner());
                }
            }
            this.players = playerList.toArray(new Player[0]);

            gamePanel.loadGame(blocks,playerList);
            gamePanel.setHudPanel(hudPanel);
            gamePanel.setController(this);

            this.refreshBlockListeners();
            currentPlayerIndex = gameSandL.getNoBat();
            TimeEndGame=gameSandL.TimeGame();
            TimeForGetGoldAndFoolPlayer = 10;
            TimeForTurn = 30;
            updateHUD();
        });


        // تایمر نوبت و منابع
        timer = new javax.swing.Timer(1000, _ -> {
            TimeEndGame++;
            if (TimeForTurn < 10) {
                hudPanel.getTimerTurnEnd().setForeground(Color.RED);
            }
            hudPanel.getTimerTurnEnd().setText("Your Turn : " + TimeForTurn--);
            hudPanel.getTimerForget().setText("Getting resources up : " + TimeForGetGoldAndFoolPlayer--);

            if (TimeForGetGoldAndFoolPlayer == 0) {
                collectResources();
                updateHUD();
                TimeForGetGoldAndFoolPlayer = 10;
            }

            if (TimeForTurn < 0) {
                TimeForGetGoldAndFoolPlayer = 10;
                endTurn();
                TimeForTurn = 30;
                hudPanel.getTimerTurnEnd().setText("Your Turn : " + TimeForTurn);
                hudPanel.getTimerTurnEnd().setForeground(new Color(190, 190, 190));
            }
        });
        timer.start();

        // دکمه Level Up برای ارتقاء سازه
        hudPanel.getLevelUpButton().addActionListener(e -> {
            if (selectedBlock == null) {
                hudPanel.addLog("⚠️ Please select a block first.");
                return;
            }

            Structures structure = selectedBlock.getStructure();

            if (structure == null) {
                hudPanel.addLog("❌ No structure on selected block to level up.");
                return;
            }

            if (!selectedBlock.getOwner().equals(getCurrentPlayer())) {
                hudPanel.addLog("❌ You can only level up structures you own.");
                return;
            }

            if (structure.getLevel() >= structure.getMaxLevel()) {
                hudPanel.addLog("❌ " + structure.getName() + " is already at max level.");
                return;
            }

            int upgradeCost = 10 + structure.getLevel() * 5;

            if (!getCurrentPlayer().spendGold(upgradeCost)) {
                hudPanel.addLog("💰 Not enough gold. You need " + upgradeCost + " gold to upgrade.");
                return;
            }

            if (structure instanceof Barrack) {
                Barrack barrack = (Barrack) structure;
                int before = barrack.getUnitSpace();
                barrack.levelUp();
                int after = barrack.getUnitSpace();
                int diff = after - before;
                getCurrentPlayer().addMaxUnitSpace(diff);
            } else {
                structure.levelUp();
            }

            hudPanel.updatePlayerInfo(
                    getCurrentPlayer().getName(),
                    getCurrentPlayer().getGold(),
                    getCurrentPlayer().getFood(),
                    getCurrentPlayer().getUsedUnitSpace(),
                    getCurrentPlayer().getMaxUnitSpace()
            );
            hudPanel.addLog("🔺 Structure " + structure.getName() + " upgraded to level " + structure.getLevel() + ".");
            updateHUD();
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
                        } else if (block.getOwner() == getOpponentPlayer()) {
                            moveUnit(moveFromBlock, block, unit);
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
