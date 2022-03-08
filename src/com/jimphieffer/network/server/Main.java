package com.jimphieffer.network.server;

public class Main {
    public static void main(String[] args) {
        ServerConsole console = new ServerConsole(800, 600);
        Server server = console::onMessage;
        ConnectionListener listener = new ConnectionListener(9000, server, console);
        listener.listen();
    }
}
