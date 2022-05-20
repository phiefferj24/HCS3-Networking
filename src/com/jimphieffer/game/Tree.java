package com.jimphieffer.game;

import com.jimphieffer.game.Static;

import java.util.UUID;

import static java.lang.Integer.parseInt;

public class Tree extends Static {

    public Tree(double x, double y, int width, int height, String image, UUID id) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id);
    }
    public Tree(String x, String y, String width, String height, String image, String id) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id);
    }

    public Tree(String x, String y, String width, String height, String id) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, "textures/wood.png", id);
    }

    public String toString()
    {
        return "[" + "TREE" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() +  "]";
        //(String image, double x, double y, double vx, double vy, int width, int height, UUID id, int programID, double angle, int health) {
    }
}
