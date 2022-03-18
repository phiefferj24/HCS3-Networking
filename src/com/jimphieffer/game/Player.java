package com.jimphieffer.game;

import java.util.ArrayList;

public class Player extends Sprite{

    private double vx;
    private double vy;
    private int health;
    private int amtStone = 0;
    private String username;

    public Player(double left, double top, int width, int height, String image,
                  double velocityX, double velocityY, String username)
    {
        super(left, top, width, height, image);
        vx = velocityX;
        vy = velocityY;
        health  = 15;
        this.username = username;

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

    public void setHealth(int health){this.health=health;}

    public void step(Game game)
    {


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

        if (getLeft() < -game.getWindowWidth())
            vx = Math.abs(vx);
        if (getLeft() + getWidth() > game.getWindowWidth())
            vx = -Math.abs(vx);
        if (getTop() < -game.getWindowHeight())
            vy = Math.abs(vy);
        if (getTop() + getHeight() >game.getWindowHeight())
            vy = -Math.abs(vy);














        setLeft(getLeft()+vx);
        setTop(getTop()+vy);


    }



}
