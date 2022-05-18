package com.jimphieffer.game;

import com.jimphieffer.game.Game;
import com.jimphieffer.game.Sprite;
import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;

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
    @JsonDefaultConstructor(names = {"x", "y", "width", "height", "image", "id", "vx", "vy"})
    public NonStatic(double x, double y, int width, int height, String image, UUID id,
                     double vx, double vy)
    {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        super(x, y,width,height,image,id);
        this.image=image;
        this.health  = health;
        this.x=x;
        this.y=y;
        this.vx = vx;
        this.vy=vy;
        this.image = image;
    }
    public NonStatic() {
        super();
    }


    public double getVY()
    {

        return vy;
    }

    public String getClassType(){
        return "NonStatic";
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

    public void changeAll(String x, String y,String vx,String vy)
    {
        setX(parseDouble(x));
        setY(parseDouble(y));
        setVX(parseDouble(vx));
        setVY(parseDouble(vy));
    }


    public void setHealth(int health){this.health=health;}

    public double getAngle()
    {
        return angle;
    }

    public void step()
    {
        //dont put it here specify in lowest order class
    }

    //String image, double x, double y, int width, int height, double angle, int health,  int programID)
}
