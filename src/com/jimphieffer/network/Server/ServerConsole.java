package com.jimphieffer.network.Server;

import com.jimphieffer.Message;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ServerConsole implements ActionListener {
    private JTextArea textArea;
    private JTextField textField;
    private ArrayList<String> clients;
    private ConnectionListener listener;
    private static final int TEXT_INPUT_HEIGHT = 30;
    public ServerConsole(int width, int height) {
        JFrame jf = new JFrame("Console");
        jf.getContentPane().setPreferredSize(new Dimension(width, height));
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAutoscrolls(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 0, width, height-TEXT_INPUT_HEIGHT);
        textField = new JTextField();
        textField.setBounds(0, height-TEXT_INPUT_HEIGHT, width, TEXT_INPUT_HEIGHT);
        textField.addActionListener(this);
        jf.setLayout(null);
        jf.add(scrollPane);
        jf.add(textField);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
        clients = new ArrayList<>();
    }
    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        if(type == Message.MessageType.MESSAGE) {
            if (protocol == Message.MessageProtocol.RELAY) {
                for (ServerThread thread : listener.getServerThreads()) {
                    if (!socket.equals(thread.getSocket())) {
                        thread.send(message);
                    }
                }
            }
            else if (protocol == Message.MessageProtocol.SEND) {

            }
            log("Received: \"" + message + "\" from " + getClientName(socket) + " (" + socket.getInetAddress() + ")");
        }
        else if(type == Message.MessageType.CONNECT) {
            clients.add(message);
            log("Connected: " + message + " (" + socket.getInetAddress() + ")");
        }
        else if(type == Message.MessageType.DISCONNECT) {
            clients.remove(message);
            log("Disconnected: " + message + " (" + socket.getInetAddress() + ")");
        }
    }

    public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(listener != null) {
            listener.relay(textField.getText());
            log("Sent: \"" + textField.getText() + "\"");
        }
        else log("Error: Listener not yet added");
        textField.setText("");
    }

    public static String stamped(String message) {
        return "[" + new Timestamp(System.currentTimeMillis()) + "] " + message;
    }
    public void log(String message) {
        textArea.append(stamped(message)+'\n');
    }
    public String getClientName(Socket socket) {
        for(ServerThread thread : listener.getServerThreads()) {
            if(thread.getSocket().equals(socket)) return thread.getClientName();
        }
        return "";
    }
}