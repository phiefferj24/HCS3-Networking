package com.jimphieffer.graphics.hud;

public class FloatRectangle {
    public float x1;
    public float y1;
    public float x2;
    public float y2;
    public FloatRectangle(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    public boolean contains(float x, float y) {
        if(y2 >= y1) return x >= x1 && x <= x2 && y >= y1 && y <= y2;
        else return x >= x1 && x <= x2 && y <= y1 && y >= y2;
    }
}
