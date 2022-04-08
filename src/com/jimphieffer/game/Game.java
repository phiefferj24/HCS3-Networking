package com.jimphieffer.game;

import com.jimphieffer.Message;
import com.jimphieffer.network.client.ClientThread;
import com.jimphieffer.network.server.Server;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.imageio.ImageIO;
import javax.xml.stream.Location;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    private double x;
    private double y;
    private double vx;
    private double vy;

    private BufferedImage bi;


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
            System.out.println("run");
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

        if(Message.getType(message).equals(Message.MessageType.MOVEMENT))
        {
            message = message.substring(message.indexOf(":"),message.length());
            String[] loc = message.split(",");
            x = Double.parseDouble(loc[1]);
            y = Double.parseDouble(loc[2]);
            vx = Double.parseDouble(loc[3]);
            vy = Double.parseDouble(loc[4]);
        }


    }

    public void init() {
        //ct = new ClientThread("127.0.0.1",9000);


        try {
            bi = ImageIO.read(Game.class.getResource("/com/jimphieffer/game/sprites/player.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }







        int windowWidth = 800;
        int windowHeight = 600;

        window = new Window("Window",windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        GL.createCapabilities();
       // c = new ClientThread("10.13.98.152",9000);
        //this.start();


    }

    private void tick(double deltaTime) {

        System.out.println("tick");

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
        double scale = .3; //doesnt do anything :/
        glClearColor(0, 0, 0, 0);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        double centerC = x/((double)window.getWidth()/2)-1;
        double centerY = (double)y/((double)window.getHeight()/2)-1;
        double sizex = (double)bi.getWidth()/(double)window.getWidth() * scale;
        double sizey = (double)bi.getHeight()/(double)window.getHeight() * scale;

//        System.out.println((centerC+sizex/2));
//        System.out.println((centerY+sizey/2));
//        System.out.println((centerC-sizex/2));
//        System.out.println((centerY-sizey/2));


        shittyRender(bi,centerC+sizex/2,centerY+sizey/2,centerC-sizex/2,centerY-sizey/2);

    //    shittyRender(bi,.5,0,0,.5);

        glfwSwapBuffers(windowPointer);
    }

    public void shittyRender(BufferedImage bi, double x1, double y1, double x2, double y2) {
        double xc = (x2-x1)/bi.getWidth();
        double yc = (y2-y1)/bi.getHeight();
        glBegin(GL_QUADS);
        for(int i = 0; i < bi.getWidth(); i++) {
            for(int j = 0; j < bi.getHeight(); j++) {
                int c = bi.getRGB(i, j);
                glColor4d(((c >> 16) & 0xFF) / 255., ((c >> 8) & 0xFF) / 255., (c & 0xFF) / 255., ((c >> 24) & 0xFF) / 255.);
                glVertex3d(i*xc+x1, j*yc+y1, 0);
                glVertex3d((i+1)*xc+x1, j*yc+y1, 0);
                glVertex3d((i+1)*xc+x1, (j+1)*yc+y1, 0);
                glVertex3d(i*xc+x1, (j+1)*yc+y1, 0);
            }
        }
        glEnd();
    }

    public String getUsername() {
        return username;
    }


    public void keyPressed(long window, int key) {
        vx = 0;
        vy = 0;


        if (key == GLFW_KEY_D)
            vx=1;
        if (key == GLFW_KEY_A)
            vx=1;
        if (key == GLFW_KEY_W)
            vy=1;
        if (key == GLFW_KEY_A)
            vy=1;
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
