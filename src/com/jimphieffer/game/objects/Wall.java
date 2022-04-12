package com.jimphieffer.game.objects;

import com.jimphieffer.game.Sprite;

public class Wall extends Sprite {
    private double x;
    private double y;
    private int width;
    private int height;
    public Wall(double x, double y, int width, int height)
    {
        super(x, y,width,height,"/textures/wall.png");
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
