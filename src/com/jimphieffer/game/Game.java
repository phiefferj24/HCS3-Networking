package com.jimphieffer.game;

import com.jimphieffer.network.client.ClientThread;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.xml.stream.Location;
import java.util.ArrayList;
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


    private boolean space = false;


    private double framesPerSecond = 60.d;
    private Window window;
    private long windowPointer;
    private ClientThread c;


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
        int windowWidth = 800;
        int windowHeight = 600;

        window = new Window("Window",windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        GL.createCapabilities();
       // c = new ClientThread("10.13.98.152",9000);


    }

    private void tick(double deltaTime) {


//        for(Sprite s: sprites)
//        {
//            s.step(this);
//
//            if(s instanceof Player)
//                if (space) {
//                    System.out.println("space");
//                    ((Player) s).setVX(((Player) s).getVX() + 0.1);
//                }
//
//        }



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

    public void keyPressed(long window, int key) {
        if(key==GLFW_KEY_SPACE)
        {
            space = true;
        }
    }
    public void keyReleased(long window, int key) {
        if(key == GLFW_KEY_ESCAPE) {
            Game.close(window);
        }
        else if(key==GLFW_KEY_SPACE)
        {
            space = false;
        }
    }
    public void mousePressed(long window, int button) {

    }
    public void mouseReleased(long window, int button) {

    }


    public static void main(String[] args) {
        Game g = new Game();
        g.init();
        g.run();
    }
}
