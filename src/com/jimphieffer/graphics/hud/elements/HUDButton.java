package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;


public class HUDButton extends HUDElement {
    private boolean isHovered;
    private Mesh hoverMesh;
    public HUDButton(Mesh mesh, Mesh hoverMesh, int windowWidth, int windowHeight) {
        super.mesh = mesh;
        this.hoverMesh = hoverMesh;
        super.bounds = new FloatRectangle(mesh.x - mesh.width, mesh.y - mesh.height, mesh.x + mesh.width, mesh.y + mesh.height);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }
    public void mouseMoved(double x, double y) {
        mouseX = x;
        mouseY = y;
        isHovered = bounds.contains((float)x, (float)y);
    }
    public void render() {
        if(isHovered) hoverMesh.render();
        else mesh.render();
    }
}
