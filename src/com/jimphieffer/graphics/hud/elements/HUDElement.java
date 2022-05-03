package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.game.Game;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class HUDElement {
    public Mesh mesh;
    public FloatRectangle bounds;
    protected int windowWidth;
    protected int windowHeight;
    protected double mouseX;
    protected double mouseY;
    protected HashMap<String, Game.Method> callbacks = new HashMap<>();

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
    public void charTyped(char c) {

    }
    public void render() {
        mesh.render();
    }

    public void setScreenSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }
    public void close() {}

    public void setCallback(String key, Game.Method method) {
        callbacks.put(key, method);
    }
}
