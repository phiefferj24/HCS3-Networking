package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.objects.Player;
import com.jimphieffer.game.Sprite;
import com.jimphieffer.game.objects.Wall;

import java.net.Socket;
import java.util.ArrayList;

public class ServerGame extends Thread {

    private int mapWidth = 1000;
    private int mapHeight = 1000;
    private Server server;
    private ArrayList<Sprite> sprites;


    public ServerGame(Server server) {
        sprites = new ArrayList<>();
        this.server = server;

        sprites.add(new Wall(100,100,40,40));
    }

    public void run()
    {

        StringBuilder message = new StringBuilder();
        for(Sprite s: sprites)
            message.append(s.toString()).append(",");
        server.relay(Message.encode(message.toString(), Message.MessageProtocol.SEND, Message.MessageType.SPRITE));

    }


    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        if(protocol== Message.MessageProtocol.SEND)
        {
            if(type!=null)
                run(); //TODO this is bad... run() should be running all the time on its own but it wasnt so now I just run everytime a message is sent

            //CONNECT: USERNAME
            if (type == Message.MessageType.CONNECT)
            {
                //todo if(name doesnt already exist) {
                Player p = new Player(50,50,50,50,"src/com/jimphieffer/game/sprites/player.png",0,0,message);
                p.setSocket(socket);
                sprites.add(p);
                server.send(Message.encode("SUCCESS",Message.MessageProtocol.RELAY,Message.MessageType.CONNECT),socket);
                //} else {send message FAILED}
            }
            else if (type == Message.MessageType.DISCONNECT)
            {//USERMAME
                for (int i = 0; i< sprites.size(); i++)
                {
                    Sprite s = sprites.get(i);
                    if(s instanceof Player && ((Player) s).getUsername().equals(Message.decode(message)))
                        sprites.remove(i);
                }
            }
            else if (type == Message.MessageType.MOVEMENT)//username,x,y,vy,vy SHOULD ONLY PRETAIN TO PLAYER OBJECTS
            {


                double curX = 0;
                double curY = 0;
                double vx = 0;
                double vy = 0;

                message = Message.decode(message);
               String[] locs =  message.split(",");
                   String username = locs[0];
                   try {
                       curX = Double.parseDouble(locs[1]);
                       curY = Double.parseDouble(locs[2]);
                       vx = Double.parseDouble(locs[3]);
                       vy = Double.parseDouble(locs[4]);
                   }
                   catch (NumberFormatException e)
                   {
                       System.out.println(message);
                       System.out.println(e.getMessage());
                   }



                Player p = null;
                for (Sprite spr : sprites) {
                    if (spr instanceof Player && ((Player) spr).getUsername().equals(username)) {
                        p = (Player) spr;
                    }
                }

                if (p==null)
                {
                    throw new RuntimeException("Username of sprite that sent movement command did not match those of the server. :)");
                }

                //server.send(Message.encode(curX + ", " + curY + ", " + vx + ", " + vy,Message.MessageProtocol.SEND, Message.MessageType.MOVEMENT),socket);

//                for (int i = 0; i< spritesNames.size(); i++) TODO collisions
//                {
//                    if(spritesNames.get(i).equals(message)) {
//                        for(int j = 0; j <sprites.size(); j++) {
//                            if (j == i)
//                                break;
//                            Player player = (Player)sprites.get(i);
//
//
//                            if(!player.touchingAfterDisplacement(sprites.get(j),vx,vy))
//                            {
//                                server.send(Message.encode(curX + ", " + curY + ", " + vx + ", " + vy,Message.MessageProtocol.SEND,Message.MessageType.CONNECT),socket);
//                            }
//                        }
//                    }
//                }


                p.setX(curX);
                p.setY(curY);
                p.setVX(vx);
                p.setVY(vy);

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
