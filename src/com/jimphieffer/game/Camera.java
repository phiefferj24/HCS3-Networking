package com.jimphieffer.game;

import org.joml.Math;
import org.joml.Matrix4f;

public class Camera {
    public Matrix4f projectionMatrix;
    private float aspectRatio;
    private float fovDeg = 60.f;
    public Camera(int width, int height) {
        aspectRatio = (float) width / height;
        projectionMatrix = new Matrix4f().perspective(Math.toRadians(fovDeg), aspectRatio, 0.01f, 1000.f);
    }
    public void translate(float x, float y, float z) {
        projectionMatrix.translate(-x, -y, -z);
    }
    public void setPosition(float x, float y, float z) {
        projectionMatrix.identity().translate(-x, -y, -z);
    }
    public void setScreenSize(int width, int height) {
        aspectRatio = (float) width / height;
        projectionMatrix.setPerspective(Math.toRadians(fovDeg), aspectRatio, 0.01f, 1000.f);
    }
    public void setFOV(float degrees) {
        fovDeg = degrees;
        projectionMatrix.setPerspective(Math.toRadians(fovDeg), aspectRatio, 0.01f, 1000.f);
    }
    public float getFOV() {
        return fovDeg;
    }
    public float getAspectRatio() {
        return aspectRatio;
    }
    public void rotate(float angle) {
        projectionMatrix.rotateZ(Math.toRadians(-angle));
    }
}
