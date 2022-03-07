package com.jimphieffer.game;

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

public class Game extends Thread {
    private static final int MAX_QUEUE_SIZE = 3;
    public static final BlockingQueue<MethodWrapper> queue = new LinkedBlockingQueue<>();

    private double framesPerSecond = 30.d;
    private Window window;
    private long windowPointer;
    public void run() {
        final double secondsPerFrame = 1.d / framesPerSecond;
        double lastRenderTime = glfwGetTime();
        while(!glfwWindowShouldClose(windowPointer)) {
            double deltaTime = glfwGetTime() - lastRenderTime;
            while(deltaTime < secondsPerFrame) {
                tick(deltaTime);
                deltaTime = glfwGetTime() - lastRenderTime;
            }
            if(queue.size() < MAX_QUEUE_SIZE) queue.add(this::render);
            else System.err.println("Rendering running behind!");
            lastRenderTime = glfwGetTime();
        }
        queue.add(this::close);
        System.exit(0);
    }

    public void init() {
        window = new Window("Window",800, 600, new InputListener());
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
    }

    public void render() { //DO NOT CALL FROM INSIDE THREAD!
        glfwPollEvents();
        double time = System.currentTimeMillis() / 1000.d;
        glClearColor((float) Math.abs(Math.sin(time)), 0, (float) (1-Math.abs(Math.sin(time))), 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapBuffers(windowPointer);
    }

    public static void main(String[] args) {
        Configuration.GLFW_CHECK_THREAD0.set(false);
        Game g = new Game();
        g.init();
        g.start();
        while(!glfwWindowShouldClose(g.windowPointer)) {
            try {
                queue.take().run();
            } catch (InterruptedException e) {
                System.err.println("Could not take render from queue!");
            }
        }
    }
}
