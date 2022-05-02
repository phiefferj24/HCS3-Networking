package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;
import com.jimphieffer.graphics.hud.TextMesh;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;


public class HUDButton extends HUDElement {
    private boolean hovered;
    private boolean selected;
    private Mesh hoverMesh;
    private Mesh selectedMesh;
    private ArrayList<Mesh> textMeshes;
    private String text;
    private int programId;
    public HUDButton(Mesh mesh, Mesh hoverMesh, Mesh selectedMesh, int windowWidth, int windowHeight, int programId, String font, String text) {
        super.mesh = mesh;
        this.hoverMesh = hoverMesh;
        this.selectedMesh = selectedMesh;
        super.bounds = new FloatRectangle(mesh.x - mesh.width, mesh.y - mesh.height, mesh.x + mesh.width, mesh.y + mesh.height);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.text = text;
        this.programId = programId;
        textMeshes = new ArrayList<>();
        for(int i = 0; i < text.length(); i++) {
            float x = mesh.x + (mesh.height * this.text.length()) / 2 - mesh.height / 2;
            float y = mesh.y;
            textMeshes.forEach(mesh1 -> mesh1.translate(-mesh1.height, 0, 0));
            textMeshes.add(new TextMesh(text.charAt(i), font, x, y, 0.5f, mesh.height/2, mesh.height/2, programId));
        }
    }
    public void mouseMoved(double x, double y) {
        mouseX = x;
        mouseY = y;
        hovered = bounds.contains((float)x, (float)y);
    }
    public void mousePressed(int button) {
        if(button == GLFW_MOUSE_BUTTON_LEFT) {
            selected = hovered;
        }
    }
    public void render() {
        if (selected) selectedMesh.render();
        else if(hovered) hoverMesh.render();
        else mesh.render();
    }

    public void close() {
        textMeshes.forEach(Mesh::close);
        mesh.close();
        selectedMesh.close();
        hoverMesh.close();
    }
}
