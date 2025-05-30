package Blocks;

import Structure.Structures;
import src.main.java.Player;

import javax.swing.*;
import java.awt.*;

public abstract class Block extends JButton {
    protected int x;
    protected int y;
    protected String name;
    protected Color color;
    protected Player Owner;
    protected Structures Structure;


    //     protected Boolean findPlayer;
    public Block(int x, int y, String name, Color color, Player Owner, Structures Structure) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.Structure = Structure;
        this.Owner = Owner;

        setPreferredSize(new Dimension(70, 70)); // 👈 این مهمه
        setBackground(color);
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Player getOwner() {
        return Owner;
    }

    public void setOwner(Player owner) {
        Owner = owner;
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(70, 70); // عدد دلخواه، ترجیحاً مربعی
    }

//
//    public Boolean getFindPlayer() {
//        return findPlayer;
//    }
//
//    public void setFindPlayer(Boolean findPlayer) {
//        ..this.findPlayer = findPlayer;
//    }

}