package com.jimphieffer.game.objects;

public class Pig extends NonStatic{

    public Pig(String image, double x, double y, double vx, double vy, int width, int height, double angle, int health,  int programID) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        super(image, x, y, vx, vy, width, height, angle, health, programID);
    }
    /*
    public Pig(String image, String x, String y, String vx, String vy, String width, String height, String angle, String health,  String programID) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which one

        //super(image, parseDouble(x), parseDouble(y), parseDouble(vx), parseDouble(vy), parseInt(width), parseInt(height), image,parseInt(programID));
        //super(image, x, y, vx, vy, width, height, angle, health, programID);
    }

     */

    public String toString()
    {
        return null;
        //return ("[" +image + ";" + x + ";" + y + ";"+vx+";"+vy+";"+getWidth()+";"+getHeight()+";"+ username  +";"  + angle + ";"  + health + ";" + getProgramID()+ "]");
    }
}
