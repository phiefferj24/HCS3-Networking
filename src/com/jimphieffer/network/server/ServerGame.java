package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.Player;
import com.jimphieffer.game.Sprite;
import com.jimphieffer.game.objects.Pig;

import java.net.Socket;
import java.util.ArrayList;

public class ServerGame extends Thread {

    private int mapWidth = 1000;
    private int mapHeight = 1000;
    private Server server;
    private ArrayList<Sprite> sprites;
    private ArrayList<String> spritesNames;

    public ServerGame(Server server) {
        sprites = new ArrayList<>();
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
        System.out.println("Server Recieved Message");
        if(protocol== Message.MessageProtocol.SEND)
        {
            System.out.println("Client Thread Recieved a send message");
            System.out.println("message to server: " + message);
            //CONNECT: USERNAME
            if (type == Message.MessageType.CONNECT)
            {

                //todo if(name doesnt already exist) {
                sprites.add(new Player(50,50,50,50,"src/com/jimphieffer/game/sprites/player.png",null,0,0,message));
                spritesNames.add(message);
                server.send(Message.encode("SUCCESS",Message.MessageProtocol.RELAY,Message.MessageType.CONNECT),socket);
                //} else {send message FAILED}
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
            else if (type == Message.MessageType.SPRITE)//username,x,y,vy,vy
            {
                System.out.println("itgetshere");
               String[] locs =  message.split(",");
               String username = locs[0];
               double curX = Double.parseDouble(locs[1]);
               double curY = Double.parseDouble(locs[2]);
                double vx = Double.parseDouble(locs[3]);
                double vy = Double.parseDouble(locs[4]);

                for (int i = 0; i< spritesNames.size(); i++)
                {
                    if(spritesNames.get(i).equals(message)) {
                        for(int j = 0; j <sprites.size(); j++) {
                            if (j == i)
                                break;
                            Player player = (Player)sprites.get(i);

                           // System.out.println("itgetstotis");
                            if(!player.touchingAfterDisplacement(sprites.get(j),vx,vy))
                            {
                                server.send(Message.encode("SUCCESS",Message.MessageProtocol.SEND,Message.MessageType.CONNECT),socket);
                            }
                        }
                    }
                }


            }
            else if (type == Message.MessageType.SPRITE)
            {
                sprites.clear();
                for(String s: message.split(","))
                {
                    sprites.add(Sprite.stringToSprite(s));
                }
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


    }
}
