package fr.upem.crazygame.game.checkGame;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;
import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.game.Players;


/**
 * Wait the result of adversary if he win before us the the game finish
 */
public class AsyncTaskWaitShakeResult extends AsyncTask<Integer, Integer, Players>{

    private final SocketChannel sc;
    private final ShakeGameActivity shakeGameActivity;
    private int result;
    private int scoreAdversary;
    private int score;

    public AsyncTaskWaitShakeResult(SocketChannel sc, ShakeGameActivity shakeGameActivity) {
        this.sc = sc;
        this.shakeGameActivity = shakeGameActivity;
    }


    @Override
    protected Players doInBackground(Integer... ints) {
        ByteBuffer bb = ByteBuffer.allocate(64);
        score = ints[0];

        //Wait the response of other player
        try {
                bb.limit(4);
                Log.d("ReadFully", "tranquille");
                if (ByteBufferManager.readFully(sc, bb)) {
                    Log.d("ReadFully", "Reception du score adversaire");
                    bb.flip();
                    scoreAdversary = bb.getInt();

                    if (scoreAdversary > score) {
                        Log.d("Perdu", "perdu " + scoreAdversary);
                        result = R.string.lose;
                    } else if (scoreAdversary < score) {
                        Log.d("Gagné", "Gagné" + scoreAdversary);
                        result = R.string.win;
                    } else {
                        Log.d("egalité", "egalité" + scoreAdversary);
                        result = R.string.equality;
                    }
                }
        } catch (IOException e) {
            //Loose connexion with the other client
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);


        //Vrai si gagné
        if (values[0] == 1) {

        } else if ((values[0] == 0)) {

        } else {

        }
    }

    @Override
    protected void onPostExecute(Players players) {
        super.onPostExecute(players);
        shakeGameActivity.endGame(result, score, scoreAdversary);
    }
}
