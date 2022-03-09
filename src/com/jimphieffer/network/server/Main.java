package com.jimphieffer.network.server;

public class Main {
    public static void main(String[] args) {
        Server listener = new Server(9000);
        listener.listen();
    }
}
