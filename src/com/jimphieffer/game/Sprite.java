package com.jimphieffer.game;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;

public class Sprite
{
    private double left;  //the x-coordinate of the left edge of the sprite
    private double top;   //the y-coordinate of the top edge of the sprite
    private int width;
    private int height;
    private String image;

    public Sprite(double theLeft, double theTop, int theWidth, int theHeight, String theImage)
    {
        left = theLeft;
        top = theTop;
        width = theWidth;
        height = theHeight;
        setImage(theImage);
    }

    public Sprite() {

    }

    public boolean touching(double x, double y)
    {
        return x>left && x<left+width && y>top && y<top + height;
    }

    public boolean touchingAfterDisplacement(double x, double y, double dx, double dy)
    {
        return x>left+dx && x<left+width+dx && y>top+dy && y<top + height+dy ;
    }

    public boolean touching(Sprite s)
    {
        double scale = 0.2;
        double sRight = (s.getLeft() + s.getWidth()) - s.getWidth()*scale;
        double sBottom = s.getTop() + s.getHeight() - s.getHeight()*scale;
        if (touching(s.getLeft() + s.getWidth()*scale, s.getTop() + s.getHeight()*scale))
            return true;
        if (touching(s.getLeft() + s.getWidth()*scale,sBottom))
            return true;
        if (touching(sRight,sBottom))
            return true;
        if (touching(sRight,s.getTop() + s.getHeight()*scale))
            return true;
        return false;
    }

    public void deactivate()
    {
        setHeight(0);
        setLeft(10000);
        setTop(10000);
    }

    public boolean touching(Sprite s, double scale)
    {
        double sRight = (s.getLeft() + s.getWidth()) - s.getWidth()*scale;
        double sBottom = s.getTop() + s.getHeight() - s.getHeight()*scale;
        if (touching(s.getLeft() + s.getWidth()*scale, s.getTop() + s.getHeight()*scale))
            return true;
        if (touching(s.getLeft() + s.getWidth()*scale,sBottom))
            return true;
        if (touching(sRight,sBottom))
            return true;
        if (touching(sRight,s.getTop() + s.getHeight()*scale))
            return true;



        return false;
    }

    public boolean touchingAfterDisplacement(Sprite s, double dx, double dy) {
        double scale = 0.2;
        double sLeft = s.getLeft();
        double sTop = s.getTop();
        double sRight = (s.getLeft() + s.getWidth());
        double sBottom = s.getTop() + s.getHeight();

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


    public double getLeft()
    {
        return left;
    }

    public void setLeft(double l)
    {
        left = l;
    }

    public double getTop()
    {
        return top;
    }

    public void setTop(double t)
    {
        top = t;
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

    public String getImage()
    {
        return image;
    }

    public void setImage(String i)
    {
        image = i;
    }

    public void step(Game game)
    {
        //do NOT insert any code here
    }
}
