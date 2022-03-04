package com.jimphieffer.Network.Client;

import com.jimphieffer.Message;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private Client client;
    private PrintWriter output;
    private BufferedReader input;
    public ClientThread(String ip, int port, Client client) {
        System.out.println("Trying to connect on port " + port + "...");
        try {
            socket = new Socket(ip, port);
            this.client = client;
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected on port " + port + ".");
            output.println(Message.encode(client.getName(), Message.MessageProtocol.SEND, Message.MessageType.CONNECT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while(true) {
            try {
                String message = input.readLine();
                if(message == null) {
                    System.out.println("Server terminated connection.");
                    System.exit(1);
                }
                client.onMessage(Message.decode(message));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void send(String message) {
        output.println(Message.encode(message, Message.MessageProtocol.SEND, Message.MessageType.MESSAGE));
        System.out.println("Sent: " + message);
    }
    public void relay(String message) {
        output.println(Message.encode(message, Message.MessageProtocol.RELAY, Message.MessageType.MESSAGE));
        System.out.println("Relayed: " + message);
    }
    public Client getClient() {
        return client;
    }
}
