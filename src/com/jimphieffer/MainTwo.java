package com.jimphieffer;

import com.jimphieffer.Client.Client;
import com.jimphieffer.Client.ClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

public class MainTwo implements KeyListener {
    public static JTextArea jta = new JTextArea();
    public static Client client;
    public static ClientThread clientThread;
    public static void main(String[] args) {
        JFrame jf = new JFrame("bruh");
        jf.setPreferredSize(new Dimension(800, 600));
        jta.setPreferredSize(new Dimension(800, 600));
        jf.add(jta);
        jta.setLineWrap(true);
        jta.addKeyListener(new MainTwo());
        client = new Client() {
            @Override
            public void onMessage(String message) {
                jta.append(message);
            }

            @Override
            public String getName() {
                return Double.toString(Math.random());
            }
        };
        clientThread = new ClientThread("127.0.0.1", 5678, client);
        clientThread.start();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        clientThread.relay(Character.toString(e.getKeyChar()));
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
