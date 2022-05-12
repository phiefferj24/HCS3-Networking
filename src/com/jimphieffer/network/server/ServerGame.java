package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.NonStatic;
import com.jimphieffer.game.Player;
import com.jimphieffer.game.Sprite;
import com.jimphieffer.game.Static;
import com.jimphieffer.game.objects.Pig;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ServerGame extends Thread {

    private int mapWidth = 1000;
    private int mapHeight = 1000;
    private Server server;
    private ArrayList<Sprite> sprites;
    private ArrayList<String> spritesNames;

    public ServerGame(Server server) {
        sprites = new ArrayList<>();
        sprites.add(new Pig(100,100));
        spritesNames = new ArrayList<>();
        this.server = server;
    }

    public void run()
    {

        while(true)
        {
            System.out.println("sending?");
            String bruh = "";
            for (Sprite s: sprites)
            {
                bruh+=s.toString() + ",";
            }
            server.relay(Message.encode(bruh, Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));

        }

    }


    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        System.out.println("===============================MESSAGE TO SERVER FROM " + socket.toString() +  "===============================");

        if(message.contains("/") && message.contains(":"))
            type = Message.MessageType.SPRITE;

        System.out.println("message: " + message);



        if(protocol== Message.MessageProtocol.SEND)
        {
            if (type == Message.MessageType.CONNECT)
            {

                StringBuilder bruh = new StringBuilder();
                for (Sprite s: sprites)
                {
                    bruh.append(s.toString()).append(",");
                }
                server.send(Message.encode(bruh.toString().substring(0,bruh.length()-1), Message.MessageProtocol.RELAY,Message.MessageType.CONNECT),socket);

            }
            else if (type == Message.MessageType.DISCONNECT)
            {//USERMAME
                for (int i = 0; i< spritesNames.size(); i++)
                {
                    if(spritesNames.get(i).equals(message)) {
                        sprites.remove(i);
                        spritesNames.remove(i);
                        break;
                    }
                }
            }//protocol == Message.MessageProtocol.SEND

            else if (type == Message.MessageType.SPRITE)
            {
                System.out.println("SPRITE ran (S)");
                addSprites(message);



                String bruh = "";
                for (Sprite s: sprites)
                {
                    s.step();
                    bruh+=s.toString() + ",";
                }
                server.relay(Message.encode(bruh, Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));

            }
        }
        else if (protocol== Message.MessageProtocol.RELAY)
        {
            //TODO
        }
        else
        {
            System.out.println("tried an illegal protocol");
        }

        System.out.println("=============================================================================================");

        System.out.println();
        System.out.println();

    }


    private void addSprites(String message) {

        message = message.substring(message.indexOf("[")+1);
        String[] sprs = message.split(",");
        for (int i = 0; i < sprs.length; i++) {
            boolean matched = false;

            for (int j = 0; j < sprites.size(); j++) {
                String[] onGuh = sprs[i].split(";");
                if (sprites.get(j).getUUID().equals(UUID.fromString(onGuh[6]))) {
                    Sprite s = sprites.get(j);
                    matched = true;
                    if (s instanceof Static)
                        ((Static) s).changeAll(onGuh[1], onGuh[2]);
                    else
                        ((NonStatic) s).changeAll(onGuh[1], onGuh[2], onGuh[7], onGuh[8]);
                    break;
                }
            }
            if(!matched)
                sprites.add(Sprite.stringToSprite(sprs[i]));

        }

    }
}
