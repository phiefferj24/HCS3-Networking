package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;

public class HUDElement {
    public Mesh mesh;
    public FloatRectangle bounds;

    public void hover() {

    }
    public void mousePressed(int button) {

    }
    public void mouseReleased(int button) {

    }
    public void keyPressed(int key) {

    }
    public void keyReleased(int key) {

    }
    public void selected() {

    }
    public void deselected() {

    }
    public void render() {
        mesh.render();
    }
}
