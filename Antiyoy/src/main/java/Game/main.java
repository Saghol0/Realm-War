package Game;


import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatPropertiesLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;


public class main {
    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen("Image/loadiing.png");
        splash.showSplash(2500);
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
    }
        SwingUtilities.invokeLater(()-> new Menu() );

    }
}
