package fr.upem.crazygame.game.mixwords;

import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import fr.upem.crazygame.charset.CharsetServer;
import fr.upem.crazygame.game.morpion.AsyncTaskWaitResult;

/**
 * Created by myfou on 06/02/2018.
 */

public class HandlerMixWords {

    private final SocketChannel sc;
    private ByteBuffer out = ByteBuffer.allocate(128);
    private MixWords mixWords;
    private boolean waitResult = false;
    private AsyncTaskWaitResult asyncTaskWaitResult;


    public HandlerMixWords(SocketChannel sc, MixWords mixWords) {
        this.sc = sc;
        this.mixWords = mixWords;
        this.asyncTaskWaitResult = new AsyncTaskWaitResult();
    }

    public void sendWord(String word) throws IOException {
        if (!waitResult) {
            out.clear();

            ByteBuffer byteBuffer = CharsetServer.CHARSET_UTF_8.encode(word);

            out.putInt(byteBuffer.limit());
            out.put(byteBuffer);
            out.flip();
            sc.write(out);
            waitResult = true;
        }
    }

    public void receiveResult () {
        waitResult = false;
    }
}
