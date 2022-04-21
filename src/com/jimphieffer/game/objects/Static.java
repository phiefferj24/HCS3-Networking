package com.jimphieffer.game.objects;

import com.jimphieffer.game.Sprite;


public class Static extends Sprite {
    private double x;
    private double y;
    private int width;
    private int height;
    private String username;
    private Double angle;
    private String image;
//TODO:CHANGE WALL WITH NON-STATIC AND EXTEND THIS
    public Static(String image, double x, double y, int width, int height, double angle, int programID)
    {
        super(x, y,width,height,image,programID );
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle=angle;
        username = "Static" + this.hashCode();
    }

    //public String getType() { return type;}

    public String getUsername()
    {
        return username;
    }

    public String toString()
    {
        return ("[" + username + ";" + x +";" + y + ";"  + angle + ";" + image + "]");
    }

}
