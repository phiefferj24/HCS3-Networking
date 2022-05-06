package com.jimphieffer.utils;

import com.jimphieffer.graphics.hud.TextMesh;
import org.joml.Math;

import java.util.ArrayList;
import java.util.Arrays;

public class TextUtilities {
    private String font;
    private byte[] fontData;
    private int spacing;
    public TextUtilities(String font, int spacing) {
        String dataFilePath = font.substring(0, font.lastIndexOf(".")) + ".dat";
        byte[] fontData = FileUtilities.loadFileAsBytes(dataFilePath);
        this.font = font;
        this.fontData = fontData;
        this.spacing = spacing;
    }
    public float getX1(char c) {
        return ((int)c-0x20 < 0 || (int)c-0x20 >= 0x60) ? 0.f : (((int)c) & 0x0F) / 16.f;
    }
    public float getX2(char c) {
        return ((int)c-0x20 < 0 || (int)c-0x20 >= 0x60) ? 1/16.f : ((((int)c) & 0x0F) + getWidth(c) / 8.f) / 16.f;
}
    public float getY1(char c) {
        return ((int)c-0x20 < 0 || (int)c-0x20 >= 0x60) ? 0.f : ((((int)c) >> 4) & 0x0F) / 16.f;
    }
    public float getY2(char c) {
        return ((int)c-0x20 < 0 || (int)c-0x20 >= 0x60) ? 1/16.f : (((((int)c) >> 4) & 0x0F) + 1) / 16.f;
    }

    public float getWidth(char c) {
        return Math.min((float)((fontData[(int)c/2] >> ((int)c % 2 == 0 ? 4 : 0)) & 0x0F) + spacing, 8.f);
    }

    public static float getCombinedWidth(ArrayList<TextMesh> textMeshes, int startIndex, int endIndex) {
        float width = 0.f;
        for (int i = startIndex; i < endIndex; i++) {
            width += textMeshes.get(i).width;
        }
        return width;
    }

    public static float getCombinedWidth(String text, TextUtilities textUtilities) {
        float width = 0.f;
        for (char c : text.toCharArray()) {
            width += textUtilities.getWidth(c) + textUtilities.spacing;
        }
        return width;
    }
}
