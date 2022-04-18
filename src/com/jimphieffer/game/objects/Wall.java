package com.jimphieffer.game.objects;

import com.jimphieffer.game.Sprite;
import java.util.Random;


public class Wall extends Sprite {
    private double x;
    private double y;
    private int width;
    private int height;
    private String username;
    public Wall(double x, double y, int width, int height,int programID)
    {
        super(x, y,width,height,"/textures/wall.png",programID );
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Random r = new Random();
        int low = 1;
        int high = 10000000;
        int result = r.nextInt(high-low) + low;
        username = "WALL" + result;
    }

    public String getUsername()
    {
        return username;
    }

    public String toString()
    {
        //TODO: TIKO IS THIS USERNAME CHANGE OKAY ON TOSTRING WHERE DO YOU USE THIS
        return "[" + username + ";" + x +";" + y + ";" + width + ";" + height + ";" + "/textures/wall.png" + "]";
    }

}
