package com.jimphieffer.graphics;

import org.lwjgl.system.MemoryUtil;

import static com.jimphieffer.utils.ImageUtilities.*;

import java.nio.*;

import static org.lwjgl.opengl.GL30.*;

public class Texture {
    public final int id;
    public Texture(String path) {
        ImageData imageData = getImageData("resources"+path);
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imageData.width,
                imageData.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData.buffer);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glGenerateMipmap(id);
        freeImageBuffer(imageData.buffer);
    }
}
