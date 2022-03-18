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

    private int windowWidth;
    private int windowHeight;
    private boolean space = false;
    private ArrayList<Sprite> sprites;

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
        sprites = new ArrayList<>();
        sprites.add(new Player(50,50,50,50,"src/com/jimphieffer/game/sprites/player.png",0,0,"Host Player"));
        windowWidth = 800;
        windowHeight = 600;
        window = new Window("Window",windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        GL.createCapabilities();
       // c = new ClientThread("10.13.98.152",9000);


    }

    private void tick(double deltaTime) {


        for(Sprite s: sprites)
        {
            s.step(this);

            if(s instanceof Player)
                if (space) {
                    System.out.println("space");
                    ((Player) s).setVX(((Player) s).getVX() + 0.1);
                }

        }



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
        glClearColor(0.0f, 0.0f, 0.0f,0.0f);
        glClear(GL_COLOR_BUFFER_BIT & GL_DEPTH_BUFFER_BIT);
        glColor3f(1, 1, 1);

        for(Sprite s: sprites) {
            glBegin(GL_TRIANGLES);
            {



                glVertex2d(s.getLeft()/windowWidth, (double)s.getHeight()/windowHeight);
                glVertex2d(s.getLeft()/windowWidth+(double)s.getWidth()/windowWidth, s.getTop()/windowHeight);
                glVertex2d(s.getLeft()/windowWidth+(double)s.getWidth()/(windowWidth*2), s.getTop()/windowHeight+(double)s.getHeight()/windowHeight);


            }
        }
        glEnd();
        glfwSwapBuffers(windowPointer);
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

    public int getWindowWidth()
    {
        return windowWidth;
    }

    public int getWindowHeight()
    {
        return windowHeight;
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.init();
        g.run();
    }
}
