package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.game.Game;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;
import com.jimphieffer.graphics.hud.TextMesh;
import com.jimphieffer.utils.TextUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import static org.lwjgl.glfw.GLFW.*;


public class HUDButton extends HUDElement {
    public boolean hovered;
    private boolean callbackForHover = false;
    public boolean selected;
    private boolean callbackForSelect = false;
    private Mesh hoverMesh;
    private Mesh selectedMesh;
    private ArrayList<TextMesh> textMeshes;
    private String text;
    private int programId;
    private boolean holdsState;
    private TextUtilities textUtilities;
    public HUDButton(Mesh mesh, Mesh hoverMesh, Mesh selectedMesh, int windowWidth, int windowHeight, int programId, String font, String text, boolean holdsState) {
        super.mesh = mesh;
        this.hoverMesh = hoverMesh;
        this.selectedMesh = selectedMesh;
        super.bounds = new FloatRectangle(mesh.x - mesh.width, mesh.y + mesh.height, mesh.x + mesh.width, mesh.y - mesh.height);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.text = text;
        this.programId = programId;
        textMeshes = new ArrayList<>();
        this.textUtilities = new TextUtilities(font, 1);
        for(int i = 0; i < text.length(); i++) {
            float x = mesh.x + TextUtilities.getCombinedWidth(text, textUtilities) / 16 * mesh.height;
            float y = mesh.y;
            char c = text.charAt(i);
            textMeshes.forEach(mesh1 -> {
                mesh1.translate(-textUtilities.getWidth(c)/8*this.mesh.height/2, 0, 0);
            });
            textMeshes.add(new TextMesh(c, font, x, y, 0.5f, mesh.height/2, textUtilities, programId));
        }
        this.holdsState = holdsState;
    }
    public void mouseMoved(double x, double y) {
        mouseX = x;
        mouseY = y;
        hovered = bounds.contains((float)x, (float)y);
        if (callbacks.get("mouseMoved") != null) callbacks.get("mouseMoved").run();
        if (!callbackForHover && hovered) {
            if(callbacks.get("hovered") != null) callbacks.get("hovered").run();
            callbackForHover = true;
        }
        if (callbackForHover && !hovered) {
            if(callbacks.get("unhovered") != null) callbacks.get("unhovered").run();
            callbackForHover = false;
        }
    }
    public void mousePressed(int button) {
        if(button == GLFW_MOUSE_BUTTON_LEFT) {
            selected = hovered;
        }
        if (callbacks.get("mousePressed") != null) callbacks.get("mousePressed").run();
        if (!callbackForSelect && selected) {
            if(callbacks.get("selected") != null) callbacks.get("selected").run();
            callbackForSelect = true;
        }
        if (callbackForSelect && !selected) {
            if(callbacks.get("unselected") != null) callbacks.get("unselected").run();
            callbackForSelect = false;
        }
    }
    public void mouseReleased(int button) {
        if(button == GLFW_MOUSE_BUTTON_LEFT) {
            if(!holdsState) selected = false;
        }
        if (callbackForSelect && !selected) {
            if(callbacks.get("unselected") != null) callbacks.get("unselected").run();
            callbackForSelect = false;
        }
        if (callbacks.get("mouseReleased") != null) callbacks.get("mouseReleased").run();
    }
    public void render() {
        if (selected) selectedMesh.render();
        else if(hovered) hoverMesh.render();
        else mesh.render();
        textMeshes.forEach(TextMesh::render);
    }

    public void close() {
        textMeshes.forEach(Mesh::close);
        mesh.close();
        selectedMesh.close();
        hoverMesh.close();
    }
}
