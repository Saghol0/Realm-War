package src.main.java;

import javax.swing.*;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(()->new GameFrame() );

    }
}
