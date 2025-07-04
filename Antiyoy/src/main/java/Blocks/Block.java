package Blocks;

import Structure.Structures;
import Units.Unit;
import src.main.java.HUDPanel;
import src.main.java.Player;

import javax.swing.*;
import java.awt.*;

public class Block extends JButton {
    protected int x;
    protected int y;
    protected String name;
    protected Color color;
    protected Player Owner;
    protected Structures Structure;
    protected Unit unit ;
//    protected HUDPanel hudPanel;
    public Block(int x, int y, String name, Color color, Player Owner, Structures Structure,  Unit unit) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.Structure = Structure;
        this.Owner = Owner;
        this.unit = unit;
//        this.hudPanel = hudPanel;

        setBackGroundColor();
        setOpaque(true);
        setBorderPainted(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        updateIcon();

    }
    public void setBackGroundColor()
    {
        if (Owner != null && Owner.getColor() != null) {
            setBackground(Owner.getColor());
        } else {
            setBackground(color);
        }
    }
    public void updateIcon() {
        if (Structure != null && Structure.getIcon() != null) {
            setIcon(new ImageIcon(Structure.getIcon()));
        }
        else if (unit != null && unit.getIcon() != null) {
            setIcon(new ImageIcon(unit.getIcon()));
        }
        else {
            setIcon(null); // اطمینان حاصل کن آیکون پاک میشه وقتی Structure حذف بشه
        }
    }

    public void setStructure(Structures structure) {
        this.Structure = structure;
        updateIcon();
    }

    public Structures getStructure() {
        return Structure;
    }

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player owner) {
        this.Owner = owner;
    }


}
