package Game;

import Blocks.Block;
import Structure.Structures;
import Units.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class GameController {
    private Block selectedBlock = null;
    private GamePanel gamePanel;
    private HUDPanel hudPanel;
    private Player[] players;
    private int currentPlayerIndex = 0;
    private Unit selectedUnit = null;

    public GameController(GamePanel gamePanel, HUDPanel hudPanel, Player[] players) {
        this.gamePanel = gamePanel;
        this.hudPanel = hudPanel;
        this.players = players;

        setupListeners();
        updateHUD();
    }
//نصب اکشن لیسنر ها
    private void setupListeners() {
        //دکمه end turn
        hudPanel.getEndTurnButton().addActionListener(e -> endTurn());

        hudPanel.getUnitSelector().addActionListener(e -> {
            String unitName = (String) hudPanel.getUnitSelector().getSelectedItem();
            if (!"None".equals(unitName)) {
                selectedUnit = createUnitByName(unitName);
                hudPanel.getStructureSelector().setSelectedItem("None");
                hudPanel.addLog("Selected Unit: " + unitName);
            } else {
                selectedUnit = null;
            }
        });
        //لیست استراکچر
        hudPanel.getStructureSelector().addActionListener(e -> {
            String structureName = (String) hudPanel.getStructureSelector().getSelectedItem();
            if (!"None".equals(structureName)) {
                selectedUnit = null;
                hudPanel.getUnitSelector().setSelectedItem("None");
                hudPanel.addLog("Selected Structure: " + structureName);
            }
        });
    }

    public void handleBlockClick(Block block) {
        if (selectedBlock != null) {
            selectedBlock.setBorder(new  LineBorder(Color.BLACK,1));
        }
        block.setBorder(new LineBorder(Color.BLACK,5));
        selectedBlock = block;

        Player currentPlayer = players[currentPlayerIndex];
        String selectedStructureName = (String) hudPanel.getStructureSelector().getSelectedItem();

        if (!"None".equals(selectedStructureName)) {
            if ((block.getOwner() == currentPlayer || block.getOwner()== null) && block.getStructure() == null) {
                Structures newStructure = createStructureByName(selectedStructureName);
                if (canBuildStructure(currentPlayer, newStructure)) {
                    block.setStructure(newStructure);
                    block.setOwner(currentPlayer);
                    block.setBackGroundColor();
                    payForStructure(currentPlayer, newStructure);
                    hudPanel.addLog(currentPlayer.getName() + " built " + selectedStructureName);
                    updateHUD();
                } else {
                    hudPanel.addLog("Not enough resources to build " + selectedStructureName);
                }
            } else {
                hudPanel.addLog("Cannot build here!");
            }
            return;
        }

        if (selectedUnit != null) {
            if ((block.getOwner() == currentPlayer||block.getOwner()==null) && block.getStructure() instanceof Structure.Barrack) {
                if (canBuildUnit(currentPlayer, selectedUnit)) {
                    hudPanel.addLog(currentPlayer.getName() + " created unit: " + selectedUnit.getName());
                    payForUnit(currentPlayer, selectedUnit);
                    updateHUD();
                    selectedUnit = null;
                    hudPanel.getUnitSelector().setSelectedItem("None");
                } else {
                    hudPanel.addLog("Not enough resources to create unit: " + selectedUnit.getName());
                }
            } else {
                hudPanel.addLog("Units can only be created on your Barrack");
            }
            return;
        }

        // TODO: حرکت واحد، حمله و غیره

        updateHUD();
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
        return player.getGold() >= structure.getMaintenanceCost();
    }

    private boolean canBuildUnit(Player player, Unit unit) {
        return player.getGold() >= unit.costGold && player.getFood() >= unit.costFood && player.getUnitSpace() + unit.unitSpace <= 100;
    }

    private void payForStructure(Player player, Structures structure) {//هزینه ساخت ساختمون
        player.addGold(-structure.getMaintenanceCost());
    }

    private void payForUnit(Player player, Unit unit) {
        player.addGold(-unit.costGold);
        player.addFood(-unit.costFood);
        player.addUnitSpace(unit.unitSpace);
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
