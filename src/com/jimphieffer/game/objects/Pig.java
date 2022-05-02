package com.jimphieffer.game.objects;

import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Pig extends NonStatic{
    private int health;

    public Pig(double x, double y, int width, int height, String image, UUID id, int programID,
               double vx, double vy, int health) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id, programID, vx, vy);
        this.health = health;
    }
    public Pig(String x, String y, String width, String height, String image, String id, String programID,
               String vx, String vy, String health) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id, programID, vx, vy);
        this.health = parseInt(health);
    }



    public int getHealth(){return health;}



    public String toString()
    {
        return "[" + "PIG" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";" +  getProgramID()+ ";" + getVX() + ";" + getVY() +";" + getHealth()+ "]";

        //(String image, double x, double y, double vx, double vy, int width, int height, UUID id, int programID, double angle, int health) {
    }
}
