package Blocks;

import Game.Player;
import Structure.Structures;
import Units.Unit;


import javax.swing.*;
import java.awt.*;

public class Block extends JButton {

    protected int x, y;
    protected String name;
    protected Color baseColor;
    protected Player owner;
    protected Structures structure;
    protected Unit unit;

    public Block(int x, int y, String name, Color color) {
        this(x, y, name, color, null, null,null);
    }

    public Block(int x, int y, String name, Color color, Player owner, Structures structure,Unit unit) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.baseColor = color;
        this.owner = owner;
        this.structure = structure;
        this.unit = unit;

        setBackGroundColor();
        setOpaque(true);
        setBorderPainted(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        updateIcon();
    }

    public void setBackGroundColor() {
        if (  owner != null && owner.getColor() != null) {
            setBackground(owner.getColor());
        } else {
            setBackground(baseColor);
        }
    }

    public void updateIcon() {
        if (structure != null && structure.getIcon() != null) {
            setIcon(new ImageIcon(structure.getIcon()));
        } else if (unit != null && unit.getIcon() != null) {
            setIcon(new ImageIcon(unit.getIcon()));
        } else {
            setIcon(null);
        }
    }

    // Getters and setters
    public void setStructure(Structures structure) {
        this.structure = structure;
        updateIcon();
    }

    public Structures getStructure() {
        return structure;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
        setBackGroundColor();
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        updateIcon();
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public String getBlockName() {
        return name;
    }


    public int getGridX() {
        return x;
    }

    public int getGridY() {
        return y;
    }

    public int getColor(){
        return baseColor.getRGB();
    }

    public String getNAME(){
        return name;
    }

}