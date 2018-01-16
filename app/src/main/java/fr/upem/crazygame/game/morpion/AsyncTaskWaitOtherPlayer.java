package fr.upem.crazygame.game.morpion;

import android.os.AsyncTask;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by myfou on 16/01/2018.
 */

public class AsyncTaskWaitOtherPlayer extends AsyncTask<Void,Void, Cell>{

    private final SocketChannel sc;
    private final HandlerMorpion handlerMorpion;
    public AsyncTaskWaitOtherPlayer(SocketChannel sc, HandlerMorpion handlerMorpion) {
        this.sc = sc;
        this.handlerMorpion = handlerMorpion;
    }


    @Override
    protected Cell doInBackground(Void... voids) {
        ByteBuffer bb = ByteBuffer.allocate(1024);

        //Wait the response of other player
        try {
            sc.read(bb);
        } catch (IOException e) {
            //Loose connexion with the other client
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Cell cell) {
        super.onPostExecute(cell);

        int x = cell.getX();
        int y = cell.getY();

        handlerMorpion.playOtherPlayer(x, y);
    }
}
