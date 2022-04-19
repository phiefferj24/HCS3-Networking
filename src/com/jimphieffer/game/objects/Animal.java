package com.jimphieffer.game.objects;

import com.jimphieffer.game.Game;
import com.jimphieffer.game.Sprite;

import java.util.Random;

public class Animal extends Sprite {
    private double vx;
    private double vy;
    private int health;
    private String username;
    private double angle;


    public Animal(double x, double y, int width, int height,int programID)
    {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one
        /*
        double left, double top, int width, int height, String image, int programID,
                  double velocityX, double velocityY, String username
         */
        super(x, y,width,height,"/textures/textureMissing.png",programID);
        health  = 15;
        vx = 0.0;
        vy=0.0;
        angle = 0.0;
        Random r = new Random();
        int low = 1;
        int high = 10000000;
        int result = r.nextInt(high-low) + low;
        username = "ANIMAL" + this.hashCode();

    }

    public double getVY()
    {

        return vy;
    }

    public void setVY(double velocityY)
    {
        vy = velocityY;
    }

    public double getVX()
    {
        //make this use the fucntion to get the x
        return vx;
    }

    public void setVX(double velocityX)
    {
        vx = velocityX;
    }

    public int getHealth(){return health;}

    public String getUsername(){return username;}

    public void setHealth(int health){this.health=health;}

    public double getAngle()
    {
        return angle;
    }

    public void step(Game game)
    {

        //System.out.println(theta);
        int theta = (int) (System.currentTimeMillis()%1000);
        double tempVX = 3 * Math.sin(theta / Math.PI);

        //movement on animals
        //

        //there is rotate function

        //make this use the function to get the y
        if(vx<0)
            vx+=1;
        if(vx>0)
            vx-=1;
        if(vy<0)
            vy+=1;
        if(vy>0)
            vy-=1;
        if(Math.abs(vx)<.3)
            vx = 0;
        if(Math.abs(vy)<.3)
            vy = 0;

//        if (getLeft() < -game.getWorldWidth())
//            vx = Math.abs(vx);
//        if (getLeft() + getWidth() > game.getWorldWidth())
//            vx = -Math.abs(vx);
//        if (getTop() < -game.getWorldHeight())
//            vy = Math.abs(vy);
//        if (getTop() + getHeight() >game.getWorldHeight())
//            vy = -Math.abs(vy);
        setX(getX()+vx);
        setY(getY()+vy);
    }

    public String toString()
    {
        return "null";// I FARTED AND POOPIED ALL OVER THE GROUNDDDDD FARTTTTTT POOOOPPPYYY FART AND FART AND POOP
        //TODO: TIKO IS THIS USERNAME CHANGE OKAY ON TOSTRING WHERE DO YOU USE THIS
        //return "[" + username + ";" + x +";" + y + ";" + width + ";" + height + ";" + "/textures/wall.png" + "]";
    }
}
