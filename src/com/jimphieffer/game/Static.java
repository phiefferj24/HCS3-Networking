package com.jimphieffer.game;


import com.jimphieffer.graphics.Mesh;

import java.util.UUID;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class Static extends com.jimphieffer.game.objectTypes.Sprite {

    public Static(double x, double y, int width, int height, String image, UUID id) //Exact same as sprite
    {
        super(x,y,width,height,image,id);

    }
    public Static(String x, String y, String width, String height, String image, String id )
    {
        super(x,y,width,height,image,id);


    }

    public void changeAll(String x, String y) {
        setX(parseDouble(x));
        setY(parseDouble(y));
    }

    public String getClassType(){
        return "Static";
    }
    public void step()
    {
        super.step();
    }

    //public String getType() { return type;}



    //(String image, double x, double y, int width, int height, double angle, int programID)
    public String toString()
    {
        return "[" + "STATIC" +";" + getX() +";" + getY() + ";" + getWidth() + ";" + getHeight() + ";" + getImage() + ";" + getID().toString() + ";" + "]";
    }

}
