package com.jimphieffer.network.server;

import com.jimphieffer.game.Display;
import com.jimphieffer.game.Game;

import javax.swing.*;
import java.awt.*;

public class Main extends JComponent{
    public static void main(String[] args) {


        Thread t = new Thread(() -> {
            Server s = new Server(9000);
            s.listen();

        });

        t.start();


        Display d = new Display(800,600);
        d.run();




    }
}
