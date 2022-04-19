package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;

public class HUDElement {
    public Mesh mesh;
    public FloatRectangle bounds;
    protected int windowWidth;
    protected int windowHeight;
    protected double mouseX;
    protected double mouseY;

    public void mousePressed(int button) {

    }
    public void mouseReleased(int button) {

    }
    public void keyPressed(int key) {

    }
    public void keyReleased(int key) {

    }
    public void mouseMoved(double x, double y) {

    }
    public void render() {
        mesh.render();
    }

    public void setScreenSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }
}
