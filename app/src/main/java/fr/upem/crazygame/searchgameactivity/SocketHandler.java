package fr.upem.crazygame.searchgameactivity;

import java.nio.channels.SocketChannel;


public class SocketHandler {

    private static SocketChannel socket;

    public static synchronized SocketChannel getSocket(){
        return socket;
    }

    public static synchronized void setSocket(SocketChannel socket){
        SocketHandler.socket = socket;
    }
}
