package com.jimphieffer.graphics.hud;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.elements.HUDButton;
import com.jimphieffer.graphics.hud.elements.HUDElement;

import java.util.ArrayList;

public class HUD {
    private ArrayList<HUDElement> elements;
    private double mouseX = 0;
    private double mouseY = 0;
    private int windowWidth;
    private int windowHeight;
    public HUD(int programId, int windowWidth, int windowHeight) {
        elements = new ArrayList<>();
        elements.add(new HUDButton(new Mesh(0, 0, 0, 0.1f, 0.1f, "/textures/edition.png", programId), 0, 0, 0, 0, windowWidth, windowHeight));
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    public void render() {
        elements.forEach(HUDElement::render);
    }
    public void mouseMoved(double x, double y) {
        mouseX = x / (windowWidth / 2.) - 1;
        mouseY = - y / (windowHeight / 2.) + 1;
        elements.forEach(hudElement -> hudElement.mouseMoved(mouseX, mouseY));
    }
    public void setScreenSize(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        elements.forEach(hudElement -> hudElement.setScreenSize(width, height));
    }
}
