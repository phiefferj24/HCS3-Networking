package com.jimphieffer.graphics.hud.elements;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.hud.FloatRectangle;
import com.jimphieffer.graphics.hud.TextMesh;
import com.jimphieffer.utils.TextUtilities;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class HUDTextBox extends HUDElement {

    private String text = "";
    private TextUtilities textUtilities;
    private Mesh selectedMesh;
    private Mesh hoverMesh;
    private boolean selected = false;
    private boolean callbackForSelect = false;
    private boolean hovered = false;
    private boolean callbackForHover = false;
    private ArrayList<TextMesh> textMeshes = new ArrayList<>();
    private ArrayList<TextMesh> placeholderTextMeshes = new ArrayList<>();
    private String font;
    private int programId;

    public HUDTextBox(Mesh mesh, Mesh hoverMesh, Mesh selectedMesh, int windowWidth, int windowHeight, int programId, String font, String placeholder) {
        super.mesh = mesh;
        this.hoverMesh = hoverMesh;
        this.selectedMesh = selectedMesh;
        super.bounds = new FloatRectangle(mesh.x - mesh.width, mesh.y + mesh.height, mesh.x + mesh.width, mesh.y - mesh.height);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.programId = programId;
        this.font = font;
        this.textUtilities = new TextUtilities(font, 1);
        float textWidth = TextUtilities.getCombinedWidth(placeholder, textUtilities) / 8 * mesh.height;
        for(int i = 0; i < placeholder.length(); i++) {
            float offset = TextUtilities.getCombinedWidth(placeholder.substring(0, i + 1), textUtilities) / 8 * mesh.height;
            float x = mesh.x + offset - textWidth / 2;
            float y = mesh.y;
            placeholderTextMeshes.add(new TextMesh(placeholder.charAt(i), font, x, y, 0.5f, mesh.height / 2, textUtilities, programId));
        }
        //IntStream.range(0, placeholderTextMeshes.size()).forEach(i -> placeholderTextMeshes.get(i).translate(-TextUtilities.getCombinedWidth(placeholderTextMeshes, i, placeholder.length()) * 2, 0, 0));
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
        if(text.isEmpty() && !selected) placeholderTextMeshes.forEach(Mesh::render);
    }

    @Override
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

    @Override
    public void mouseMoved(double x, double y) {
        mouseX = x;
        mouseY = y;
        hovered = bounds.contains((float) mouseX, (float) mouseY);
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
    public void charTyped(char c) {
        if(selected) {
            if(TextUtilities.getCombinedWidth(text, textUtilities)/8*mesh.height> Math.abs(bounds.x2 - bounds.x1) - mesh.height) {
                return;
            }
            float x = mesh.x + TextUtilities.getCombinedWidth(text, textUtilities) / 16 * mesh.height;
            float y = mesh.y;
            textMeshes.forEach(mesh -> {
                mesh.translate(-textUtilities.getWidth(c)/8*this.mesh.height/2, 0, 0);
            });
            textMeshes.add(new TextMesh(c, font, x, y, 0.5f, mesh.height/2, textUtilities, programId));
            setText(getText() + c);
        }
        if (callbacks.get("charTyped") != null) callbacks.get("charTyped").run();
    }

    public void keyPressed(int key) {
        if (selected) {
            if (key == GLFW_KEY_BACKSPACE || key == GLFW_KEY_DELETE) {
                if (text.length() > 0) {
                    textMeshes.remove(textMeshes.size() - 1).close();
                    textMeshes.forEach(mesh -> mesh.translate(textUtilities.getWidth(text.charAt(text.length() - 1))/8*this.mesh.height/2, 0, 0));
                    setText(getText().substring(0, getText().length() - 1));
                }
            }
        }
        if (callbacks.get("keyPressed") != null) callbacks.get("keyPressed").run();
    }
    public void close() {
        textMeshes.forEach(Mesh::close);
        mesh.close();
        selectedMesh.close();
        hoverMesh.close();
    }
}
