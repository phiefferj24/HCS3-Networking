package com.jimphieffer.network.server;

import com.jimphieffer.Message;
import com.jimphieffer.game.Player;
import com.jimphieffer.game.Sprite;
import com.jimphieffer.game.Window;
import org.lwjgl.opengl.GL;

import java.net.Socket;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

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

    }


    public void onMessage(String message, Message.MessageProtocol protocol, Message.MessageType type, Socket socket) {
        if(protocol== Message.MessageProtocol.SEND)
        {
            System.out.println("message sent: " + message);
            message = Message.decode(message);
            //CONNECT: USERNAME
            if (type == Message.MessageType.CONNECT)
            {
                sprites.add(new Player(50,50,50,50,"src/com/jimphieffer/game/sprites/player.png",0,0,message));
                spritesNames.add(message);
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
            }
            else if (type == Message.MessageType.MOVEMENT)//username,x,y,vy,vy
            {
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


                            if(!player.touchingAfterDisplacement(sprites.get(j),vx,vy));
                            {
                                //TODO send message to client thred with updated locations

                            }
                        }
                    }
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
