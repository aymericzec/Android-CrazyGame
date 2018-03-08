package fr.upem.crazygame.game.morpion;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;
import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;

/**
 * Created by myfou on 16/01/2018.
 */

public class AsyncTaskWaitOtherPlayer extends AsyncTask<Void,Void, Cell>{

    private final SocketChannel sc;
    private final HandlerMorpion handlerMorpion;
    private final MorpionActivity morpionActivity;

    public AsyncTaskWaitOtherPlayer(SocketChannel sc, HandlerMorpion handlerMorpion, MorpionActivity morpionActivity) {
        this.sc = sc;
        this.handlerMorpion = handlerMorpion;
        this.morpionActivity = morpionActivity;
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
                morpionActivity.myTurnGraphic();

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

        Log.d("Fin de la tâche", cell.getX() + " " + cell.getY());
        if (cell != null) {
            int x = cell.getX();
            int y = cell.getY();

              handlerMorpion.playOtherPlayer(x, y);
            morpionActivity.putClickAdvsersary(x, y);

            if (!(handlerMorpion.isWinner() || handlerMorpion.isEgality())) {
                morpionActivity.setYourTurn();

            } else {
                Context context = morpionActivity.getApplicationContext();
                CharSequence text;
                if (handlerMorpion.isWinner()) {
                    ProviderDataGame.addWinGame(GameCrazyGameColumns.NAME_MORPION, morpionActivity);
                   text = context.getString(R.string.lose);
                } else {
                    text = context.getString(R.string.equality);
                }

                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        } else {
            handlerMorpion.looseConnexion();
        }

    }
}
