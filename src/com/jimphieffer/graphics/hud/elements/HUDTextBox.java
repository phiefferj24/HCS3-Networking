package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;
import com.jimphieffer.graphics.hud.TextMesh;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class HUDTextBox extends HUDElement {

    private String text = "";
    private Mesh selectedMesh;
    private Mesh hoverMesh;
    private boolean selected = false;
    private boolean hovered = false;
    private ArrayList<TextMesh> textMeshes = new ArrayList<>();
    private String font;
    private int programId;

    public HUDTextBox(Mesh mesh, Mesh hoverMesh, Mesh selectedMesh, int windowWidth, int windowHeight, int programId, String font) {
        super.mesh = mesh;
        this.hoverMesh = hoverMesh;
        this.selectedMesh = selectedMesh;
        super.bounds = new FloatRectangle(mesh.x - mesh.width, mesh.y - mesh.height, mesh.x + mesh.width, mesh.y + mesh.height);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.programId = programId;
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void render() {
        if(selected) {
            selectedMesh.render();
        } else if(hovered) {
            hoverMesh.render();
        } else {
            mesh.render();
        }
        textMeshes.forEach(Mesh::render);
    }

    @Override
    public void mousePressed(int button) {
        if(button == GLFW_MOUSE_BUTTON_LEFT) {
            selected = hovered;
        }
    }

    @Override
    public void mouseMoved(double x, double y) {
        hovered = bounds.contains((float) mouseX, (float) mouseY);
    }
    public void charTyped(char c) {
        if(selected) {
            setText(getText() + c);
            float x = mesh.x + (mesh.height * text.length()) / 2 - mesh.height / 2;
            float y = mesh.y;
            textMeshes.forEach(mesh -> {
                mesh.translate(-mesh.height, 0, 0);
            });
            textMeshes.add(new TextMesh(c, font, x, y, 0.5f, mesh.height/2, mesh.height/2, programId));
        }
    }

    public void keyPressed(int key) {
        if (selected) {
            if (key == GLFW_KEY_BACKSPACE || key == GLFW_KEY_DELETE) {
                if (text.length() > 0) {
                    setText(getText().substring(0, getText().length() - 1));
                    textMeshes.remove(textMeshes.size() - 1);
                    textMeshes.forEach(mesh -> mesh.translate(mesh.height, 0, 0));
                }
            }
        }
    }
    public void close() {
        textMeshes.forEach(Mesh::close);
        mesh.close();
        selectedMesh.close();
        hoverMesh.close();
    }
}
