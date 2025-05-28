package src.main.java;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
public class main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(()->new GameFrame() );

    }
}
