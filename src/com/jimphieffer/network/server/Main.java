package com.jimphieffer.network.server;

import com.jimphieffer.game.Game;

public class Main {
    public static void main(String[] args) {
        Server listener = new Server(9000);
        listener.listen();
        Game g = new Game();
        g.init();
        g.run();
    }
}
