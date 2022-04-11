package com.jimphieffer.game;

import com.jimphieffer.Message;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.Texture;
import com.jimphieffer.graphics.Uniforms;
import com.jimphieffer.network.client.ClientThread;
import com.jimphieffer.network.server.Server;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;

import static com.jimphieffer.utils.FileUtilities.*;
import static org.lwjgl.system.MemoryUtil.*;

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

    private BufferedImage bi;

    private int programId;
    private int vsId;
    private int fsId;

    private ArrayList<Mesh> meshes;

    private boolean[] keys = new boolean[6];

    private Camera camera;

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
        double lastTickTime;
        double deltaTime = 0;
        while(!glfwWindowShouldClose(windowPointer)) {
            glfwPollEvents();
            tick(glfwGetTime() - lastRenderTime);
            render();
            lastRenderTime = glfwGetTime();
            while(glfwGetTime() - lastRenderTime < secondsPerFrame);
            //System.out.println("FPS: " + (1/sinceRender));
        }
        close();
        System.exit(0);
    }

    public void onMessage(String message) {

        System.out.println("message to game: " + message);

        if(Message.getType(message).equals(Message.MessageType.MOVEMENT))
        {
            message = message.substring(message.indexOf(":"),message.length());
        }


    }

    public void init() {
        //ct = new ClientThread("127.0.0.1",9000);
        int windowWidth = 1280;
        int windowHeight = 720;

        window = new Window("Window",windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
       // c = new ClientThread("10.13.98.152",9000);
        //this.start();
        initShaders();

        meshes = new ArrayList<>();

        initTextures();

        camera = new Camera(window.getWidth(), window.getHeight());
    }

    public void windowSizeChanged() {
        camera.setScreenSize(window.getWidth(), window.getHeight());
    }

    private void initTextures() {
        Uniforms.createUniform("texture_sampler", programId);
        Uniforms.createUniform("positionMatrix", programId);
        Uniforms.createUniform("projectionMatrix", programId);

        meshes.add(createMesh(0, 0, 0, 0.1f, 0.1f, "resources/sprites/gb.png"));
    }

    private void initShaders() {
        // create shader program
        programId = glCreateProgram();
        if(programId == 0) {
            System.err.println("Could not create shader program.");
            return;
        }

        // compile and link vertex shader
        vsId = glCreateShader(GL_VERTEX_SHADER);
        if(vsId == 0) {
            System.err.println("Could not create vertex shader.");
            return;
        }
        glShaderSource(vsId, loadFile("/shaders/vertex.vs"));
        glCompileShader(vsId);
        if(glGetShaderi(vsId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile vertex shader.");
            return;
        }
        glAttachShader(programId, vsId);

        // compile and link fragment shader
        fsId = glCreateShader(GL_FRAGMENT_SHADER);
        if(fsId == 0) {
            System.err.println("Could not create fragment shader.");
            return;
        }
        glShaderSource(fsId, loadFile("/shaders/fragment.fs"));
        glCompileShader(fsId);
        if(glGetShaderi(fsId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Could not compile fragment shader.");
            return;
        }
        glAttachShader(programId, fsId);

        // link shader program to window
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            System.err.println("Could not link shader program.");
            return;
        }

        // clear compiled shaders
        glDetachShader(programId, vsId);
        glDetachShader(programId, fsId);

        // validate the shader program
        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating shader program.");
        }
    }

    private void tick(double deltaTime) {

        //ct.send(Message.encode(username + ", " + x + ", " + y + ", " + vx + ", " + vy,Message.MessageProtocol.SEND, Message.MessageType.MOVEMENT));

        int dirx = keys[0] ? 1 : -1;
        int diry = keys[3] ? 1 : -1;
        meshes.get(0).translate((keys[2] || keys[3]) ? (float)deltaTime * diry : 0, (keys[0] || keys[1]) ? (float)deltaTime * dirx : 0, 0);
        if(keys[4]) {
            camera.setFOV(camera.getFOV() + (float)deltaTime * 10);
        }
        if(keys[5]) {
            camera.setFOV(camera.getFOV() - (float)deltaTime * 10);
        }

    }




    private void close() {
        if(programId != 0) glDeleteProgram(programId);
        meshes.forEach(Mesh::close);
        glfwFreeCallbacks(windowPointer);
        glfwDestroyWindow(windowPointer);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        System.exit(0);
    }

    public void render() { //DO NOT CALL FROM INSIDE THREAD!
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);


        glUseProgram(programId);
        Uniforms.setUniform("texture_sampler", 0);
        Uniforms.setUniform("projectionMatrix", camera.projectionMatrix);

        meshes.forEach(Mesh::render);
        glUseProgram(0);

        glfwSwapBuffers(windowPointer);
    }

    public String getUsername() {
        return username;
    }


    public void keyPressed(long window, int key) {
        switch (key) {
            case GLFW_KEY_UP -> keys[0] = true;
            case GLFW_KEY_DOWN -> keys[1] = true;
            case GLFW_KEY_LEFT -> keys[2] = true;
            case GLFW_KEY_RIGHT -> keys[3] = true;
            case GLFW_KEY_Z -> keys[4] = true;
            case GLFW_KEY_X -> keys[5] = true;
        }
    }
    public void keyReleased(long window, int key) {
        if(key == GLFW_KEY_ESCAPE) {
            close();
        }
        switch (key) {
            case GLFW_KEY_UP -> keys[0] = false;
            case GLFW_KEY_DOWN -> keys[1] = false;
            case GLFW_KEY_LEFT -> keys[2] = false;
            case GLFW_KEY_RIGHT -> keys[3] = false;
            case GLFW_KEY_Z -> keys[4] = false;
            case GLFW_KEY_X -> keys[5] = false;
        }
    }
    public void mousePressed(long window, int button) {

    }
    public void mouseReleased(long window, int button) {

    }


    // use but dont touch

    public Mesh createMesh(float x, float y, float z, float width, float height, String texture) {
        return new Mesh(
                new float[] {
                    -width, -height, -1.f,
                    width, -height, -1.f,
                    -width, height, -1.f,
                    width, height, -1.f,
                },
                new int[] {
                    0, 1, 2, 1, 3, 2,
                },
                new float[] {
                    0, 1,
                    1, 1,
                    0, 0,
                    1, 0,
                },
                new Texture(texture),
                x, y, z);
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
