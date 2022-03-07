package com.jimphieffer.game;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long window;
    private int width;
    private int height;
    public Window(String name, int width, int height, InputListener listener) {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW!");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
        window = glfwCreateWindow(width, height, name, NULL, NULL);
        if(window == NULL) {
            throw new RuntimeException("Could not initialize window!");
        }
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS) listener.keyPressed(window, key);
            else if(action == GLFW_RELEASE) listener.keyReleased(window, key);
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if(action == GLFW_PRESS) listener.mousePressed(window, button);
            else if(action == GLFW_RELEASE) listener.mouseReleased(window, button);
        });
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwSetWindowSizeCallback(window, (window, newWidth, newHeight) -> setWindowSize(newWidth, newHeight));
    }

    public long getWindow() {
        return window;
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
