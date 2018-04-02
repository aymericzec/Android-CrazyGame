package fr.upem.crazygame.game.mixwords;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;


/**
 * Wait the result of adversary if he win before us the the game finish
 */
public class AsyncTaskWaitResult extends AsyncTask<Void,Void, Players>{

    private final SocketChannel sc;
    private final HandlerMixWords handlerMixWords;
    private final MixWordActivity mixWordActivity;
    private String word = null;

    public AsyncTaskWaitResult(SocketChannel sc, HandlerMixWords handlerMixWords, MixWordActivity mixWordActivity) {
        this.sc = sc;
        this.handlerMixWords = handlerMixWords;
        this.mixWordActivity = mixWordActivity;
    }


    @Override
    protected Players doInBackground(Void... voids) {
        ByteBuffer bb = ByteBuffer.allocate(64);

        int idRequest;
        int i = 0;
        int j = 0;

        //Wait the response of other player
        try {
            //bb.limit(Integer.BYTES * 3);
            while (true) {
                bb.limit(4);
                if (ByteBufferManager.readFully(sc, bb)) {
                    bb.flip();
                    idRequest = bb.getInt();
                    //1 = You Win, 2 = Adversay found Word, 3 Bad Word
                    if (idRequest == 1) {
                        ProviderDataGame.addWinGame(GameCrazyGameColumns.NAME_MIXWORD, mixWordActivity);
                        return Players.PLAYER1;
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
                                this.word = CharsetServer.CHARSET_UTF_8.decode(bb).toString();
                                return Players.PLAYER2;
                            }
                        }
                    } else if (idRequest == 3) {
                        Log.d("Pas le bon mot", "Ce n'est pas le bon");
                    }
                    bb.clear();
                    if (idRequest == 3) {
                        handlerMixWords.receiveResult();
                    }

                }
            }
        } catch (IOException e) {
            //Loose connexion with the other client
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Players players) {
        super.onPostExecute(players);

        mixWordActivity.endGame(players.equals(Players.PLAYER1), this.word);
    }
}
