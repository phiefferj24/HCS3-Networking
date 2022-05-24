package com.jimphieffer.game.objects;

import com.jimphieffer.game.Player;
import com.jimphieffer.game.objectTypes.NonStatic;
import com.jimphieffer.game.objectTypes.Sprite;
import com.jimphieffer.utils.json.annotations.JsonDefaultConstructor;

import java.util.ArrayList;
import java.util.UUID;

public class Zombie extends NonStatic {
    private double angle = Math.random()*360;
    private int health;

    @JsonDefaultConstructor(names = {"x", "y", "width", "height", "image", "id", "vx", "vy", "health"})
    public Zombie(double x, double y, int width, int height, String image, UUID id,
                  double vx, double vy, int health) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, width, height, image, id, vx, vy);
        this.health = health;
    }
    public Zombie(double x, double y) {
        //for any program id do Animal#### and the numbers will be from where we render it go thru list
        //of all sprites and then compute which on
        super(x, y, 50, 50, "/textures/zombie.png", UUID.randomUUID(), 0, 0);
        this.health = health;
    }

    public Zombie() {
        super();
        setImage("/textures/zombie.png");
    }



    public int getHealth(){return health;}


    public void step(ArrayList<Sprite> sprites)
    {
        double vx = getVX();
        double vy = getVY();
        for (Sprite s : sprites) {
            if (s instanceof Wall) {
                boolean right = (vx > 0);
                boolean up = (vy > 0);


                while (touchingAfterDisplacement(s, vx, 0)) {
                    if (right)
                        vx -= 0.1;
                    else
                        vx += 0.1;
                }
                while (touchingAfterDisplacement(s, 0, vy)) {
                    if (up)
                        vy -= 0.1;
                    else
                        vy += 0.1;
                }
            }
            else if (s instanceof Player)
            {
                if(distanceTo(s)<500)
                {
                    moveTo(s,10);
                    //System.out.println("sees");
                }
            }
        }
        step();

    }

    public void step()
    {
        //setX(getX()+2);
        super.step();
    }

    public String getClassType(){
        return "Zombie";
    }

}
