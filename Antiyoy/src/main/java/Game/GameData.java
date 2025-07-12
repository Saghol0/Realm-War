package Game;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.sql.*;

public class GameData {
    private final String URL="jdbc:postgresql://localhost:5432/realm War";
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
              "Winner VARCHAR(20) ,"+
              "ScorePlayer1 DECIMAL(5,2) ,"+
              "ScorePlayer2 DECIMAL(5,2) ,"+
              "turns INT ) ";

      try{
          Connection connection = DriverManager.getConnection(URL,USER,PASSWORD);
          Statement Stmt = connection.createStatement();
          Stmt.execute(SQL);
          hudPanel.addLog("..Table verified..");
      }catch (SQLException e){
          hudPanel.addLog("ERROR CREAT :" + e.getMessage());
      }

    }

    public boolean INSERTable(String Player1,String Player2,String winner,Double ScorePlayer1,Double ScorePlayer2,int turns){
        String SQL="INSERT INTO ListData (Player1,Player2,Winner,ScorePlayer1,ScorePlayer2,turns) VALUES (?,?,?,?,?,?)";
        try{
            Connection connection=DriverManager.getConnection(URL,USER,PASSWORD);
            PreparedStatement Pstmt=connection.prepareStatement(SQL);
            Pstmt.setString(1,Player1);
            Pstmt.setString(2,Player2);
            Pstmt.setString(3,winner);
            Pstmt.setDouble(4,ScorePlayer1);
            Pstmt.setDouble(5,ScorePlayer2);
            Pstmt.setInt(6,turns);
            if(0<Pstmt.executeUpdate()){
                hudPanel.addLog("..INSERT verified..");
                return true;
            } else {
                hudPanel.addLog("..ERROR INSERT..");
                return false;
            }
        }catch (SQLException e){
            hudPanel.addLog("ERROR INSERT"+ e.getMessage());
            return false;
        }
    }


    public void SELECTable(){
        String SQL="SELECT * From ListData";

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
            panel.add(new JLabel("Winner"),gbc);

            gbc.gridx=4;
            panel.add(new JLabel("ScorePlayer1"),gbc);

            gbc.gridx=5;
            panel.add(new JLabel("ScorePlayer2"),gbc);

            gbc.gridx=6;
            panel.add(new JLabel("turns"),gbc);

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
             panel.add(new JLabel ( ru.getString("Winner")  ),gbc);

             gbc.gridx=4;
             panel.add(new JLabel (String.valueOf( ru.getDouble("ScorePlayer1") ) ),gbc);

             gbc.gridx=5;
             panel.add(new JLabel (String.valueOf( ru.getDouble("ScorePlayer2") ) ),gbc);

             gbc.gridx=6;
             panel.add(new JLabel (String.valueOf( ru.getInt("turns") ) ),gbc);

             RadiF++;
            }
            frame.add(new JScrollPane(panel));
            frame.setVisible(true);
        }catch (SQLException e){
            hudPanel.addLog("ERROR SELECT"+e.getMessage());
        }


    }

}
