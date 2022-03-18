package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.Player;
import com.jimphieffer.game.Window;
import org.lwjgl.opengl.GL;

import java.net.Socket;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class ServerGame extends Thread {

    private Server server;

    public ServerGame(Server server) {
        this.server = server;
    }

    public void run()
    {

    }


    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        if(protocol== Message.MessageProtocol.SEND)
        {
            if (type == Message.MessageType.CONNECT)
            {

            }
        }
        else if (protocol== Message.MessageProtocol.RELAY)
        {
            //TODO
        }
        else
        {
            System.out.println("tried an illegal protocol");
        }


    }
}
