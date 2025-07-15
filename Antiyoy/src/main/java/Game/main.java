package Game;


import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;


public class main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
    }
        SwingUtilities.invokeLater(()-> new Menu() );

    }
}
