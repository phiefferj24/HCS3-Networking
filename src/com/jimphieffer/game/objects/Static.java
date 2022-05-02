package com.jimphieffer.game.objects;

import com.jimphieffer.game.Sprite;
import com.jimphieffer.graphics.Mesh;

import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class Static extends Sprite {

    public Static(double x, double y, int width, int height, String image, UUID id, int programID) //Exact same as sprite
    {
        super(x,y,width,height,image,id,programID);

    }
    public Static(String x, String y, String width, String height, String image, String id ,String programID)
    {
        super(x,y,width,height,image,id,programID);


    }

    //public String getType() { return type;}



    //(String image, double x, double y, int width, int height, double angle, int programID)
    public String toString()
    {
        return "[" + "NONSTATIC" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";" +  getProgramID()+ "]";
    }

}
