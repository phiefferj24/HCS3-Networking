package com.jimphieffer.game;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private final long window;
    private int width;
    private int height;
    private Game game;
    public Window(String name, int width, int height, Game game) {
        this.width = width;
        this.height = height;
        this.game = game;
        GLFWErrorCallback.createPrint(System.err).set();
        if(!glfwInit()) {
            throw new IllegalStateException("Could not initialize GLFW!");
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        window = glfwCreateWindow(width, height, name, NULL, NULL);
        if(window == NULL) {
            throw new RuntimeException("Could not initialize window!");
        }
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(action == GLFW_PRESS) game.keyPressed(window, key);
            else if(action == GLFW_RELEASE) game.keyReleased(window, key);
        });
        //glfwSetCharCallback(window, (window, code) -> game.charTyped(window, (char)code));
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if(action == GLFW_PRESS) game.mousePressed(window, button);
            else if(action == GLFW_RELEASE) game.mouseReleased(window, button);
        });
        glfwSetCursorPosCallback(window, game::mouseMoved);
        glfwSetFramebufferSizeCallback(window, (window, nwidth, nheight) -> {
            this.width = nwidth;
            this.height = nheight;
            game.windowSizeChanged();
        });
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwSetWindowSizeCallback(window, (window, newWidth, newHeight) -> setWindowSize(newWidth, newHeight));
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0, 0, 0, 0);
    }

    public long getWindow() {
        return window;
    }

    public void setWindowSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
