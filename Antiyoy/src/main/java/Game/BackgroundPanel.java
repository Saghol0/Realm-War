package Game;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
private final Image backgroundImage;
public BackgroundPanel(String imageName) {

    URL imageUrl = getClass().getClassLoader().getResource(imageName);
    System.out.println("Image path: " + imageUrl);
    if(imageUrl == null)
    {
        throw new RuntimeException("Background image not found");
    }
    backgroundImage = new ImageIcon(imageUrl).getImage();
}
protected  void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
}

}
