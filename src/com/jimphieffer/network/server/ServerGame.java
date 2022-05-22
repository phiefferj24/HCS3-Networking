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
import java.util.*;

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
    }

    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        System.out.println("\n\nServerGame recieved: " + message + type);
            if (type == Message.MessageType.CONNECT)
            {
                AnnotatedEncoder encoder = new AnnotatedEncoder();

                sprites.forEach(encoder::addAnnotatedObject);
                server.send(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.CONNECT),socket);
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
                System.out.println("was sprite");
                String data = Message.decode(message);
                AnnotatedDecoder decoder = new AnnotatedDecoder(data);
                decoder.addAssignmentMethod(UUID.class, UUID::fromString);
                Sprite[] tempSpritesarr = decoder.getDerivativeObjects(Sprite.class);
                List<Sprite> tempSprites = Arrays.asList(tempSpritesarr);
                sprites.clear();
                sprites.addAll(tempSprites);
//                sprites.replaceAll(sprite -> {
//                    for (int i = 0; i < tempSprites.size(); i++) {
//                        if (sprite.getUUID().equals(tempSprites.get(i).getUUID())) {
//                            return tempSprites.remove(i);
//                        }
//                    }
//                    return sprite;
//                });
                System.out.println("eheheh");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(Sprite sprite : sprites) sprite.step();
                AnnotatedEncoder encoder = new AnnotatedEncoder();
                sprites.forEach(encoder::addObject);
                System.out.println("got here");
                server.relay(Message.encode(encoder.encode(), Message.MessageProtocol.RELAY,Message.MessageType.SPRITE));
                System.out.println("and here");
            }
    }
}
