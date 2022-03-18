package com.jimphieffer.network.server;

import com.jimphieffer.Message;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private Server server;
    private ArrayList<ServerThread> serverThreads;
    private ServerConsole console;
    private ServerGame serverGame;
    public Server(int port) {
        this.serverGame = new ServerGame(this);
        try {
            serverSocket = new ServerSocket(port);
            this.server = server;
            serverThreads = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public Server(int port, ServerConsole console) {
        this(port);
        this.console = console;
        console.setListener(this);
    }
    public void listen() {
        log("Listening for connections on port " + serverSocket.getLocalPort() + "...");
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket, server);
                serverThreads.add(serverThread);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void relay(String message) {
        for(ServerThread thread : serverThreads) {
            thread.send(message);
        }
    }
    public void send(String message, Socket... sockets) {
        for(ServerThread thread : serverThreads) {
            for(Socket socket : sockets) {
                if(thread.getSocket() == socket) thread.send(message);
            }
        }
    }
    public void log(String message) {
        if(console == null) System.out.println(message);
        else console.log(message);
    }
    public ArrayList<ServerThread> getServerThreads() {
        return serverThreads;
    }
    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        serverGame.onMessage(message,protocol,type,socket);
    }

}
