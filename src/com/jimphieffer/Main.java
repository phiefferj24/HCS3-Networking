package com.jimphieffer;

import com.jimphieffer.Client.Client;
import com.jimphieffer.Client.ClientThread;
import com.jimphieffer.Server.ConnectionListener;
import com.jimphieffer.Server.Server;
import com.jimphieffer.Server.ServerConsole;

public class Main {
    public static void main(String[] args) {
        ServerConsole sc = new ServerConsole(800, 600);
        Server server = sc::onMessage;
        ConnectionListener listener = new ConnectionListener(5678, server, sc);
        listener.listen();
    }
}
