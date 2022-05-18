package com.jimphieffer.game;

import com.jimphieffer.game.objectTypes.*;

import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;

import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


import static java.lang.Double.*;
import static java.lang.Integer.*;


public class Player extends NonStatic{

    private double vx;
    private double vy;
    private int health;
    private int amtWood = 0;
    private String username;
    private float rotation;
    private boolean isAttacking;

    @JsonDefaultConstructor(names = {"x", "y", "width", "height", "image", "id", "vx", "vy", "username"})
    public Player(double x, double y, int width, int height, String image, UUID id,
                  double velocityX, double velocityY, String username)
    {
        super(x, y, width, height, image, id,velocityX,velocityY);
        vx = velocityX;
        vy = velocityY;
        health  = 100;
       this.username = username + id;
    }
    public Player() {
        super();
    }

    public void setUsername(String name)
    {
        username = name;
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
        return vx;
    }

    public void setVX(double velocityX)
    {
        vx = velocityX;
    }

    public int getHealth(){return health;}

    public String getUsername(){return username;}

    public String getClassType(){
        return "Player";
    }

    public void setHealth(int health){this.health=health;}

    public void setLocalRotation(float angle)
    {
        rotation=angle;
    }

    public float getLocalRotation()
    {
        return rotation;
    }

    public void setAmtWood(int wood){ amtWood=wood; }

    public int getAmtWood(){ return amtWood; }
    public void setAttacking(boolean attacking){isAttacking=attacking;}
    public boolean getAttacking(){ return isAttacking;}

    public void step()
    {
       /*
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












        */

        setX(getX()+vx);
        setY(getY()+vy);

        super.step();
    }
}
