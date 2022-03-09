package com.jimphieffer.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.util.concurrent.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Note:
 * RenderWrapper exists here because OpenGL with Java on Mac is incredibly annoying.
 * Most events (but not all) that interface with the window need to be run in the same thread as the window was created in.
 * The window MUST also be created in the main thread, or the program will error.
 * The BlockingQueue here allows render to be queued up in the main thread while tick can run on the separate thread.
 * This should not cause any problems (as of now).
 */

public class Game {

    private double framesPerSecond = 60.d;
    private Window window;
    private long windowPointer;
    private InputListener inputListener;

    public Game(InputListener inputListener) {
        this.inputListener = inputListener;
    }
    public void run() {
        final double secondsPerFrame = 1.d / framesPerSecond;
        double lastRenderTime = glfwGetTime();
        double lastTickTime = glfwGetTime();
        double deltaTime = glfwGetTime() - lastRenderTime;
        while(!glfwWindowShouldClose(windowPointer)) {
            while(deltaTime < secondsPerFrame) {
                tick(deltaTime);
                deltaTime = glfwGetTime() - lastTickTime;
            }
            glfwPollEvents();
            render();
            lastRenderTime = glfwGetTime();
        }
        close();
        System.exit(0);
        //
    }

    public void init() {
        window = new Window("Window",800, 600, inputListener);
        windowPointer = window.getWindow();
        GL.createCapabilities();
    }

    private void tick(double deltaTime) {

    }

    private void close() {
        glfwFreeCallbacks(windowPointer);
        glfwDestroyWindow(windowPointer);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.exit(0);
    }

    public static void close(long window) {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.exit(0);
    }

    public void render() { //DO NOT CALL FROM INSIDE THREAD!

    }

    public static void main(String[] args) {
        Game g = new Game(new InputListener());
        g.init();
        g.run();
    }
}
