package com.jimphieffer.game;

import com.jimphieffer.Message;
import com.jimphieffer.network.client.ClientThread;
import com.jimphieffer.network.server.Server;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Scanner;
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
    private String ip;
    private int port;

    private String username;


    private double framesPerSecond = 60.d;
    private Window window;
    private long windowPointer;
    private ClientThread ct;

    private int x;
    private int y;
    private int vx;
    private int vy;


    public Game(String ip, int port) {

        Scanner s = new Scanner(System.in);
        System.out.println("username: ");
        username = s.nextLine();

        x = 50;
        y = 50;
        vx = 0;
        vy = 0;

        ct = new ClientThread(ip,port, this);
        ct.start();



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


    }

    public void onMessage(String message) {

        System.out.println("message to game: " + message);


    }

    public void init() {
        //ct = new ClientThread("127.0.0.1",9000);









        int windowWidth = 800;
        int windowHeight = 600;

        window = new Window("Window",windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        GL.createCapabilities();
       // c = new ClientThread("10.13.98.152",9000);
        //this.start();


    }

    private void tick(double deltaTime) {

        ct.send(Message.encode(username + ", " + x + ", " + y + ", " + vx + ", " + vy,Message.MessageProtocol.SEND, Message.MessageType.MOVEMENT));




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

    public String getUsername() {
        return username;
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
        Thread t = new Thread(() -> {
            Server s = new Server(9000);
            s.listen();
        });
        t.start();

        Game g = new Game("127.0.0.1",9000);
        g.init();
        g.run();
    }
}
