package com.jimphieffer.network.client;

import com.jimphieffer.Message;
import com.jimphieffer.game.Game;

import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import javax.swing.*;
import javax.sound.sampled.*;

public class ClientThread extends Thread{
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private String lastMessage = "";
    private Game game;


    private static ArrayList<File> soundtrack = new ArrayList<File>();


    public ClientThread(String ip, int port, Game game) {
        this.game = game;
        System.out.println("Trying to connect to " + ip + " on port " + port + "...");
        try {
            socket = new Socket(ip, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to " + ip + " on port " + port + ".");
            output.println(Message.encode(game.getUsername(), Message.MessageProtocol.SEND, Message.MessageType.CONNECT));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void run() {
        //soundtrack.add(new File("tetris.wav"));
        while(true) {
            try {
                //play(new File(tetris.wav),true);
                String message = input.readLine();
                if(message == null) {
                    System.out.println("Server terminated connection.");
                    System.exit(1);
                }
                onMessage(Message.decode(message));

                run();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void send(String message) {
        output.println(Message.encode(message, Message.MessageProtocol.SEND, Message.MessageType.MESSAGE));
       //System.out.println("Sent: " + message);
    }
    public void relay(String message) {
        output.println(Message.encode(message, Message.MessageProtocol.RELAY, Message.MessageType.MESSAGE));
    }
    public void onMessage(String message) {
        game.onMessage(message);
    }

    public static void play(File file, Boolean repeat)
    {
        try
        {
            final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

            clip.addLineListener(new LineListener()
            {
                @Override
                public void update(LineEvent event)
                {
                    if (event.getType() == LineEvent.Type.STOP)
                    {
                        clip.close();
                        if(repeat)
                            play(soundtrack.get((int)(Math.random()*soundtrack.size())), true);
                    }
                }
            });

            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();

        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getClientName() { //TODO uses the name of device as client name in the future we wanna have user input name on display before connecting for this :) -Tiko
        String hostname = "Unknown";

        try
        {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return hostname;
    }
}
