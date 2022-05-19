package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.objectTypes.NonStatic;
import com.jimphieffer.game.objectTypes.Sprite;
import com.jimphieffer.game.objectTypes.Static;
import com.jimphieffer.game.objects.Pig;
import com.jimphieffer.game.objects.Tree;
import com.jimphieffer.utils.json.AnnotatedDecoder;
import com.jimphieffer.utils.json.AnnotatedEncoder;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class ServerGame extends Thread {

    private int mapWidth = 1000;
    private int mapHeight = 1000;
    private Server server;
    private ArrayList<Sprite> sprites;
    private ArrayList<String> spritesNames;

    public ServerGame(Server server) {
        sprites = new ArrayList<>();
        sprites.add(new Pig(100,100));
        sprites.add(new Tree(150,150, 100, 100, "/textures/wood.png", UUID.randomUUID()));
        spritesNames = new ArrayList<>();
        this.server = server;
      //  runT();
    }

    public void runT()
    {
        Thread t = new Thread(() ->
        {
            while(true)
            {

                String bruh = "";
                for (Sprite s: sprites)
                {
                        s.step();
                        bruh += s.toString() + ",";
                }
                server.relay(Message.encode(bruh, Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));
            }
        });



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

                AnnotatedEncoder encoder = new AnnotatedEncoder();

                for (Sprite s: sprites)
                {
                    encoder.addObject(s);
                }
                server.send(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.CONNECT),socket);

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
                int numSteps = parseInt(message.substring(message.indexOf(":")+1,message.indexOf(">")));
                addSprites(message.substring(message.indexOf(">")+1));
                for(int f = 0; f<numSteps; f++)
                    for (Sprite s: sprites)
                        s.step();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AnnotatedEncoder encoder = new AnnotatedEncoder();
                for (Sprite s: sprites)
                {
                    encoder.addObject(s);
                }
                server.relay(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));

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

        AnnotatedDecoder decoder = new AnnotatedDecoder(message);
        decoder.addAssignmentMethod(UUID.class, UUID::fromString);

        Sprite[] tempSprites = decoder.getDerivativeObjects(Sprite.class);
        System.out.println("in ServerGame: " + tempSprites.length);
        for(Sprite s : tempSprites) {
            boolean found = false;
            for(int i = 0; i < sprites.size(); i++) {
                if(s.getUUID().equals(sprites.get(i).getUUID())) {
                    found = true;
                    sprites.set(i, s);
                }
            }
            if(!found) {
                sprites.add(s);
            }
        }

    }
}
