package com.jimphieffer.graphics;


import org.joml.Math;
import java.nio.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.*;

public class Mesh {
    public float x;
    public float y;
    public float z;
    public float angle;
    public float width;
    public float height;
    private final int vvboId;
    private final int evboId;
    private final int tvboId;
    public final int vaoId;
    public final int vertexCount;
    private final Texture texture;
    public Matrix4f positionMatrix = new Matrix4f();
    private final int programId;
    public Mesh(float x, float y, float z, float width, float height, String texture, int programId) {
        this(new float[] {-width, -height, -1.f, width, -height, -1.f, -width, height, -1.f, width, height, -1.f}, new int[] {0, 1, 2, 1, 3, 2}, new float[] {0, 1, 1, 1, 0, 0, 1, 0}, new Texture(texture), x, y, z, programId);
    }
    public Mesh(float x, float y, float z, float width, float height, String texture, int programId, float textureScaleX, float textureScaleY) {
        this(new float[] {-width, -height, -1.f, width, -height, -1.f, -width, height, -1.f, width, height, -1.f}, new int[] {0, 1, 2, 1, 3, 2}, new float[] {0, 1/textureScaleY, 1/textureScaleX, 1/textureScaleY, 0, 0, 1/textureScaleX, 0}, new Texture(texture), x, y, z, programId);
    }
    public Mesh(float x, float y, float z, float width, float height, String texture, int programId, float texX1, float texY1, float texX2, float texY2) {
        this(new float[] {-width, -height, -1.f, width, -height, -1.f, -width, height, -1.f, width, height, -1.f}, new int[] {0, 1, 2, 1, 3, 2}, new float[] {texX1, texY2, texX2, texY2, texX1, texY1, texX2, texY1}, new Texture(texture), x, y, z, programId);
    }
    public Mesh(float[] vertices, int[] indices, float[] textureCoordinates, Texture texture, float x, float y, float z, int programId) {
        this.x = x;
        this.y = y;
        this.width = vertices[3];
        this.height = -vertices[4];
        this.programId = programId;
        vertexCount = indices.length;
        this.texture = texture;
        FloatBuffer verticesBuffer = null;
        IntBuffer elementBuffer = null;
        FloatBuffer textureCoordinateBuffer = null;
        positionMatrix.translate(x, y, z);
        try {
            // initialize buffers
            verticesBuffer = memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();
            elementBuffer = memAllocInt(indices.length);
            elementBuffer.put(indices).flip();
            textureCoordinateBuffer = memAllocFloat(textureCoordinates.length);
            textureCoordinateBuffer.put(textureCoordinates).flip();

            // initialize and bind vertex array object
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // handle vertex buffer
            vvboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vvboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // handle element buffer
            evboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, evboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

            // handle color buffer
            tvboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, tvboId);
            glBufferData(GL_ARRAY_BUFFER, textureCoordinateBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // clear buffer and vertex array object
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            // clear buffers if they exist
            if(verticesBuffer != null) {
                memFree(verticesBuffer);
            }
            if(elementBuffer != null) {
                memFree(elementBuffer);
            }
            if(textureCoordinateBuffer != null) {
                memFree(textureCoordinateBuffer);
            }
        }
    }
    public void close() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vvboId);
        glDeleteBuffers(evboId);
        glDeleteBuffers(tvboId);
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
        glDeleteTextures(texture.id);
    }
    public void render() {
        Uniforms.setUniform("positionMatrix", positionMatrix, programId);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.id);
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
    public void translate(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        positionMatrix.translate(x, y, z);
    }
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        positionMatrix = new Matrix4f().translate(x, y, z).rotateZ(angle);
    }
    public void scale(float factor) {
        positionMatrix.scale(factor);
    }
    public void rotate(float degrees) {
        angle += Math.toRadians(degrees);
        positionMatrix.translate(-x, -y, -z);
        positionMatrix.rotateZ(Math.toRadians(degrees));
        positionMatrix.translate(x, y, z);
    }
    public void setRotation(float degrees) {
        angle = Math.toRadians(degrees);
        positionMatrix = new Matrix4f().rotateZ(angle).translate(x, y, z);
    }
}
