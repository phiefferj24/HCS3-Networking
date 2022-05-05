package com.jimphieffer.graphics.hud;

import com.jimphieffer.game.Camera;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.elements.HUDButton;
import com.jimphieffer.graphics.hud.elements.HUDElement;
import com.jimphieffer.graphics.hud.elements.HUDTextBox;

import java.util.ArrayList;

public class HUD {
    public ArrayList<HUDElement> elements;
    private double mouseX = 0;
    private double mouseY = 0;
    private int windowWidth;
    private int windowHeight;
    public Camera camera;
    public boolean visible;
    public HUD(int programId, int windowWidth, int windowHeight, boolean visible) {
        elements = new ArrayList<>();
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        camera = new Camera(windowWidth, windowHeight);
        this.visible = visible;
    }
    public void render() {
        if(visible) elements.forEach(HUDElement::render);
    }
    public void mouseMoved(double x, double y) {
        mouseX = x * 2 - windowWidth;
        mouseY = -y * 2 + windowHeight;
        //print camera's projection matrix width and height
//        mouseX = x / (windowWidth / 2.) - 1;
//        mouseY = - y / (windowHeight / 2.) + 1;
        if(visible) elements.forEach(hudElement -> hudElement.mouseMoved(mouseX, mouseY));
    }
    public void setScreenSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        camera.setScreenSize(width, height);
        elements.forEach(hudElement -> hudElement.setScreenSize(width, height));
    }
    public void mousePressed(int button) {
        if(visible) elements.forEach(hudElement -> hudElement.mousePressed(button));
    }
    public void mouseReleased(int button) {
        if(visible) elements.forEach(hudElement -> hudElement.mouseReleased(button));
    }
    public void keyPressed(int key) {
        if(visible) elements.forEach(hudElement -> hudElement.keyPressed(key));
    }
    public void keyReleased(int key) {
        if(visible) elements.forEach(hudElement -> hudElement.keyReleased(key));
    }
    public void charTyped(char c) {
        if(visible) elements.forEach(hudElement -> hudElement.charTyped(c));
    }
    public void close() { elements.forEach(HUDElement::close); }
}
