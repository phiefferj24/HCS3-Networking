package com.jimphieffer.game.objectTypes;


import com.jimphieffer.game.objectTypes.NonStatic;

import java.util.UUID;

import static java.lang.Integer.parseInt;

public class Living extends NonStatic {
    private double angle;
    private int health;

    public Living(double x, double y, int width, int height, String image, UUID id,
               double vx, double vy, int health) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id, vx, vy);
        this.health = health;
    }
    public Living(double x, double y) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, 50, 50, "/textures/Amogus.png", UUID.randomUUID(), 0, 0);
    }
    public Living(String x, String y, String width, String height, String image, String id,
               String vx, String vy, String health) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id, vx, vy);
        this.health = parseInt(health);
    }



    public int getHealth(){return health;}

    public void step()
    {
        super.step();
    }



    public String toString()
    {
        return "[" + "Living" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";"  + getVX() + ";" + getVY() +";" + getHealth()+";" + getAngle()+ "]";

        //(String image, double x, double y, double vx, double vy, int width, int height, UUID id, int programID, double angle, int health) {
    }
}
