package fr.upem.crazygame.classement;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.game.checkGame.ShakeGameActivity;
import fr.upem.crazygame.searchgameactivity.SocketHandler;

/**
 * Created by myfou on 16/01/2018.
 */

/**
 * Wait the result of adversary if he win before us the the game finish
 */
public class AsyncTaskWaitScoreWorld extends AsyncTask<Void, Void, List<Classement>> {
    private final ClassementActivity classementActivity;

    public AsyncTaskWaitScoreWorld(ClassementActivity classementActivity) {
        this.classementActivity = classementActivity;
    }


    @Override
    protected List<Classement> doInBackground(Void ... v) {
        SocketChannel sc = SocketHandler.getSocket();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ArrayList<Classement> classements = new ArrayList<>();

        Log.d("Size", "Protocole");
        boolean sendSucces = sendProtocol(sc, buffer);
        Log.d("Size","OK");
        if (sendSucces) {
            try {
                Log.d("Size", "LALALALs");
                int size = size(sc, buffer);
                Log.d("Size", "LALALALs " + size);
                int cpt = 0;
                Log.d("Size", String.valueOf(size));
                //On lit tous les mots
                while (cpt != size) {
                    int sizeWord = size(sc, buffer);
                    Log.d("Size", String.valueOf(sizeWord));
                    String nameGame = word(sc, buffer, sizeWord);
                    Log.d("Size", nameGame);
                    int score = size(sc, buffer);
                    classements.add(new Classement(nameGame, score));
                    cpt++;
                }

                return classements;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... v) {
        super.onProgressUpdate();
    }

    @Override
    protected void onPostExecute(List<Classement> classements) {
        super.onPostExecute(classements);
        classementActivity.initList(classements);
    }


    private boolean sendProtocol (SocketChannel sc, ByteBuffer buffer) {
        buffer.putInt(4300);
        buffer.flip();
        try {
            Log.d("socket", sc + "");
            sc.write(buffer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int size (SocketChannel sc, ByteBuffer buffer) throws IOException {
        buffer.clear();
        buffer.limit(4);
        if (ByteBufferManager.readFully(sc, buffer)) {
            buffer.flip();
            return buffer.getInt();
        }

        return -1;
    }

    private String word (SocketChannel sc, ByteBuffer buffer, int sizeWord) throws IOException {
        buffer.clear();
        buffer.limit(sizeWord);
        if (ByteBufferManager.readFully(sc, buffer)) {
            buffer.flip();
            return CharsetServer.CHARSET_UTF_8.decode(buffer).toString();
        }
        return null;
    }
}
