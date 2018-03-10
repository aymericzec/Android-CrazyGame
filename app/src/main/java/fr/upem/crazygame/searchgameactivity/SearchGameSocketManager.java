package fr.upem.crazygame.searchgameactivity;

import android.os.StrictMode;
import android.util.Log;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import fr.upem.crazygame.charset.CharsetServer;

/**
 * Created by myfou on 31/01/2018.
 */

/**
 * This class connect a socket to server
 */
public class SearchGameSocketManager implements Serializable {
    private final SocketChannel sc;
    private final SearchGameActivity searchGameActivity;
    private boolean isConnected = false;

    private SearchGameSocketManager (SocketChannel sc, SearchGameActivity searchGameActivity) {
        this.sc = sc;
        this.searchGameActivity = searchGameActivity;
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
                        sendLanguage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isConnected = true;
                }
            }).start();
    }

    public static SearchGameSocketManager createSearchGameSocketManager (SearchGameActivity searchGameActivity) throws IOException {
        SocketChannel sc = SocketChannel.open();
        SocketHandler.setSocket(sc);
        return new SearchGameSocketManager(sc, searchGameActivity);
    }

    public SearchGameManager isConnected() {
        if (isConnected) {
            return new SearchGameManager(this.sc, this.searchGameActivity);
        }

        return null;
    }


    /**
     * Send the language of device, first request
     */
    private void sendLanguage () throws IOException {
        ByteBuffer in = ByteBuffer.allocate(16);


        ByteBuffer buffer = CharsetServer.CHARSET_UTF_8.encode(Locale.getDefault().getLanguage());
        Log.d("Envoie Name Language de ", buffer.limit() + "");
        in.putInt(buffer.limit());

        in.put(buffer);//fr en...

        Log.d("Envoie Name Language de ", Locale.getDefault().getLanguage());
        in.flip();
        sc.write(in);
    }
}
