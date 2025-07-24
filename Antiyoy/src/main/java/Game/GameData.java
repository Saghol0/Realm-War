package Game;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.sql.*;

public class GameData {
    private final String URL="jdbc:postgresql://localhost:5432/Pain";
    private final String USER="postgres";
    private final String PASSWORD="73752";
    private HUDPanel hudPanel;


    public GameData(HUDPanel h){
        this.hudPanel=h;
        CREATETable();
    }

    public void CREATETable(){
      String SQL ="CREATE TABLE IF NOT EXISTS ListData( "+
              "IDGame SERIAL PRIMARY KEY ,"+
              "Player1 VARCHAR(55) NOT NULL ,"+
              "Player2 VARCHAR(55) NOT NULL ,"+
              "Player3 VARCHAR(55),"+
              "Player4 VARCHAR(55),"+
              "Winner VARCHAR(20) ,"+
              "Playtime INT  ,"+
              "play_date DATE DEFAULT CURRENT_DATE)" ;


      try{
          Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
          Statement Stmt = connection.createStatement();
          Stmt.execute(SQL);
          hudPanel.addLog("..Table verified..");
      }catch (SQLException e){
          hudPanel.addLog("ERROR CREAT :" + e.getMessage());
      }

    }

    public int INSERTable(String Player1,String Player2,String Player3,String Player4){
        String SQL="INSERT INTO ListData (Player1,Player2,Player3,Player4) VALUES (?,?,?,?) RETURNING IDGame";
        try{
            Connection connection=DriverManager.getConnection(URL,USER,PASSWORD);
            PreparedStatement Pstmt=connection.prepareStatement(SQL);
            Pstmt.setString(1,Player1);
            Pstmt.setString(2,Player2);
            Pstmt.setString(3,Player3);
            Pstmt.setString(4,Player4);

            ResultSet ru =Pstmt.executeQuery();

            if(ru.next()){
                hudPanel.addLog("..INSERT verified..");
                return ru.getInt(1);
            } else {
                hudPanel.addLog("..ERROR INSERT..");
                return -1;
            }
        }catch (SQLException e){
            hudPanel.addLog("ERROR INSERT"+ e.getMessage());
            return -1;
        }
    }

    public void UpdateTimeAndWinner(int ID,int Playtime,String Winner){
    String SQL = "UPDATE ListData set Winner = ?,Playtime = ? where IDGame = ? ";

    try {
        Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
        PreparedStatement Stmt= conn.prepareStatement(SQL);
        Stmt.setString(1,Winner);
        Stmt.setInt(2,Playtime);
        Stmt.setInt(3,ID);
        Stmt.executeUpdate();
        hudPanel.addLog("..Update verified..");
    }catch (SQLException e){
        hudPanel.addLog("ERROR INSERT Time and Winner :"+ e.getMessage());
    }

    }

    public void DeleteNull(int ID){
        String SQL = "DELETE FROM ListData where IDGame = ?";

        try {
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            PreparedStatement Pstmt = conn.prepareStatement(SQL);
            Pstmt.setInt(1,ID);
            Pstmt.executeUpdate();
            hudPanel.addLog("..DELETE verified..");

        }catch (SQLException e){
            hudPanel.addLog("ERROR ListData delete"+ e.getMessage());
        }
    }




    public void SELECTable(){
        String SQL="SELECT * From ListData ";

        try{
            Connection connection=DriverManager.getConnection(URL,USER,PASSWORD);
            Statement Stmt=connection.createStatement();
            ResultSet ru=Stmt.executeQuery(SQL);


            JFrame frame=new JFrame();
            frame.setSize(600,450);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setTitle("View");

            JPanel panel=new JPanel(new GridBagLayout());

            GridBagConstraints gbc=new GridBagConstraints();
            gbc.insets=new Insets(5,5,5,5);
            gbc.gridx=0; gbc.gridy=0;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            panel.add(new JLabel("IdGame"),gbc);

            gbc.gridx=1;
            panel.add(new JLabel("Player1"),gbc);

            gbc.gridx=2;
            panel.add(new JLabel("Player2"),gbc);

            gbc.gridx=3;
            panel.add(new JLabel("Player3"),gbc);

            gbc.gridx=4;
            panel.add(new JLabel("Player4"),gbc);

            gbc.gridx=5;
            panel.add(new JLabel("Winner"),gbc);

            gbc.gridx=6;
            panel.add(new JLabel("Playtime"),gbc);

            gbc.gridx=7;
            panel.add(new JLabel("Playdate"));

             int RadiF=1;
            while (ru.next()){
             gbc.gridy=RadiF;

             gbc.gridx=0;
             panel.add(new JLabel (String.valueOf( ru.getInt("IDGame") ) ),gbc);

             gbc.gridx=1;
             panel.add(new JLabel ( ru.getString("Player1")  ),gbc);

             gbc.gridx=2;
             panel.add(new JLabel ( ru.getString("Player2")  ),gbc);

             gbc.gridx=3;
             panel.add(new JLabel ( ru.getString("Player3")  ),gbc);

             gbc.gridx=4;
             panel.add(new JLabel (String.valueOf( ru.getString("Player4") ) ),gbc);

             gbc.gridx=5;
             panel.add(new JLabel (String.valueOf( ru.getString("Winner") ) ),gbc);

             gbc.gridx=6;
             panel.add(new JLabel (String.valueOf( ru.getInt("Playtime") ) ),gbc);

             gbc.gridx=7;
             panel.add(new JLabel(String.valueOf( ru.getString("play_date") ) ),gbc);

             RadiF++;
            }
            frame.add(new JScrollPane(panel));
            frame.setVisible(true);
        }catch (SQLException e){
            hudPanel.addLog("ERROR SELECT"+e.getMessage());
        }


    }

}
