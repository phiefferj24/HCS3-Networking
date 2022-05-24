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
        super(x, y, 15, 15, "/textures/zombie.png", UUID.randomUUID(), 0, 0);
        this.health = health;
    }

    public Zombie() {
        super();
        setImage("/textures/zombie.png");
    }



    public int getHealth(){return health;}


    public void step(ArrayList<Sprite> sprites)
    {
        for (Sprite s : sprites) {
            if (s instanceof Wall) {

            }
            else if (s instanceof Player)
            {

            }
        }

    }

    public void step()
    {
        double xMov = Math.cos(angle)*3;
        double yMov = Math.sin(angle)*3;
        angle+=.02;
        setX(getX()+xMov);
        setY(getY()+yMov);

        //setX(getX()+2);

        super.step();
    }
}
