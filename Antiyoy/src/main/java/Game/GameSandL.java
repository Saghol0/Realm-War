package Game;

import Blocks.Block;
import Structure.*;
import Units.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

 public class GameSandL {
    private final String URL="jdbc:postgresql://localhost:5432/realm war";
    private final String USER="postgres";
    private final String PASSWORD="1383";

    private final String FolderAddress="FolderSandL";
    private final String FileAddress=FolderAddress+"/SaveLoad";
    private  boolean IfFolder;
    private  boolean IfFile;
    private HUDPanel hudPanel;
    private File Folder;
    private File file;

    public GameSandL (HUDPanel h){
        try {


            Folder = new File(FolderAddress);
            if (!Folder.exists()) {
                IfFolder = Folder.mkdir();
                if (IfFolder) {
                    hudPanel.addLog("Folder Create");
                } else hudPanel.addLog("Folder Made");
            }


            file = new File(FileAddress);
            if (!file.exists()) {
                IfFile = file.createNewFile();
                if(IfFile){
                    hudPanel.addLog("File Create");
                }else hudPanel.addLog("File Made");


            }
        } catch (IOException e){
            hudPanel.addLog("Error file:"+ e.getMessage());
        }
        this.hudPanel=h;
        CreateTableSaveGame();

    }

    public void CreateTableSaveGame(){
        String SQLBlock = "CREATE TABLE IF NOT EXISTS Block(" +
                "ID SERIAL PRIMARY KEY ,"+
                "X INT NOT NULL ,"+
                "Y INT NOT NULL ,"+
                "Name VARCHAR(85) NOT NULL,"+
                "Color INT NOT NULL )";

        String SQLPlayer=  "CREATE TABLE IF NOT EXISTS Player ("+
                "ID INT NOT NULL ,"+
                "Name VARCHAR(85) NOT NULL ,"+
                "Gold INT NOT NULL ,"+
                "Food INT NOT NULL ,"+
                "UnitSpace INT NOT NULL ,"+
                "Color INT NOT NULL )";

        String SQLStructures = "CREATE TABLE IF NOT EXISTS Structures ("+
                "ID INT NOT NULL ,"+
                "Name VARCHAR(85) NOT NULL ,"+
                "durability INT NOT NULL ,"+
                "maintenanceCost INT NOT NULL ,"+
                "level INT NOT NULL ,"+
                "maxLevel INT NOT NULL ,"+
                "Image VARCHAR(150) NOT NULL ,"+
                "buildCost INT NOT NULL ,"+
                "health INT  )  ";

        String SQLUnit ="CREATE TABLE IF NOT  EXISTS Unit (" +
                "ID INT NOT NULL ,"+
                "Name VARCHAR(85) NOT NULL ,"+
                "Rank INT NOT NULL ,"+
                "movementRange INT NOT NULL ,"+
                "CostGold INT NOT NULL ,"+
                "CostFood INT NOT NULL ,"+
                "UnitSpace INT NOT NULL ," +
                "Image VARCHAR(150) NOT NULL ,"+
                "Moved BOOLEAN  ,"+
                "health INT NOT NULL ,"+
                "attackPower INT NOT NULL )";

        String SQLGameController = "CREATE TABLE IF NOT  EXISTS Controller(NoBat INT NOT NULL) ";



        try{
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement Stmt=conn.createStatement();
            Stmt.execute(SQLBlock);
            Stmt.execute(SQLPlayer);
            Stmt.execute(SQLStructures);
            Stmt.execute(SQLUnit);
            Stmt.execute(SQLGameController);
            hudPanel.addLog("..CREATE save TABLE..");
            try{
                BufferedWriter riter= new BufferedWriter(new FileWriter(file,true));
                riter.write("..CREATE save TABLE..");
                riter.newLine();
                riter.close();
                hudPanel.addLog("..CREATE save TABLE to File..");
            }catch (IOException e){
                hudPanel.addLog("ERROR CREATE FILE:"+ e.getMessage());
            }
        }catch (SQLException e){
            hudPanel.addLog("ERROR CREATE Save:" + e.getMessage());
        }
    }


    public Image StringtoImage(String s){
        switch (s){
            case "Farm": return Farm.loadImage() ;
            case "Barrack": return Barrack.loadImage();
            case "Market": return Market.loadImage();
            case "Tower": return Tower.loadImage();
            case "Town Hall": return TownHall.loadImage();
            case "Knight": return Knight.loadImage();
            case "Peasant": return Peasant.loadImage();
            case "Spearman": return Spearman.loadImage();
            case "Swordman": return Swordman.loadImage();
            default: return null;
        }
    }


    public void SaveGame(Block[][] b, int SIZE,int NoBat){
        String SQLBlack="INSERT INTO Block(X,Y,Name,Color) VALUES (?,?,?,?) RETURNING ID";
        String SQLPlayer="INSERT INTO Player(ID,Name,Gold,Food,UnitSpace,Color) VALUES (?,?,?,?,?,?) ";
        String SQLStructures="INSERT INTO Structures(ID,Name,durability,maintenanceCost,level,MaxLevel,Image,buildCost)"+
                "VALUES (?,?,?,?,?,?,?,?)";
        String SQLUnit="INSERT INTO Unit(ID,Name,Rank,movementRange,CostGold,CostFood,UnitSpace,Image,health,attackPower)"+
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String SQLGameController= "INSERT INTO Controller(NoBat) VALUES (?)";
        int ID;

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement PstmtB = conn.prepareStatement(SQLBlack);
                PreparedStatement PstmtP = conn.prepareStatement(SQLPlayer);
                PreparedStatement PstmtS = conn.prepareStatement(SQLStructures);
                PreparedStatement PstmtU = conn.prepareStatement(SQLUnit);
                PreparedStatement PstmtC = conn.prepareStatement(SQLGameController);
        ){

            PstmtC.setInt(1,NoBat);
            PstmtC.executeUpdate();
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Block block=b[i][j];


                    PstmtB.setInt(1, block.getGridX());
                    PstmtB.setInt(2, block.getGridY());
                    PstmtB.setString(3, block.getNAME());
                    PstmtB.setInt(4, block.getColor());


                    try (ResultSet ru = PstmtB.executeQuery()) {
                        if (ru.next()) {
                            ID = ru.getInt("ID");
                        } else {
                            ID = -1;
                        }
                    }

                    if (block.getOwner() != null) {
                        PstmtP.setInt(1, ID);
                        PstmtP.setString(2, block.getOwner().getName());
                        PstmtP.setInt(3, block.getOwner().getGold());
                        PstmtP.setInt(4, block.getOwner().getFood());
                        PstmtP.setInt(5, block.getOwner().getUnitSpace());
                        PstmtP.setInt(6, (block.getOwner().getColor()).getRGB());
                        PstmtP.executeUpdate();
                    }

                    if (block.getStructure() != null) {
                        PstmtS.setInt(1, ID);
                        PstmtS.setString(2, block.getStructure().getName());
                        PstmtS.setInt(3, block.getStructure().getDurability());
                        PstmtS.setInt(4, block.getStructure().getMaintenanceCost());
                        PstmtS.setInt(5, block.getStructure().getLevel());
                        PstmtS.setInt(6, block.getStructure().getMaxLevel());
                        PstmtS.setString(7, block.getStructure().getName());
                        PstmtS.setInt(8, block.getStructure().getBuildCost());
                        PstmtS.executeUpdate();
                    }

                    if (block.getUnit() != null) {
                        PstmtU.setInt(1, ID);
                        PstmtU.setString(2, block.getUnit().getName());
                        PstmtU.setInt(3, block.getUnit().getRank());
                        PstmtU.setInt(4, block.getUnit().getMovementRange());
                        PstmtU.setInt(5, block.getUnit().getCostGold());
                        PstmtU.setInt(6, block.getUnit().getCostFood());
                        PstmtU.setInt(7, block.getUnit().getUnitSpace());
                        PstmtU.setString(8, block.getUnit().getName());
                        PstmtU.setInt(9, block.getUnit().getHealth());
                        PstmtU.setInt(10, block.getUnit().getAttackPower());
                        PstmtU.executeUpdate();
                    }

                }}
            hudPanel.addLog("..Save Game verified..");
            try{
                BufferedWriter riter= new BufferedWriter(new FileWriter(file,true));
                riter.write("..CREATE save TABLE..");
                riter.newLine();
                riter.close();
                hudPanel.addLog("..CREATE save TABLE to File..");
            }catch (IOException e){
                hudPanel.addLog("ERROR CREATE FILE:"+ e.getMessage());
            }

        } catch (SQLException e) {
            hudPanel.addLog("Error Save: " + e.getMessage());

        }

    }

    public Block[][] LoadGame(Block[][] blocks) {
        String SQLLoadB = "SELECT * FROM Block";
        String SQLLoadP = "SELECT * FROM Player";
        String SQLLoadS = "SELECT * FROM Structures";
        String SQLLoadU = "SELECT * FROM Unit";

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmtB = conn.createStatement();
                Statement stmtP = conn.createStatement();
                Statement stmtS = conn.createStatement();
                Statement stmtU = conn.createStatement();
                ResultSet rsB = stmtB.executeQuery(SQLLoadB);
                ResultSet rsP = stmtP.executeQuery(SQLLoadP);
                ResultSet rsS = stmtS.executeQuery(SQLLoadS);
                ResultSet rsU = stmtU.executeQuery(SQLLoadU)
        ) {
            Map<Integer, Player> playerMap = new HashMap<>();
            Map<Integer, Structures> structureMap = new HashMap<>();
            Map<Integer, Unit> unitMap = new HashMap<>();

            while (rsP.next()) {
                int id = rsP.getInt("ID");
                Player player = new Player(
                        rsP.getString("Name"),
                        new Color(rsP.getInt("Color"), true),
                        rsP.getInt("Gold"),
                        rsP.getInt("Food"),
                        rsP.getInt("UnitSpace")
                );
                playerMap.put(id, player);
            }

            while (rsS.next()) {
                int id = rsS.getInt("ID");
                String name = rsS.getString("Name");

                Structures structure = null;
                switch (name) {
                    case "Farm":
                        structure = new Structure.Farm(); break;
                    case "Market":
                        structure = new Structure.Market(); break;
                    case "Tower":
                        structure = new Structure.Tower(); break;
                    case "Barrack":
                        structure = new Structure.Barrack(); break;
                    case "Town Hall":
                        structure = new Structure.TownHall(); break;
                }

                if (structure != null) {
                    structure.setDurability(rsS.getInt("durability"));
                    structure.setLevel(rsS.getInt("level"));
                    structure.setMaintenanceCost(rsS.getInt("maintenanceCost"));
                    structure.setMaxLevel(rsS.getInt("MaxLevel"));
                    structure.setBuildCost(rsS.getInt("buildCost"));
                    structureMap.put(id, structure);
                }
            }

            while (rsU.next()) {
                int id = rsU.getInt("ID");
                String name = rsU.getString("Name");
                int rank = rsU.getInt("Rank");
                int movementRange = rsU.getInt("movementRange");
                int costGold = rsU.getInt("CostGold");
                int costFood = rsU.getInt("CostFood");
                int unitSpace = rsU.getInt("UnitSpace");
                int health = rsU.getInt("health");
                int attackPower = rsU.getInt("attackPower");

                JLabel label = new JLabel();
                Unit unit = null;

                switch (name) {
                    case "Knight":
                        unit = new Knight(label); break;
                    case "Peasant":
                        unit = new Peasant(label); break;
                    case "Swordman":
                        unit = new Swordman(label); break;
                    case "Spearman":
                        unit = new Spearman(label); break;
                }

                if (unit != null) {
                    unit.setRank(rank);
                    unit.setMovementRange(movementRange);
                    unit.costGold = costGold;
                    unit.costFood = costFood;
                    unit.unitSpace = unitSpace;
                    unit.setHealth(health);
                    unit.setAttackPower(attackPower);
                    unit.setMoved(false);
                    unitMap.put(id, unit);
                }
            }

            while (rsB.next()) {
                int x = rsB.getInt("X");
                int y = rsB.getInt("Y");
                String name = rsB.getString("Name");
                int colorInt = rsB.getInt("Color");
                int id = rsB.getInt("ID");

                Player player = playerMap.get(id);
                Structures structure = structureMap.get(id);
                Unit unit = unitMap.get(id);

                blocks[x][y] = new Block(x, y, name, new Color(colorInt, true), player, structure, unit);
            }

            hudPanel.addLog("..Load Game verified..");
            try{
                BufferedWriter riter= new BufferedWriter(new FileWriter(file,true));
                riter.write("..Load Game verified..");
                riter.newLine();
                riter.close();
                hudPanel.addLog("..Load Game verified to File..");
            }catch (IOException e){
                hudPanel.addLog("ERROR LoadGame FILE:"+ e.getMessage());
            }
        } catch (SQLException e) {
            hudPanel.addLog("ERROR LoadGame: " + e.getMessage());
        }

        return blocks;
    }

    public int getNoBat(){

        String SQL = "SELECT * FROM Controller";

        try {

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement Pstmt=conn.prepareStatement(SQL);
            ResultSet ru=Pstmt.executeQuery();

            if(ru.next()){
                return ru.getInt("NoBat");
            }

        }catch (SQLException e){
            hudPanel.addLog("ERROR getNoBat :"+ e.getMessage());
            return 0;
        }
        return 0;
    }



    public void DropTable(){
        String SQL="DROP TABLE block,player,unit,structures,Controller;";
        try {
            Connection conn =DriverManager.getConnection(URL,USER,PASSWORD);
            Statement Stmt=conn.createStatement();
            Stmt.execute(SQL);

            hudPanel.addLog("Drop verified");

                try (BufferedWriter riter = new BufferedWriter(new FileWriter(file, true))) {
                    riter.write("..DROP Game verified..");
                    riter.newLine();
                    hudPanel.addLog("..DROP Game verified to File..");
                } catch (IOException e) {
                    hudPanel.addLog("ERROR LoadGame FILE:" + e.getMessage());
                }


        }catch (SQLException e){
            hudPanel.addLog("ERROR DROP:" + e.getMessage());
        }
        CreateTableSaveGame();
    }



}