package com.jimphieffer.network.server;

import com.jimphieffer.Message;

import java.net.Socket;

public interface Server {
    void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket);
}
