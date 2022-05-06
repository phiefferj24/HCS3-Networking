package com.jimphieffer.graphics.hud;

import com.jimphieffer.utils.TextUtilities;

import java.util.ArrayList;

public class TextBox {
    private String text;
    private ArrayList<TextMesh> textMeshes;
    private TextUtilities textUtilities;
    private float x;
    private float y;
    private float z;
    private float height;
    private String font;
    private int programId;

    public TextBox(int programId, String font, String text, float x, float y, float z, float height) {
        this.programId = programId;
        this.font = font;
        this.text = text;
        this.x = x;
        this.y = y;
        this.z = z;
        this.height = height;
        this.textMeshes = new ArrayList<>();
        this.textUtilities = new TextUtilities(font, 1);
        float textWidth = TextUtilities.getCombinedWidth(text, textUtilities) / 8 * height;
        for(int i = 0; i < text.length(); i++) {
            float offset = TextUtilities.getCombinedWidth(text.substring(0, i + 1), textUtilities) / 8 * height;
            float mx = x + offset - textWidth / 2;
            float my = y;
            textMeshes.add(new TextMesh(text.charAt(i), font, mx, my, z, height/2, textUtilities, programId));
        }
    }

    public void setText(String text) {
        this.text = text;
        while(!textMeshes.isEmpty()) {
            textMeshes.remove(0).close();
        }
        float textWidth = TextUtilities.getCombinedWidth(text, textUtilities) / 16 * height;
        for(int i = 0; i < text.length(); i++) {
            float offset = TextUtilities.getCombinedWidth(text.substring(0, i + 1), textUtilities) / 16 * height;
            float mx = x + offset - textWidth / 2;
            float my = y;
            textMeshes.add(new TextMesh(text.charAt(i), font, mx, my, z, height, textUtilities, programId));
        }
    }
    public String getText() {
        return text;
    }

    public void close() {
        for(TextMesh mesh : textMeshes) {
            mesh.close();
        }
    }
    public void render() {
        for (TextMesh mesh : textMeshes) {
            mesh.render();
        }
    }
}
