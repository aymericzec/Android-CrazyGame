package fr.upem.crazygame.searchgameactivity;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.charset.CharsetServer;

/**
 * Created by myfou on 31/01/2018.
 */

public class SearchGameManager {
    private final SocketChannel socketChannel;
    private ByteBuffer in = ByteBuffer.allocate(4096);
    private ByteBuffer out = ByteBuffer.allocate(4096);

    public SearchGameManager (SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void sendSearchGame (String nameGame) throws IOException {
        this.in.clear();
        this.in.putInt(1);
        ByteBuffer tmp = CharsetServer.CHARSET_UTF_8.encode(nameGame);
        Log.d("Longuer de " + nameGame, tmp.position() + " " + tmp.limit());
        this.in.putInt(tmp.limit());
        this.in.put(tmp);

        this.in.flip();

        this.socketChannel.write(this.in);
    }

    /**
     * Read byte receive while all receive for change activity game
     */
    public void waitFoundGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Read the response of server
                try {
                    SearchGameManager.this.out.clear();
                    SearchGameManager.this.out.limit(Integer.BYTES*2);
                    if (readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
                        SearchGameManager.this.out.flip();
                        int action = SearchGameManager.this.out.getInt(); //must be 1 to say, game found
                        int lengthNameGame = SearchGameManager.this.out.getInt();
                        SearchGameManager.this.out.compact();
                        SearchGameManager.this.out.limit(lengthNameGame);

                        if (readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
                            SearchGameManager.this.out.flip();
                            String name = CharsetServer.CHARSET_UTF_8.decode(SearchGameManager.this.out).toString();

                            SearchGameManager.this.out.compact();
                            SearchGameManager.this.out.limit(Integer.BYTES);
                            final int whoBegin = SearchGameManager.this.out.getInt();

                            SearchGameManager.this.out.compact();

                            //Launch Activity
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("Partie trouvé", "Partie trouvé");
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean readFully(SocketChannel sc, ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            if (sc.read(buffer) == -1) {
                return false;
            }
        }

        return true;
    }
}
