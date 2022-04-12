package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;


public class HUDButton extends HUDElement {
    public HUDButton(Mesh mesh, float x1, float y1, float x2, float y2, float z) {
        super.mesh = mesh;
        super.bounds = new FloatRectangle(x1, y1, x2, y2);
    }
}
