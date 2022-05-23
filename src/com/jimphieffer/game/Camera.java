package com.jimphieffer.game;

import org.joml.Math;
import org.joml.Matrix4f;

public class Camera {
    public Matrix4f projectionMatrix;
    private int width;
    private int height;
    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
        projectionMatrix = new Matrix4f().ortho(-width, width, -height, height, 0.01f, 1000.f);
    }
    public void translate(float x, float y, float z) {
        projectionMatrix.translate(-x, -y, -z);
    }
    public void setPosition(float x, float y, float z) {
        projectionMatrix = new Matrix4f().ortho(-width, width, -height, height, 0.01f, 1000.f).translate(-x, -y, -z);
    }
    public void setScreenSize(int width, int height) {
        this.width = width;
        this.height = height;
        projectionMatrix = new Matrix4f().ortho(-width, width, -height, height, 0.01f, 1000.f);
    }
    public void rotate(float angle) {
        projectionMatrix.rotateZ(Math.toRadians(-angle));
    }
}
