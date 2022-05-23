package com.jimphieffer.game;

import java.util.ArrayList;
import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import com.jimphieffer.game.objectTypes.Sprite;


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

    public Player(double x, double y, int width, int height, String image, UUID id,
                  double velocityX, double velocityY, String username)
    {
        super(x, y, width, height, image, id,velocityX,velocityY);
        vx = velocityX;
        vy = velocityY;
        health  = 100;
       this.username = username + id;
    }

    public Player(String x, String y, String width, String height, String image, String id,
                  String velocityX, String velocityY, String username)
    {
        super(x, y, width, height, image,id,velocityX,velocityY);
        vx = parseDouble(velocityX);
        vy = parseDouble(velocityY);
        health  = 100;
        this.username = username;

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
    public void setAttacking(){isAttacking=true;}
    public boolean isAttacking(){ return isAttacking;}

    public void step(ArrayList<Sprite> sprites)
    {


        for(Sprite s: sprites)
        {
            if(s instanceof Wall)
            {
                boolean right = (vx>0);
                boolean up = (vy>0);

                while(touchingAfterDisplacement(s,vx,0)) {
                    if(right)
                        vx-=0.1;
                    else
                        vx+=0.1;
                    //System.out.println("right o left");
                }
                while(touchingAfterDisplacement(s,0,vy)) {
                    if(up)
                        vy-=0.1;
                    else
                        vy+=0.1;
                    //System.out.println("up o down");
                }
            }
        }


        setX(getX()+vx);
        setY(getY()+vy);

        super.step();
    }

    public void step()
    {
       step(null);
    }


    public String toString()
    {
        return "[" + "PLAYER" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";"  + getVX() + ";" + getVY() + ";" + username + "]";
    }

}
