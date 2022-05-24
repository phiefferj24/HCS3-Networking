package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.Player;
import com.jimphieffer.game.objectTypes.NonStatic;
import com.jimphieffer.game.objectTypes.Sprite;
import com.jimphieffer.game.objectTypes.Static;
import com.jimphieffer.game.objects.Pig;
import com.jimphieffer.game.objects.Stone;
import com.jimphieffer.game.objects.Tree;
import com.jimphieffer.game.objects.Wall;
import com.jimphieffer.utils.json.AnnotatedDecoder;
import com.jimphieffer.utils.json.AnnotatedEncoder;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Integer.parseInt;

public class ServerGame extends Thread {

    private int mapWidth = 1000;
    private int mapHeight = 1000;
    private Server server;
    private ArrayList<Sprite> sprites;
    private ArrayList<String> spritesNames;
    public BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    public ServerGame(Server server) {
        sprites = new ArrayList<>();
        sprites.add(new Pig(100,100));
        sprites.add(new Wall(400,400));
        sprites.add(new Stone(50,50, 25, 25, "/textures/stone.png", UUID.randomUUID()));
        sprites.add(new Tree(150,150, 15, 15, "/textures/wood.png", UUID.randomUUID()));
        spritesNames = new ArrayList<>();
        this.server = server;
        // create a new thread that calls onMessage whenever a new message is added to the queue
        new Thread(() -> {
            while (true) {
                try {
                    String message = messages.take();
                    onMessage(Message.decode(message), Message.getProtocol(message), Message.getType(message));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type) {
        //System.out.println("\n\nServerGame recieved: " + message + " - " + type);
            if (type == Message.MessageType.CONNECT)
            {
                AnnotatedEncoder encoder = new AnnotatedEncoder();

                sprites.forEach(encoder::addAnnotatedObject);
                server.relay(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.CONNECT));
            }
            else if (type == Message.MessageType.DISCONNECT)
            {//USERMAME
                for (int i = 0; i < spritesNames.size(); i++)
                {
                    if(spritesNames.get(i).equals(message)) {
                        sprites.remove(i);
                        spritesNames.remove(i);
                        break;
                    }
                }
            }//protocol == Message.MessageProtocol.SEND

            else if (type == Message.MessageType.SPRITE || type == Message.MessageType.MESSAGE)
            {
                int numSteps = Integer.parseInt(message.substring(message.indexOf(":")+1,message.indexOf("<")));
                String data = Message.decode("SEND/SPRITE:"+message.substring(message.indexOf("<")+1));
                AnnotatedDecoder decoder = new AnnotatedDecoder(data);
                decoder.addAssignmentMethod(UUID.class, UUID::fromString);
                Sprite[] tempSpritesarr = decoder.getDerivativeObjects(Sprite.class);

                for (Sprite sprite : tempSpritesarr) {
                    for(int i = 0; i < sprites.size(); i++) {
                        if(sprites.get(i).getUUID().equals(sprite.getUUID())) {
                            sprites.set(i, sprite);
                        }
                    }
                }
//                sprites.clear();
//                sprites.addAll(tempSprites);

//                for(int i = 0; i<sprites.size(); i++)
//                    if (sprites.get(i) instanceof Player)
//                        sprites.set(i,tempSpritesarr[0]);



                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sprites.forEach((s) -> s.step(numSteps));

                AnnotatedEncoder encoder = new AnnotatedEncoder();
                sprites.forEach(encoder::addAnnotatedObject);
                server.relay(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));
            }
    }
}
