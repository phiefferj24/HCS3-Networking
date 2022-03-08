package com.jimphieffer.network.server;

import java.io.*;
import java.net.*;

import com.jimphieffer.Message;

public class ServerThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter output;
    private BufferedReader input;
    private String name = "";
    public ServerThread(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void run() {
        while(true) {
            try {
                String message = input.readLine();
                if(message == null) {
                    System.out.println("Client at\"" + socket.getInetAddress().toString() + "\" terminated connection.");
                    server.onMessage(name, Message.MessageProtocol.SEND, Message.MessageType.DISCONNECT, socket);
                    this.interrupt();
                    return;
                }
                else if(Message.getType(message) == Message.MessageType.CONNECT) name = Message.decode(message);
                server.onMessage(Message.decode(message), Message.getProtocol(message), Message.getType(message), socket);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void send(String message) {
        output.println(Message.encode(message, Message.MessageProtocol.SEND, Message.MessageType.MESSAGE));
    }
    public Socket getSocket() {
        return socket;
    }
    public String getClientName() {
        return name;
    }
}
