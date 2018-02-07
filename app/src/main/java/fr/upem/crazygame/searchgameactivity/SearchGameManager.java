package fr.upem.crazygame.searchgameactivity;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;

import fr.upem.crazygame.bytebuffer_manager.ByteBufferManager;
import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.game.mixwords.MixWordActivity;
import fr.upem.crazygame.game.morpion.MorpionActivity;

/**
 * Created by myfou on 31/01/2018.
 */

public class SearchGameManager {
    private final SocketChannel socketChannel;
    private ByteBuffer in = ByteBuffer.allocate(4096);
    private ByteBuffer out = ByteBuffer.allocate(4096);
    private final SearchGameActivity searchGameActivity;

    public SearchGameManager (SocketChannel socketChannel, SearchGameActivity searchGameActivity) {
        this.socketChannel = socketChannel;
        this.searchGameActivity = searchGameActivity;
    }

    public void sendSearchGame (String nameGame) throws IOException {
        this.in.clear();
        this.in.putInt(1);
        ByteBuffer tmp = CharsetServer.CHARSET_UTF_8.encode(nameGame);
        Log.d("Longuer de " + nameGame, tmp.position() + " " + tmp.limit());
        this.in.putInt(tmp.limit()); //lenght name game
        this.in.put(tmp); //name game
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
                Log.d("toto", "Recherche d'une partie");
                //Read the response of server
                try {
                    SearchGameManager.this.out.clear();
                    //SearchGameManager.this.out.limit(Integer.BYTES*2);
                    SearchGameManager.this.out.limit(4*2);
                    if (ByteBufferManager.readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
                        SearchGameManager.this.out.flip();
                        int action = SearchGameManager.this.out.getInt(); //must be 1 to say, game found
                        int lengthNameGame = SearchGameManager.this.out.getInt();

                        Log.d("toto", action + " " + lengthNameGame);

                        SearchGameManager.this.out.compact();
                        SearchGameManager.this.out.limit(lengthNameGame);

                        if (ByteBufferManager.readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
                            SearchGameManager.this.out.flip();
                            String name = CharsetServer.CHARSET_UTF_8.decode(SearchGameManager.this.out).toString();

                            //Switch between name game
                            switch (name) {
                                case "Morpion":
                                    launchMorpion();
                                    break;
                                case "MixWord":
                                    launchMixWord();
                                    break;
                                default:
                                    break;
                            }



                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void launchMorpion () throws IOException {
        SearchGameManager.this.out.compact();
        //SearchGameManager.this.out.limit(Integer.BYTES);
        SearchGameManager.this.out.limit(4);
        if (ByteBufferManager.readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
            SearchGameManager.this.out.flip();
            final int whoBegin = SearchGameManager.this.out.getInt();
            SearchGameManager.this.out.compact();
            Log.d("Partie trouvé Morpion", "Partie trouvé");
            //Launch Activity
            Intent intent = new Intent(SearchGameManager.this.searchGameActivity, MorpionActivity.class);
            intent.putExtra("begin", whoBegin);
            SocketHandler.setSocket(socketChannel);
            SearchGameManager.this.searchGameActivity.launchGameActivity(intent);
        }
    }

    private void launchMixWord () throws IOException {
        SearchGameManager.this.out.compact();
        //SearchGameManager.this.out.limit(Integer.BYTES);
        SearchGameManager.this.out.limit(4);
        if (ByteBufferManager.readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
            SearchGameManager.this.out.flip();
            final int lengthWord = SearchGameManager.this.out.getInt();
            SearchGameManager.this.out.compact();
            SearchGameManager.this.out.limit(lengthWord);

            if (ByteBufferManager.readFully(SearchGameManager.this.socketChannel, SearchGameManager.this.out)) {
                Log.d("Partie trouvé MixWord", "Partie trouvé");
                SearchGameManager.this.out.flip();
                String word =   CharsetServer.CHARSET_UTF_8.decode(SearchGameManager.this.out).toString();
                //Launch Activity
                Intent intent = new Intent(SearchGameManager.this.searchGameActivity, MixWordActivity.class);
                intent.putExtra("wordSearch", word);
                SocketHandler.setSocket(socketChannel);
                SearchGameManager.this.searchGameActivity.launchGameActivity(intent);
            }
        }
    }
}
