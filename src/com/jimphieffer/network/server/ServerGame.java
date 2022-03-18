package com.jimphieffer.network.server;

import com.jimphieffer.Message;

import java.net.Socket;

public class ServerGame extends Server {

    public ServerGame(int port) {
        super(port);
    }

    public void run() {


    }

    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
    }
}
