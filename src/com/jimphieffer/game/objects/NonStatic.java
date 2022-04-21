package com.jimphieffer.game.objects;

import com.jimphieffer.game.Game;
import com.jimphieffer.game.Sprite;

public class NonStatic extends Sprite {
    private double vx;
    private double vy;
    private double x;
    private double y;
    private int health;
    private String username;
    private double angle;
    private String image;

    //TODO: change change animal to static and extend it
    public NonStatic(String image, double x, double y, int width, int height, double angle, int health,  int programID)
    {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        super(x, y,width,height,image,programID);
        this.image=image;
        this.health  = health;
        this.x=x;
        this.y=y;
        vx = 0.0;
        vy=0.0;
        this.angle=angle;
        username = "NonStatic" + this.hashCode();//change animal to NON-STATIC

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
        double tempVX = theta;
        double tempVY = 3 * Math.sin(theta / Math.PI);
        vx=tempVX;
        vy=tempVY;
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

         */

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

    public String toString(){
        return ("[" + username + ";" + x +";" + y + ";"  + angle + ";" + image + ";" + vx + ";" + vy + ";" + health + "]");
    }
}
