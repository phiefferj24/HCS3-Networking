package com.jimphieffer.game;

import org.joml.Math;
import org.joml.Matrix4f;

public class Camera {
    public Matrix4f projectionMatrix;
    public Camera(int width, int height) {
        projectionMatrix = new Matrix4f().ortho(-1.f, 1.f, (float)height/width, -(float)height/width, 0.01f, 1000.f);
    }
    public void translate(float x, float y, float z) {
        projectionMatrix.translate(-x, -y, -z);
    }
    public void setPosition(float x, float y, float z) {
        projectionMatrix.identity().translate(-x, -y, -z);
    }
    public void setScreenSize(int width, int height) {
        projectionMatrix.ortho(-1.f, 1.f, (float)height/width, -(float)height/width, 0.01f, 1000.f);
    }
    public void rotate(float angle) {
        projectionMatrix.rotateZ(Math.toRadians(-angle));
    }
}
