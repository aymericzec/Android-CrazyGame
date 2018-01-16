package fr.upem.crazygame.maintest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;

/**
 * Created by myfou on 15/01/2018.
 */

public class SearchGameMorpion extends Activity {
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_morpion);

        SocketChannel sc = null;


        try {
            sc = SocketChannel.open();
            InetSocketAddress serverAddress =  new InetSocketAddress("192.168.1.13",8086);
            final SocketChannel s = sc;
            final InetSocketAddress ss = serverAddress;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.connect(ss);

                        Log.d("Connexion Réussi", ss.getHostName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Test", e.getMessage());
        }
    }
}

