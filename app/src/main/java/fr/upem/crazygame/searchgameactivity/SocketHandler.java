package fr.upem.crazygame.searchgameactivity;

import java.nio.channels.SocketChannel;


/**
 * Représente un singleton d'une socket channel afin de la transférer dans chaque activité.
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
