package com.jimphieffer.game;

import com.jimphieffer.Message;
import com.jimphieffer.game.objects.NonStatic;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.Uniforms;
import com.jimphieffer.graphics.hud.HUD;
import com.jimphieffer.network.client.ClientThread;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


import static com.jimphieffer.utils.FileUtilities.*;

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

    private int objectProgramId;
    private int objectVertexShaderId;
    private int objectFragmentShaderId;

    private int hudProgramId;
    private int hudVertexShaderId;
    private int hudFragmentShaderId;

    private ArrayList<Mesh> meshes;
    private HUD hud;
    private ArrayList<Sprite> sprites;
    private ArrayList<Sprite> staticSprites;
    private ArrayList<Sprite> nonStaticSprites;
    private boolean[] keys = new boolean[6];

    private Camera camera;

    private Player player;

    private Mesh background;

    public Game(String ip, int port) {

        Scanner s = new Scanner(System.in);
        System.out.println("username: ");
        username = s.nextLine();

        x = 50;
        y = 50;
        vx = 0;
        vy = 0;

        ct = new ClientThread(ip, port, this);
        ct.start();
    }


    public void run() {

        final double secondsPerFrame = 1.d / framesPerSecond;

        double lastRenderTime = glfwGetTime();
        double lastTickTime;
        double deltaTime = 0;
        while (!glfwWindowShouldClose(windowPointer)) {
            glfwPollEvents();
            tick(glfwGetTime() - lastRenderTime);
            render();
            lastRenderTime = glfwGetTime();
            while (glfwGetTime() - lastRenderTime < secondsPerFrame) ;
            //System.out.println("FPS: " + (1/sinceRender));
        }
        //
        //Player dupe=player.set(VX)
        //ct.send(Message.encode());

        String bruh = "";
        for (Sprite s: sprites)
        {
          //  s.step(this);
            bruh+=s.toString() + ",";
        }
        ct.send(Message.encode(bruh, Message.MessageProtocol.SEND,Message.MessageType.SPRITE));



        close();
        System.exit(0);
    }

    public static ArrayList<String> splitMessage(String message){
        ArrayList<String> list=new ArrayList<>();
        int last=0;
        for(int x=0; x<message.length(); x++)
        {
            if(message.charAt(x) == ';')
            {
                list.add(message.substring(last,x));
                last=x+1;
            }
        }
        return list;
    }

    public void onMessage(String message) {

        System.out.println("message to game: " + message);
        if (Message.getType(message).equals(Message.MessageType.MOVEMENT)) {
            message = message.substring(message.indexOf(":"), message.length());
            message = message.substring(1, message.length());
            String[] loc = message.split(",");
            player.setX(Double.parseDouble(loc[0]));
            player.setY(Double.parseDouble(loc[1]));
            player.setVX(Double.parseDouble(loc[2]));
            player.setVY(Double.parseDouble(loc[3]));
        }
        if (Message.getType(message).equals(Message.MessageType.SPRITE))
        {
            sprites.clear();
            message = Message.decode(message);
            String[] sprs = message.split(",");
            for(String s: sprs)
                sprites.add(Sprite.stringToSprite(s));
        }
    }


    public void init() {
        //ct = new ClientThread("127.0.0.1",9000);
        int windowWidth = 1280;
        int windowHeight = 720;

        window = new Window("Window", windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        // c = new ClientThread("10.13.98.152",9000);
        //this.start();

        meshes = new ArrayList<>();

        initShaders();

        this.staticSprites=new ArrayList<Sprite>();
        this.nonStaticSprites=new ArrayList<Sprite>();

        //(String image, double x, double y, int width, int height, double angle, int health,  int programID



        // for(int x=0; x<sprites.size(); x++)
        // {//double x, double y, int width, int height,int programID
        //if(sprites.getType)
        // sprites.add(new Animal(Math.random()*windowHeight,Math.random()*windowWidth,50, 50,glCreateProgram() ));

        // }

        player = new Player(0, 0, 100, 100,
                "/textures/player.png", objectProgramId, 0, 0, username);

        initTextures();

        camera = new Camera(window.getWidth(), window.getHeight());

        hud = new HUD(hudProgramId, window.getWidth(), window.getHeight());

        background = new Mesh(0, 0, -10.f, windowWidth, windowHeight, "/textures/grass.png", objectProgramId, 0.1f, 0.1f);
    }

    public void windowSizeChanged() {
        camera.setScreenSize(window.getWidth(), window.getHeight());
        hud.setScreenSize(window.getWidth(), window.getHeight());
    }

    private void initTextures() {
        meshes.add(new Mesh((float) player.getX(), (float) player.getY(), 0, 0.05f, 0.05f, player.getImage(), objectProgramId));
    }

    private void initShaders() {
        // create shader program
        objectProgramId = glCreateProgram();
        if (objectProgramId == 0) {
            System.err.println("Could not create object shader program.");
            return;
        }

        // compile and link vertex shader
        objectVertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        if (objectVertexShaderId == 0) {
            System.err.println("Could not create object vertex shader.");
            return;
        }
        glShaderSource(objectVertexShaderId, loadFile("/shaders/objects/vertex.vs"));
        glCompileShader(objectVertexShaderId);
        if (glGetShaderi(objectVertexShaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile object vertex shader.");
            return;
        }
        glAttachShader(objectProgramId, objectVertexShaderId);

        // compile and link fragment shader
        objectFragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        if (objectFragmentShaderId == 0) {
            System.err.println("Could not create object fragment shader.");
            return;
        }
        glShaderSource(objectFragmentShaderId, loadFile("/shaders/objects/fragment.fs"));
        glCompileShader(objectFragmentShaderId);
        if (glGetShaderi(objectFragmentShaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile object fragment shader.");
            return;
        }
        glAttachShader(objectProgramId, objectFragmentShaderId);

        // link shader program to window
        glLinkProgram(objectProgramId);
        if (glGetProgrami(objectProgramId, GL_LINK_STATUS) == 0) {
            System.err.println("Could not link object shader program.");
            return;
        }

        // clear compiled shaders
        glDetachShader(objectProgramId, objectVertexShaderId);
        glDetachShader(objectProgramId, objectFragmentShaderId);

        // validate the shader program
        glValidateProgram(objectProgramId);
        if (glGetProgrami(objectProgramId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating object shader program.");
        }


        hudProgramId = glCreateProgram();
        if (hudProgramId == 0) {
            System.err.println("Could not create HUD shader program.");
            return;
        }

        // compile and link vertex shader
        hudVertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        if (hudVertexShaderId == 0) {
            System.err.println("Could not create HUD vertex shader.");
            return;
        }
        glShaderSource(hudVertexShaderId, loadFile("/shaders/hud/vertex.vs"));
        glCompileShader(hudVertexShaderId);
        if (glGetShaderi(hudVertexShaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile HUD vertex shader.");
            return;
        }
        glAttachShader(hudProgramId, hudVertexShaderId);

        // compile and link fragment shader
        hudFragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        if (hudFragmentShaderId == 0) {
            System.err.println("Could not create HUD fragment shader.");
            return;
        }
        glShaderSource(hudFragmentShaderId, loadFile("/shaders/hud/fragment.fs"));
        glCompileShader(hudFragmentShaderId);
        if (glGetShaderi(hudFragmentShaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile HUD fragment shader.");
            return;
        }
        glAttachShader(hudProgramId, hudFragmentShaderId);

        // link shader program to window
        glLinkProgram(hudProgramId);
        if (glGetProgrami(hudProgramId, GL_LINK_STATUS) == 0) {
            System.err.println("Could not link HUD shader program.");
            return;
        }

        // clear compiled shaders
        glDetachShader(hudProgramId, hudVertexShaderId);
        glDetachShader(hudProgramId, hudFragmentShaderId);

        // validate the shader program
        glValidateProgram(hudProgramId);
        if (glGetProgrami(hudProgramId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating HUD shader program.");
        }

        // initialize uniforms
        Uniforms.createUniform("texture_sampler", objectProgramId);
        Uniforms.createUniform("positionMatrix", objectProgramId);
        Uniforms.createUniform("projectionMatrix", objectProgramId);
        Uniforms.createUniform("color", objectProgramId);
        Uniforms.createUniform("texture_sampler", hudProgramId);
        Uniforms.createUniform("positionMatrix", hudProgramId);
        Uniforms.createUniform("color", hudProgramId);
    }

    private void tick(double deltaTime) {

        //ct.send(Message.encode(username + ", " + x + ", " + y + ", " + vx + ", " + vy,Message.MessageProtocol.SEND, Message.MessageType.MOVEMENT));
        float mod = 10;
        int dirx = keys[0] ? 1 : -1;
        int diry = keys[3] ? 1 : -1;
        meshes.get(0).setPosition((float) player.getX(), (float) player.getY(), 0);
        for(Sprite s: nonStaticSprites)
            s.step(this);

        player.step(this);
        //camera.translate((keys[2] || keys[3]) ? (float)deltaTime * diry * mod: 0, (keys[0] || keys[1]) ? (float)deltaTime * dirx * mod: 0, 0);
    }


    private void close() {
        if (objectProgramId != 0) glDeleteProgram(objectProgramId);
        meshes.forEach(Mesh::close);
        glfwFreeCallbacks(windowPointer);
        glfwDestroyWindow(windowPointer);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.exit(0);
    }

    public void render() { //DO NOT CALL FROM INSIDE THREAD!
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        glUseProgram(objectProgramId);
        Uniforms.setUniform("texture_sampler", 0, objectProgramId);
        Uniforms.setUniform("projectionMatrix", camera.projectionMatrix, objectProgramId);
        Uniforms.setUniform("color", new Vector4f(1, 1, 1, 1), objectProgramId);

        background.render();

        player.mesh.render();

        meshes.forEach(Mesh::render);

        glUseProgram(hudProgramId);
        Uniforms.setUniform("texture_sampler", 0, hudProgramId);
        Uniforms.setUniform("color", new Vector4f(1, 1, 1, 1), hudProgramId);
        hud.render();

        glUseProgram(0);

        glfwSwapBuffers(windowPointer);
    }


    public String getUsername() {
        return username;
    }


    public void keyPressed(long window, int key) {
        if(key==87)
        {
            System.out.println("imgay"); //this happens
           player.setVY(player.getVY()+1);
           //Tiko we need that thing to step bruh we cant do anything if we cant just send string
        }
        if(key==83)
       {
           player.setVX(player.getVY()-1);
        }
        if(key==65)
        {
            player.setVX(player.getVX()-1);
        }
       if(key==68)
        {
           player.setVX(player.getVX()+1);
        }

        hud.keyPressed(key);
    }

    public void keyReleased(long window, int key) {
        if (key == GLFW_KEY_ESCAPE) {
            close();
        }
        player.setVX(0);
        hud.keyReleased(key);
    }

    public void mousePressed(long window, int button) {
        hud.mousePressed(button);
    }

    public void mouseReleased(long window, int button) {
        hud.mouseReleased(button);
    }

    public void charTyped(long window, char character) {
        hud.charTyped(character);
    }

    /**
     * Called whenever the mouse moves.
     *
     * @param window the pointer to the window
     * @param x      the X position of the mouse, in pixels
     * @param y      the Y position of the mouse, in pixels
     */
    public void mouseMoved(long window, double x, double y) {
        player.setRotation(Math.atan2(y,x)*(180/Math.PI));
        //TODO: handle rotation
        hud.mouseMoved(x, y);
    }


    // use but dont touch

    public static void main(String[] args) {
//        Thread t = new Thread(() -> {
//            Server s = new Server(9000);
//            s.listen();
//        });
//        t.start();
        Game g = new Game("127.0.0.1", 9000);
        g.init();
        g.run();
    }
}
