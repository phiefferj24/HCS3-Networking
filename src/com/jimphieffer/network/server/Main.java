package com.jimphieffer.network.server;

import com.jimphieffer.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main extends JComponent{
    public static void main(String[] args) {
        //int theta = (int) (System.currentTimeMillis()%100);


        Thread t = new Thread(() -> {
            Server s = new Server(9000);
            s.listen();

        });

        t.start();




    }
}
