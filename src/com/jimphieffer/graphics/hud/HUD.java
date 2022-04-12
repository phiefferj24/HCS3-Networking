package com.jimphieffer.graphics.hud;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.elements.HUDElement;

import java.util.ArrayList;

public class HUD {
    private ArrayList<HUDElement> elements;
    public HUD(int programId) {
        elements = new ArrayList<>();
        //elements.add(new HUDElement());
        //elements.get(0).mesh = new Mesh(0, 0, 0, 0.1f, 0.1f, "/textures/edition.png", programId);
    }
    public void render() {
        elements.forEach(HUDElement::render);
    }
}
