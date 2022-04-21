package com.jimphieffer.game.objects;

import com.jimphieffer.game.Sprite;
import java.util.Random;


public class Wall extends Sprite {
    private double x;
    private double y;
    private int width;
    private int height;
    private String username;
    private Double angle;
//TODO:CHANGE WALL WITH NON-STATIC AND EXTEND THIS
    public Wall(double x, double y, int width, int height, double angle, int programID)
    {
        super(x, y,width,height,"/textures/wall.png",programID );
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle=angle;
        username = "WALL" + this.hashCode();
    }

    //public String getType() { return type;}

    public String getUsername()
    {
        return username;
    }

    public String toString()
    {
        //TODO: TIKO IS THIS USERNAME CHANGE OKAY ON TOSTRING WHERE DO YOU USE THIS
      return "gay";
    }

}
