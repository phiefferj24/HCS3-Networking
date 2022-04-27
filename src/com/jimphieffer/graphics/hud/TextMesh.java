package com.jimphieffer.graphics.hud;

import com.jimphieffer.graphics.Mesh;

public class TextMesh extends Mesh {
    public TextMesh(char letter, String font, float x, float y, float z, float width, float height, int programId) {
        super(x, y, z, width, height, font, programId,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 16.f : (((int)letter-0x20) & 0x0F) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 16.f : (((int)letter-0x20) & 0xF0) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 32.f : (((int)letter-0x20) & 0x0F + 1) / 16.f,
                ((int)letter-0x20 < 0 || (int)letter-0x20 >= 0x60) ? 32.f : (((int)letter-0x20) & 0xF0 + 1) / 16.f
                );
    }
}
