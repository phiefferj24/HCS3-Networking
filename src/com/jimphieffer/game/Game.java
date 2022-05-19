package com.jimphieffer.game;

import com.jimphieffer.Message;
import com.jimphieffer.graphics.Mesh;
import com.jimphieffer.graphics.Uniforms;
import com.jimphieffer.graphics.hud.HUD;
import com.jimphieffer.graphics.hud.TextBox;
import com.jimphieffer.graphics.hud.elements.HUDButton;
import com.jimphieffer.graphics.hud.elements.HUDTextBox;
import com.jimphieffer.network.client.ClientThread;
import com.jimphieffer.network.server.Server;
import com.jimphieffer.utils.json.*;
import org.joml.Vector4f;

import com.jimphieffer.game.objectTypes.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.lang.String;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


import com.jimphieffer.game.objectTypes.*;


import static com.jimphieffer.utils.FileUtilities.*;

public class Game {

    public interface Method {
        void run();
    }

    public BlockingQueue<Method> methods = new LinkedBlockingQueue<>();

    private double playerRotation = 0;
    private boolean space = false;
    private String ip;
    private int port;

    private String username;


    private double framesPerSecond = 60.d;
    private Window window;
    private long windowPointer;
    public ClientThread ct;

    public static int objectProgramId;
    private int objectVertexShaderId;
    private int objectFragmentShaderId;

    public static int hudProgramId;
    private int hudVertexShaderId;
    private int hudFragmentShaderId;

    private int tickCount = 0;
    private int numSteps = 0;

    private ArrayList<Mesh> meshes;
    private HUD hud;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private ArrayList<Sprite> staticSprites;
    private ArrayList<Sprite> nonStaticSprites;
    private boolean[] keys = new boolean[6];
    private boolean started = false;
    private boolean newRound = false;
    private boolean recievedConnect = false;
    private boolean recievedSprites = false;
    private UUID waitingScreen;
    private ArrayList<TextBox> waitingStuff = new ArrayList<TextBox>();

    private Camera camera;

    private Player player;

    private Mesh background;

    private int windowWidth;
    private int windowHeight;
    private String windowTitle;

    private HUD mainMenu;

    public Game(int windowWidth, int windowHeight, String windowTitle) {



        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowTitle = windowTitle;

    }

    public void connect(String ip, int port) {
        ct = new ClientThread(ip, port, this);
        ct.start();

    }

    public void setUsername(String username) {
        this.username = username;
        player.setUsername(username);

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
            while (!methods.isEmpty()) {
                try {
                    methods.take().run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (glfwGetTime() - lastRenderTime < secondsPerFrame) ;
            //System.out.println("FPS: " + (1/sinceRender));
        }



        close();
        System.exit(0);
    }

    public static ArrayList<String> splitMessage(String message) {
        ArrayList<String> list = new ArrayList<>();
        int last = 0;
        for (int x = 0; x < message.length(); x++) {
            if (message.charAt(x) == ';') {
                list.add(message.substring(last, x));
                last = x + 1;
            }
        }
        return list;
    }

    public void onMessage(String message) {
        System.out.println("\n\nGame recieved: " + message);
        if (Message.getType(message) == Message.MessageType.CONNECT) {
            onMessage(Message.encode(Message.decode(message), Message.MessageProtocol.SEND, Message.MessageType.SPRITE));
            sprites.add(player);
            recievedConnect = true;
        } else if (Message.getType(message) == Message.MessageType.SPRITE) {
            numSteps = 0;
            recievedSprites = true;
            String decodedMessage = Message.decode(message);
            AnnotatedDecoder decoder = new AnnotatedDecoder(decodedMessage);
            decoder.addAssignmentMethod(UUID.class, UUID::fromString);
            Sprite[] tempSprites = decoder.getDerivativeObjects(Sprite.class);
            sprites.replaceAll(sprite -> {
                for (Sprite tempSprite : tempSprites) {
                    if (sprite.getUUID().equals(tempSprite.getUUID())) {
                        return tempSprite;
                    }
                }
                return sprite;
            });
        }
    }


    public void init() {
        //ct = new ClientThread("127.0.0.1",9000);

        window = new Window(windowTitle, windowWidth, windowHeight, this);
        windowPointer = window.getWindow();
        // c = new ClientThread("10.13.98.152",9000);
        //this.start();

        meshes = new ArrayList<>();

        initShaders();

        this.staticSprites = new ArrayList<Sprite>();
        this.nonStaticSprites = new ArrayList<Sprite>();

        //(String image, double x, double y, int width, int height, double angle, int health,  int programID



        // for(int x=0; x<sprites.size(); x++)
        // {//double x, double y, int width, int height,int programID
        //if(sprites.getType)
        // sprites.add(new Animal(Math.random()*windowHeight,Math.random()*windowWidth,50, 50,glCreateProgram() ));

        // }

        sprites = new ArrayList<>();
        player = new Player(0, 0, 100, 100, "/textures/player.png", null, 0, 0, username);
        initTextures();

        camera = new Camera(window.getWidth(), window.getHeight());

        hud = new HUD(hudProgramId, window.getWidth(), window.getHeight(), false);

        background = new Mesh(0, 0, -10.f, windowWidth, windowHeight, "/textures/grass.png", objectProgramId, 0.1f, 0.1f);
    }

    public void windowSizeChanged() {
        camera.setScreenSize(window.getWidth(), window.getHeight());
        hud.setScreenSize(window.getWidth(), window.getHeight());
    }

    private void initTextures() {
        //meshes.add(new Mesh((float) player.getX(), (float) player.getY(), 0, 0.05f, 0.05f, player.getImage(), objectProgramId));
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
        Uniforms.createUniform("projectionMatrix", hudProgramId);
        Uniforms.createUniform("color", hudProgramId);
    }

    private void preStartTick(double deltaTime, int numPlayers) {
        for (int i = 0; i < sprites.size(); i++) {// maybe instead of this all sprites start as being deactivated where they are at position 0,0 cant do anything and dont have an image and then when we activate them they start to do stuff. just an idea
            if (sprites.get(i).getClassType().equals("Player"))
                sprites.get(i).setImage("/textures/wall.png");
        }
        waitingScreen = UUID.randomUUID();
        waitingStuff.add(new TextBox(hudProgramId, "/fonts/minecraft.png", "Waiting for next round...", 0, 0, 0, 30));
        //how to initalize

    }

    private void tick(double deltaTime) {

        //in order to check name of class do: sprite.getClass().getSimpleName().equalsIgnoreCase("PLAYER"
        tickCount++;
        if (tickCount == 1) {
            for (int i = 0; i < sprites.size(); i++) {
                if (sprites.get(i) instanceof Player) {
                    sprites.get(i).setX(windowWidth / 2.f * Math.random() + windowWidth / 2.f);
                    sprites.get(i).setY(windowHeight / 2.f * Math.random() + windowHeight / 2.f);
                    //player.mesh.setRotation(player.getLocalRotation());
                }
            }
            return;
        }
        if (recievedSprites && recievedConnect) {
            AnnotatedEncoder encoder = new AnnotatedEncoder();
            for (int d = 0; d < sprites.size(); d++) {

                System.out.println(sprites.size());
                sprites.get(d).step();
                sprites.get(d).mesh.setPosition((float) sprites.get(d).getX(), (float) sprites.get(d).getY(), 0);
                encoder.addAnnotatedObject(sprites.get(d));
            }
            ct.send(Message.encode(encoder.encode(), Message.MessageProtocol.SEND, Message.MessageType.SPRITE));
            recievedSprites=false;
        }
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

        glUseProgram(hudProgramId);
        Uniforms.setUniform("texture_sampler", 0, hudProgramId);
        Uniforms.setUniform("projectionMatrix", hud.camera.projectionMatrix, hudProgramId);
        Uniforms.setUniform("color", new Vector4f(1, 1, 1, 1), hudProgramId);
        hud.render();
        waitingStuff.forEach(TextBox::render);

        glUseProgram(objectProgramId);
        Uniforms.setUniform("texture_sampler", 0, objectProgramId);
        Uniforms.setUniform("projectionMatrix", camera.projectionMatrix, objectProgramId);
        if (hud.visible) {
            Uniforms.setUniform("color", new Vector4f(0.5f, 0.5f, 0.5f, 1), objectProgramId);
        } else {
            Uniforms.setUniform("color", new Vector4f(1, 1, 1, 1), objectProgramId);
        }

        background.render();

        player.mesh.render();

        sprites.forEach(sprite -> {
            if (sprite.mesh != null) sprite.mesh.render();
        });



        glUseProgram(0);

        glfwSwapBuffers(windowPointer);
    }


    public String getUsername() {
        return username;
    }


    public void keyPressed(long window, int key) {
        if (key == GLFW_KEY_W) {
            player.setVY(player.getVY() + 10);
        }
        if (key == GLFW_KEY_S) {
            player.setVY(player.getVY() - 10);
        }
        if (key == GLFW_KEY_A) {
            player.setVX(player.getVX() - 10);
        }
        if (key == GLFW_KEY_D) {
            player.setVX(player.getVX() + 10);
        }
        if (key == GLFW_KEY_ESCAPE) {
            if (mainMenu == null) hud.visible = !hud.visible;
        }

        hud.keyPressed(key);
        if (mainMenu != null) mainMenu.keyPressed(key);
    }

    public void keyReleased(long window, int key) {
        if (key == GLFW_KEY_W) {
            player.setVY(0.01);
        }
        if (key == GLFW_KEY_S) {
            player.setVY(-.01);
        }
        if (key == GLFW_KEY_A) {
            player.setVX(-0.01);
        }
        if (key == GLFW_KEY_D) {
            player.setVX(0.01);
        }
        hud.keyReleased(key);
        if (mainMenu != null) mainMenu.keyReleased(key);
    }

    public void mousePressed(long window, int button) {
        hud.mousePressed(button);
        if (mainMenu != null) mainMenu.mousePressed(button);
    }

    public void mouseReleased(long window, int button) {
        hud.mouseReleased(button);
        if (mainMenu != null) mainMenu.mouseReleased(button);
    }

    public void charTyped(long window, char character) {
        hud.charTyped(character);
        if (mainMenu != null) mainMenu.charTyped(character);
    }

    public void mouseMoved(long window, double x, double y) {
        float angely = (float) Math.atan2(y, x);
        player.setLocalRotation((float) (360 * angely));
        //System.out.println(angely);
        //TODO: handle rotation
        hud.mouseMoved(x, y);
        if (mainMenu != null) mainMenu.mouseMoved(x, y);
    }

    public void menu() {
        mainMenu = new HUD(hudProgramId, windowWidth, windowHeight, true);
        mainMenu.elements.add(new HUDTextBox(
                new Mesh(0, 150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 66 / 256.f, 200 / 256.f, 86 / 256.f),
                new Mesh(0, 150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 86 / 256.f, 200 / 256.f, 106 / 256.f),
                new Mesh(0, 150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 46 / 256.f, 200 / 256.f, 66 / 256.f),
                windowWidth, windowHeight, hudProgramId, "/fonts/minecraft.png", "Username"));
        mainMenu.elements.add(new HUDTextBox(
                new Mesh(0, 50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 66 / 256.f, 200 / 256.f, 86 / 256.f),
                new Mesh(0, 50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 86 / 256.f, 200 / 256.f, 106 / 256.f),
                new Mesh(0, 50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 46 / 256.f, 200 / 256.f, 66 / 256.f),
                windowWidth, windowHeight, hudProgramId, "/fonts/minecraft.png", "Address"));
        mainMenu.elements.add(new HUDButton(
                new Mesh(0, -50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 66 / 256.f, 200 / 256.f, 86 / 256.f),
                new Mesh(0, -50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 86 / 256.f, 200 / 256.f, 106 / 256.f),
                new Mesh(0, -50, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 46 / 256.f, 200 / 256.f, 66 / 256.f),
                windowWidth, windowHeight, hudProgramId, "/fonts/minecraft.png", "Join", false));
        mainMenu.elements.add(new HUDButton(
                new Mesh(0, -150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 66 / 256.f, 200 / 256.f, 86 / 256.f),
                new Mesh(0, -150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 86 / 256.f, 200 / 256.f, 106 / 256.f),
                new Mesh(0, -150, 0.f, 500f, 50f, "/textures/widgets.png", hudProgramId, 0, 46 / 256.f, 200 / 256.f, 66 / 256.f),
                windowWidth, windowHeight, hudProgramId, "/fonts/minecraft.png", "Quit Game", false));
        System.out.println(mainMenu.elements.get(2).getClass());
        mainMenu.elements.get(2).setCallback("selected", () -> {
            if (((HUDTextBox) mainMenu.elements.get(1)).getText().matches("^((([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3}))|localhost):([0-9]{1,5})$") && ((HUDTextBox) mainMenu.elements.get(0)).getText().matches("^[A-Za-z0-9_-]*$")) {
                mainMenu.visible = false;
                setUsername(((HUDTextBox) mainMenu.elements.get(0)).getText());
                connect(((HUDTextBox) mainMenu.elements.get(1)).getText().split(":")[0], Integer.parseInt(((HUDTextBox) mainMenu.elements.get(1)).getText().split(":")[1]));
            }
        });
        mainMenu.elements.get(3).setCallback("selected", () -> {
            mainMenu.close();
            close();
        });
        double lastRenderTime = glfwGetTime();
        final double secondsPerFrame = 1.d / framesPerSecond;
        while (!glfwWindowShouldClose(windowPointer) && ct == null) {
            glfwPollEvents();
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            glUseProgram(hudProgramId);
            Uniforms.setUniform("texture_sampler", 0, hudProgramId);
            Uniforms.setUniform("projectionMatrix", mainMenu.camera.projectionMatrix, hudProgramId);
            Uniforms.setUniform("color", new Vector4f(1, 1, 1, 1), hudProgramId);
            mainMenu.render();

            glUseProgram(0);
            glfwSwapBuffers(windowPointer);
            lastRenderTime = glfwGetTime();
            while (glfwGetTime() - lastRenderTime < secondsPerFrame) ;
            //System.out.println("FPS: " + (1/sinceRender));
        }
        mainMenu.close();
        mainMenu = null;
        System.out.println("run");
    }


    // use but dont touch

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            Server s = new Server(9000);
            s.listen();
        });
        t.start();

        // join menu
        Game g = new Game(1280, 720, "Game");
        g.init();
        g.menu();
        g.run();


//        Sprite p = new Player(1.0, 2.0, 3, 4, "hello", UUID.randomUUID(), 5.0, 6.0, "world");
//        AnnotatedEncoder encoder = new AnnotatedEncoder();
//        encoder.addObject(p, Sprite.class);
//        System.out.println(encoder.encode());
//        AnnotatedDecoder decoder = new AnnotatedDecoder(encoder.encode());
//        decoder.addAssignmentMethod(UUID.class, UUID::fromString);
//        Sprite p2 = decoder.getDerivativeObjects(Sprite.class)[0];
//        System.out.println(p2.getUUID());
    }
}
