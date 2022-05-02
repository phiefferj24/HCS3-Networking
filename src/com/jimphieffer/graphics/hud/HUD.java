package com.jimphieffer.graphics.hud;

import com.jimphieffer.game.Camera;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.elements.HUDButton;
import com.jimphieffer.graphics.hud.elements.HUDElement;
import com.jimphieffer.graphics.hud.elements.HUDTextBox;

import java.util.ArrayList;

public class HUD {
    private ArrayList<HUDElement> elements;
    private double mouseX = 0;
    private double mouseY = 0;
    private int windowWidth;
    private int windowHeight;
    public Camera camera;
    public boolean visible = false;
    public HUD(int programId, int windowWidth, int windowHeight) {
        elements = new ArrayList<>();
        elements.add(new HUDTextBox(
                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 66/256.f, 200/256.f, 86/256.f),
                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 86/256.f, 200/256.f, 106/256.f),
                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 46/256.f, 200/256.f, 66/256.f),
                windowWidth, windowHeight, programId, "/fonts/minecraft.png"));
//        elements.add(new HUDButton(
//                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 66/256.f, 200/256.f, 86/256.f),
//                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 86/256.f, 200/256.f, 106/256.f),
//                new Mesh(0, 0, 0.f, 500f, 50f, "/textures/widgets.png", programId, 0, 46/256.f, 200/256.f, 66/256.f),
//                windowWidth, windowHeight, programId, "/fonts/minecraft.png", "Button"));
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        camera = new Camera(windowWidth, windowHeight);
    }
    public void render() {
        if(visible) elements.forEach(HUDElement::render);
    }
    public void mouseMoved(double x, double y) {
        mouseX = x / (windowWidth / 2.) - 1;
        mouseY = - y / (windowHeight / 2.) + 1;
        elements.forEach(hudElement -> hudElement.mouseMoved(mouseX, mouseY));
    }
    public void setScreenSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        camera.setScreenSize(width, height);
        elements.forEach(hudElement -> hudElement.setScreenSize(width, height));
    }
    public void mousePressed(int button) {
        elements.forEach(hudElement -> hudElement.mousePressed(button));
    }
    public void mouseReleased(int button) {
        elements.forEach(hudElement -> hudElement.mouseReleased(button));
    }
    public void keyPressed(int key) {
        elements.forEach(hudElement -> hudElement.keyPressed(key));
    }
    public void keyReleased(int key) {
        elements.forEach(hudElement -> hudElement.keyReleased(key));
    }
    public void charTyped(char c) {
        elements.forEach(hudElement -> hudElement.charTyped(c));
    }
    public void close() { elements.forEach(HUDElement::close); }
}
