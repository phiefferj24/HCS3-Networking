package com.jimphieffer.game.objects;

import com.jimphieffer.game.Game;
import com.jimphieffer.game.Sprite;

import java.util.UUID;

import static java.lang.Double.parseDouble;

public class NonStatic extends Sprite {
    private double vx;
    private double vy;
    private double x;
    private double y;
    private int health;
    private double angle;
    private String image;

    //TODO: change change animal to static and extend it
    public NonStatic(double x, double y, int width, int height, String image, UUID id, int programID,
                     double vx, double vy)
    {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        super(x, y,width,height,image,id,programID);
        this.image=image;
        this.health  = health;
        this.x=x;
        this.y=y;
        this.vx = vx;
        this.vy=vy;
        this.image = image;
    }
    public NonStatic(String x, String y, String width, String height, String image, String id, String programID,
                     String vx, String vy)
    {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        super(x, y,width,height,image,id,programID);
        this.image=image;
        this.health  = health;
        this.x=parseDouble(x);
        this.y=parseDouble(y);
        this.vx=parseDouble(vx);
        this.vy=parseDouble(vy);
        this.image = image;
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

    public void setVX(double velocityX) {
        vx = velocityX;
    }
    public void setAngle(double change)
    {
        angle=change;
    }



    public void setHealth(int health){this.health=health;}

    public double getAngle()
    {
        return angle;
    }

    public void step(Game game)
    {
        //dont put it here specify in lowest order class
    }

    //String image, double x, double y, int width, int height, double angle, int health,  int programID)

    public String toString(){
        return "[" + "NONSTATIC" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";" +  getProgramID()+ ";" + getVX() + ";" + getVY() + "]";
        //return ("[" +image + ";" + x + ";" + y + ";"+vx+";"+vy+";"+getWidth()+";"+getHeight()+";"+ username  +";"  + angle + ";"  + health + ";" + getProgramID()+ "]");
    }
}
