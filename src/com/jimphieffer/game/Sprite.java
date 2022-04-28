package com.jimphieffer.game;

import com.jimphieffer.graphics.Mesh;

import java.util.UUID;

import static java.lang.Double.*;
import static java.lang.Integer.*;

public class Sprite
{
    private double x;  //the x-coordinate of the left edge of the sprite
    private double y;   //the y-coordinate of the top edge of the sprite
    private int width;
    private int height;
    private int programID;
    private String image;
    private UUID id;
    public Mesh mesh;

    public Sprite(double theLeft, double theTop, int theWidth, int theHeight, String theImage, int programID)
    {
        this.programID = programID;
        x = theLeft;
        y = theTop;
        width = theWidth;
        height = theHeight;
        setImage(theImage);
        if(id == null)
            this.id = UUID.randomUUID();
        else
            this.id = id;
        if(programID != 0) mesh = new Mesh((float)x,(float)y,0,width,height,image,programID);
    }
    public Sprite(String x, String y, String width, String height, String theImage, String id ,String programID)
    {
        this.programID = parseInt(programID);
        this.x = parseDouble(x);
        this.y = parseDouble(y);
        this.width = parseInt(width);
        this.height = parseInt(height);
        this.programID = parseInt(programID);
        setImage(theImage);
        this.id = UUID.fromString(id);
        if(this.programID != 0) mesh = new Mesh((float)this.x,(float)this.y ,0,this.width,this.height,image,this.programID);
    }

    public Sprite() {

    }


    public boolean touching(double x, double y)
    {
        return x> this.x && x< this.x +width && y> this.y && y< this.y + height;
    }

    public boolean touchingAfterDisplacement(double x, double y, double dx, double dy)
    {
        return x> this.x +dx && x< this.x +width+dx && y> this.y +dy && y< this.y + height+dy ;
    }

    public boolean touching(Sprite s)
    {
        double scale = 0.2;
        double sRight = (s.getX() + s.getWidth()) - s.getWidth()*scale;
        double sBottom = s.getY() + s.getHeight() - s.getHeight()*scale;
        if (touching(s.getX() + s.getWidth()*scale, s.getY() + s.getHeight()*scale))
            return true;
        if (touching(s.getX() + s.getWidth()*scale,sBottom))
            return true;
        if (touching(sRight,sBottom))
            return true;
        if (touching(sRight,s.getY() + s.getHeight()*scale))
            return true;
        return false;
    }

    public void deactivate()
    {
        setHeight(0);
        setX(10000);
        setY(10000);
    }

    public boolean touching(Sprite s, double scale)
    {
        double sRight = (s.getX() + s.getWidth()) - s.getWidth()*scale;
        double sBottom = s.getY() + s.getHeight() - s.getHeight()*scale;
        if (touching(s.getX() + s.getWidth()*scale, s.getY() + s.getHeight()*scale))
            return true;
        if (touching(s.getX() + s.getWidth()*scale,sBottom))
            return true;
        if (touching(sRight,sBottom))
            return true;
        if (touching(sRight,s.getY() + s.getHeight()*scale))
            return true;



        return false;
    }

    public boolean touchingAfterDisplacement(Sprite s, double dx, double dy) {
        double scale = 0.2;
        double sLeft = s.getX();
        double sTop = s.getY();
        double sRight = (s.getX() + s.getWidth());
        double sBottom = s.getY() + s.getHeight();

        if (touchingAfterDisplacement(sLeft,sTop,dx,dy))
            return true;
        if (touchingAfterDisplacement(sLeft,sBottom,dx,dy))
            return true;
        if (touchingAfterDisplacement(sRight,sTop,dx,dy))
            return true;
        if (touchingAfterDisplacement(sRight,sBottom,dx,dy))
            return true;

        if (touchingAfterDisplacement(sLeft + s.getWidth()/2,sTop,dx,dy))
            return true;
        if (touchingAfterDisplacement(sLeft,sTop + s.getHeight()/2,dx,dy))
            return true;

        if (touchingAfterDisplacement(sLeft+ s.getWidth()/2,sBottom,dx,dy))
            return true;
        if (touchingAfterDisplacement(sRight,sTop+s.getHeight()/2,dx,dy))
            return true;

        return false;
    }


    public double getX()
    {
        return x;
    }

    public void setX(double l)
    {
        x = l;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double t)
    {
        y = t;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int w)
    {
        width = w;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int h)
    {
        height = h;
    }

    public UUID getID()
    {
        return id;
    }

    public void setID(UUID id)
    {
        this.id = id;
    }

    public String getImage()
    {
        return image;
    }

    public int getProgramID()
    {
        return programID;
    }

    public void setImage(String i)
    {
        image = i;
    }

    public void step(Game game)
    {
        mesh.translate((float) x, (float) y, 0);
        //do NOT insert any code here
    }

    public String toString()
    {
        return "[" + "SPRITE" +";" + x +";" + y + ";" + width + ";" + height + ";" + image + ";" +id.toString()+ ";" + programID + "]";
    }

    /*
    public static Sprite stringToSprite(String s)
    {
        s= s.substring(1,s.length()-1);
        String[] onGuh = s.split(";");
        switch(onGuh[0])
        {
            case "PLAYER": return new Player(parseDouble(onGuh[1]),parseDouble(onGuh[2]),parseInt(onGuh[3]),parseInt(onGuh[4]),onGuh[5],
                                            parseInt(onGuh[6]),parseDouble(onGuh[7]),parseDouble(onGuh[8]),onGuh[9]);
            case "ANIMAL": return null; //new Animal(onGuh[1],parseDouble(onGuh[1]),parseDouble(onGuh[2]),parseInt(onGuh[3]),parseInt(onGuh[4],onGuh[5], parseInt(onGuh[6])))
            case "WALL": return null; // TODO
            default: return new Sprite(parseDouble(onGuh[1]),parseDouble(onGuh[2]),parseInt(onGuh[3]),parseInt(onGuh[4]),onGuh[5], parseInt(onGuh[6]));
    
        }

        //[" + ID + ";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";"+ getProgramID() + "]";
    }

     */

    public static Sprite stringToSprite(String s)
    {
        s= s.substring(1,s.length()-1);
        String[] onGuh = s.split(";");
        switch(onGuh[0])
        {
            case "PLAYER": return new Player(onGuh[1],onGuh[2],onGuh[3],onGuh[4],onGuh[5],onGuh[6],onGuh[7],onGuh[8], onGuh[9],onGuh[10]);
            default: return new Sprite(onGuh[1],onGuh[2],onGuh[3],onGuh[4],onGuh[5],onGuh[6],onGuh[7]);

        }
    }
}
