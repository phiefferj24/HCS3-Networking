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

public class Game extends Thread {
    public static final int MAX_QUEUE_LENGTH = 3;
    public static final BlockingQueue<MethodWrapper> queue = new LinkedBlockingQueue<>();

    private double framesPerSecond = 30.d;
    private Window window;
    private long windowPointer;
    public void run() {
        final double secondsPerFrame = 1.d / framesPerSecond;
        double lastRenderTime = glfwGetTime();
        double lastTickTime = glfwGetTime();
        double deltaTime = glfwGetTime() - lastRenderTime;
        while(!glfwWindowShouldClose(windowPointer)) {
            call(GLFW::glfwPollEvents);
            while(deltaTime < secondsPerFrame) {
                tick(deltaTime);
                deltaTime = glfwGetTime() - lastTickTime;
            }
            call(this::render);
            lastRenderTime = glfwGetTime();
        }
        queue.add(this::close);
        System.exit(0);
        //
    }

    public void call(MethodWrapper m) {
        if(queue.size() < MAX_QUEUE_LENGTH) queue.add(m);
        else while(queue.size() >= MAX_QUEUE_LENGTH);
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
        //test
        double time = System.currentTimeMillis() / 1000.d;
        glClearColor((float)((Math.sin(time)+1)/2.f), 0.f, 1-(float)((Math.sin(time)+1)/2.f), 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glfwSwapBuffers(windowPointer);
    }

    public static void main(String[] args) {
        Configuration.GLFW_CHECK_THREAD0.set(false);
        Game g = new Game();
        g.init();
        g.start();
        while(!glfwWindowShouldClose(g.windowPointer) || !queue.isEmpty()) {
            try {
                queue.take().run();
            } catch (InterruptedException e) {
                System.err.println("Could not take render from queue!");
            }
        }
    }
}
