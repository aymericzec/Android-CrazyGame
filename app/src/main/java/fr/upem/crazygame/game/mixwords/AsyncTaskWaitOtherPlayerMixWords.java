package fr.upem.crazygame.game.mixwords;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.game.morpion.Cell;
import fr.upem.crazygame.game.morpion.HandlerMorpion;
import fr.upem.crazygame.game.morpion.MorpionActivity;

/**
 * Created by myfou on 16/01/2018.
 */

/**
 * Wait the result of adversary if he win before us the the game finish
 */
public class AsyncTaskWaitOtherPlayerMixWords extends AsyncTask<Void,Void, Void>{

    private final SocketChannel sc;
    private final HandlerMixWords handlerMixWords;
    private final MixWordActivity mixWordActivity;

    public AsyncTaskWaitOtherPlayerMixWords(SocketChannel sc, HandlerMixWords handlerMixWords, MixWordActivity mixWordActivity) {
        this.sc = sc;
        this.handlerMixWords = handlerMixWords;
        this.mixWordActivity = mixWordActivity;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        ByteBuffer bb = ByteBuffer.allocate(1024);

        int idRequest;
        int i = 0;
        int j = 0;

        //Wait the response of other player
        try {
            //bb.limit(Integer.BYTES * 3);
            while (true) {
                bb.limit(4);
                Log.d("ReadFully", "tranquille");
                if (ByteBufferManager.readFully(sc, bb)) {
                    bb.flip();
                    idRequest = bb.getInt();

                    //1 = You Win, 2 = Adversay found Word
                    if (idRequest == 1) {
                        break;
                    } else if (idRequest == 2) {
                        bb.clear();
                        bb.limit(4);
                        //length word
                        if (ByteBufferManager.readFully(sc, bb)) {
                            bb.flip();
                            int l = bb.getInt();
                            bb.clear();
                            bb.limit(l);
                            if (ByteBufferManager.readFully(sc, bb)) {
                                bb.flip();
                                String word = CharsetServer.CHARSET_UTF_8.decode(bb).toString();
                            }
                        }
                    } else if (idRequest == 3) {

                    }
                }
            }
        } catch (IOException e) {
            //Loose connexion with the other client
            e.printStackTrace();
        }
    }
}
