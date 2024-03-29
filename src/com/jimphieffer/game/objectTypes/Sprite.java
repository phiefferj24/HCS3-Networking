package com.jimphieffer.game.objectTypes;

import com.jimphieffer.game.Game;
import com.jimphieffer.game.objects.Pig;
import com.jimphieffer.game.objects.Tree;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;
import com.jimphieffer.utils.json.annotations.JsonEquivalent;
import com.jimphieffer.utils.json.annotations.JsonIgnore;

import java.util.ArrayList;
import java.util.UUID;

import static java.lang.Double.*;
import static java.lang.Integer.*;

public abstract class Sprite
{
    private double x;  //the x-coordinate of the left edge of the sprite
    private double y;   //the y-coordinate of the top edge of the sprite
    private int width;
    private int height;

    private String image;
    private UUID id;
    @JsonIgnore
    public Mesh mesh;

    @JsonDefaultConstructor(names = {"x", "y", "width", "height", "image", "id"})
    public Sprite(double x, double y, int theWidth, int theHeight, String theImage, UUID id)
    {

        this.x = x;
        this.y = y;
        width = theWidth;
        height = theHeight;
        setImage(theImage);
        if(id == null)
            this.id = UUID.randomUUID();
        else
            this.id = id;
        //if(Game.objectProgramId != 0 && Thread.currentThread().getName().equals("main")) mesh = new Mesh((float)x,(float)y,0,width,height,image,Game.objectProgramId);
    }

    public Sprite() {

    }


    public boolean touching(double x, double y)
    {
        return x> this.x && x< this.x +width && y> this.y && y< this.y + height;
    }

    public boolean touchingAfterDisplacement(double x, double y, double dx, double dy)
    {
        double s = 1.5;
        double x1 = this.x+width/2-width*s;
        double x2 = this.x+width/2+width*s;
        double y1 = this.y+height/2-width*s;
        double y2 = this.x+height/2+width*s;
        //return x> x1 +dx && x< x2+dx && y> y1 +dy && y< y2+dy;
        return x> this.x +dx && x< this.x +width+dx && y> this.y +dy && y< this.y + height+dy ;
    }

    public boolean touching(Sprite s)
    {
        double scale = 1;
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

        //TODO: here is collision

        double sLeft = s.getX();
        double sTop =  s.getY();
        double sRight =(s.getX() + s.mesh.getActualWidth());
        double sBottom =(s.getY() + s.mesh.getActualHeight());

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

    public String getClassType(){
        return "Sprite";
    }

    public void setID(UUID id)
    {
        this.id = id;
    }

    public String getImage()
    {
        return image;
    }

    public UUID getUUID()
    {
        return id;
    }

    public void setImage(String i)
    {
        if(i.equals(image)) return;
        image = i;
    }

    public void open() {
        if(mesh != null) mesh.close();
        if(Game.objectProgramId != 0 && Thread.currentThread().getName().equals("main")) mesh = new Mesh((float)this.x,(float)this.y ,-1.f,this.width,this.height,image,Game.objectProgramId);
    }

    public void step(int n) {
        for(int i = 0; i<n;i++)
            step();
    }

    public void step(int n, ArrayList<Sprite> sprites) {
        for(int i = 0; i<n;i++)
            step(sprites);
    }

    public void step(ArrayList<Sprite> sprites) {
        step();
    }

    public void step() {
        if (mesh != null)
            mesh.setPosition((float) x, (float) y, 0);
        //do NOT insert any code here
    }

    public double distanceTo(double x, double y)
    {

        return Math.sqrt(Math.pow(this.x-x,2)+Math.pow(this.y-y,2));
    }

    public double distanceTo(Sprite s)
    {
        return distanceTo(s.getX(),s.getY());
    }

}
