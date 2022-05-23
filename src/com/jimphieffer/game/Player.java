package com.jimphieffer.game;

import com.jimphieffer.game.objectTypes.*;

import com.jimphieffer.game.objects.Wall;
import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;

import java.util.ArrayList;
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
       this.username = username;
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


    public void step(ArrayList<Sprite> sprites)
    {
        for (Sprite s : sprites) {
            if (s instanceof Wall) {

                boolean right = (vx > 0);
                boolean up = (vy > 0);


                while (touchingAfterDisplacement(s, vx, 0)) {
                    System.out.println("touching");
                    if (right)
                        vx -= 0.1;
                    else
                        vx += 0.1;
                }
                while (touchingAfterDisplacement(s, 0, vy)) {
                    System.out.println("touching");
                    if (up)
                        vy -= 0.1;
                    else
                        vy += 0.1;
                }
            }
        }

        setX(getX()+vx);
        setY(getY()+vy);
    }

    public void step()
    {
        setX(getX()+vx);
        setY(getY()+vy);
        super.step();
    }
}
