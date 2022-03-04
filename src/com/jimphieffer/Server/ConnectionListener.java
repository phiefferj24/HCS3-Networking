package com.jimphieffer.Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionListener {
    private ServerSocket serverSocket;
    private Server server;
    private ArrayList<ServerThread> serverThreads;
    private ServerConsole console;
    public ConnectionListener(int port, Server server) {
        try {
            serverSocket = new ServerSocket(port);
            this.server = server;
            serverThreads = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public ConnectionListener(int port, Server server, ServerConsole console) {
        this(port, server);
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
}
