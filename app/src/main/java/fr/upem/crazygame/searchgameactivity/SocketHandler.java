package fr.upem.crazygame.searchgameactivity;

import java.nio.channels.SocketChannel;

/**
 * Created by myfou on 03/02/2018.
 */

public class SocketHandler {
    private static SocketChannel socket;

    public static synchronized SocketChannel getSocket(){
        return socket;
    }

    public static synchronized void setSocket(SocketChannel socket){
        SocketHandler.socket = socket;
    }
}
