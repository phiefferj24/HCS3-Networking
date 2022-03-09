package com.jimphieffer.game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class InputListener {
    public void keyPressed(long window, int key) {

    }
    public void keyReleased(long window, int key) {
        if(key == GLFW_KEY_ESCAPE) {
            Game.close(window);
        }
    }
    public void mousePressed(long window, int button) {

    }
    public void mouseReleased(long window, int button) {

    }
}
