package fr.upem.crazygame.game.mixwords;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
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
public class AsyncTaskWaitOtherPlayerMixWords extends AsyncTask<Void,Void, Cell>{

    private final SocketChannel sc;

    public AsyncTaskWaitOtherPlayerMixWords(SocketChannel sc, HandlerMorpion handlerMorpion, MorpionActivity morpionActivity) {
        this.sc = sc;

    }


    @Override
    protected Cell doInBackground(Void... voids) {
        ByteBuffer bb = ByteBuffer.allocate(1024);

        int idRequest;
        int i = 0;
        int j = 0;

        //Wait the response of other player
        try {
            //bb.limit(Integer.BYTES * 3);
            bb.limit(4 * 3);
            Log.d("ReadFully", "tranquille");
            if (ByteBufferManager.readFully(sc, bb)) {
                bb.flip();
                idRequest = bb.getInt();
                i = bb.getInt();
                j = bb.getInt();

                return new Cell(Players.PLAYER2, i, j);
            }

        } catch (IOException e) {
            //Loose connexion with the other client
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Cell cell) {
        super.onPostExecute(cell);

    }
}
