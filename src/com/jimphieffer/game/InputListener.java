package com.jimphieffer.game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class InputListener {
    public void keyPressed(long window, int key) {

    }
    public void keyReleased(long window, int key) {
        if(key == GLFW_KEY_ESCAPE) {
            Game.queue.add(() -> {
                glfwFreeCallbacks(window);
                glfwDestroyWindow(window);
                glfwTerminate();
                glfwSetErrorCallback(null).free();
                System.exit(0);
            });
        }
    }
    public void mousePressed(long window, int button) {

    }
    public void mouseReleased(long window, int button) {

    }
}
