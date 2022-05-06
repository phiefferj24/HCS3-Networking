package com.jimphieffer.graphics.hud;

import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.utils.TextUtilities;

public class TextMesh extends Mesh {
    public final char letter;
    public TextMesh(char letter, String font, float x, float y, float z, float width, float height, int programId) {
        super(x, y, z, width, height, font, programId,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 0.f : (((int)letter) & 0x0F) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 0.f : ((((int)letter) >> 4) & 0x0F) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 1/16.f : ((((int)letter) & 0x0F) + 1) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 1/16.f : (((((int)letter) >> 4) & 0x0F) + 1) / 16.f
                );
        this.letter = letter;
    }
    public TextMesh(char letter, String font, float x, float y, float z, float height, TextUtilities data, int programId) {
        super(x, y, z, data.getWidth(letter)*height/8, height, font, programId, data.getX1(letter), data.getY1(letter), data.getX2(letter), data.getY2(letter));
        this.letter = letter;
    }
}
