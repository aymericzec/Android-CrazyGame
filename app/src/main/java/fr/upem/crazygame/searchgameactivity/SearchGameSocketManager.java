package fr.upem.crazygame.searchgameactivity;

import android.os.StrictMode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by myfou on 31/01/2018.
 */

/**
 * This class connect a socket to server
 */
public class SearchGameSocketManager {
    private final SocketChannel sc;
    private boolean isConnected = false;

    private SearchGameSocketManager (SocketChannel sc) {
        this.sc = sc;
    }

    public void connectSocket (String hostname, int port) {
            InetSocketAddress serverAddress =  new InetSocketAddress(hostname,port);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            final InetSocketAddress serverAddressTmp = serverAddress;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sc.connect(serverAddressTmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isConnected = true;
                }
            }).start();
    }

    public static SearchGameSocketManager createSearchGameSocketManager (SearchGameActivity searchGameActivity) throws IOException {
        SocketChannel sc = SocketChannel.open();
        return new SearchGameSocketManager(sc);
    }

    public SearchGameManager isConnected() {
        if (isConnected) {
            return new SearchGameManager(this.sc);
        }

        return null;
    }
}
