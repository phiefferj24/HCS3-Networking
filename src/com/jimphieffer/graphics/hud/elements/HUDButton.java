package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;


public class HUDButton extends HUDElement {
    public HUDButton(Mesh mesh, float x1, float y1, float x2, float y2, int windowWidth, int windowHeight) {
        super.mesh = mesh;
        super.bounds = new FloatRectangle(x1, y1, x2, y2);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    public void mouseMoved(double x, double y) {
        System.out.println(x + " " + y + " is stupid");
        mouseX = x;
        mouseY = y;
        System.out.println(mouseX + " " + mouseY);
        if (bounds.contains((float)mouseX, (float)mouseY)) {
            System.out.println("Mouse is in bounds");
        }
    }
}
