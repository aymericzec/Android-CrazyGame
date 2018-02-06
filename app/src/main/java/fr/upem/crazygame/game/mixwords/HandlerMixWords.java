package fr.upem.crazygame.game.mixwords;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by myfou on 06/02/2018.
 */

public class HandlerMixWords {

    private final SocketChannel sc;
    ByteBuffer out = ByteBuffer.allocate(2048);

    public HandlerMixWords(SocketChannel sc) {
        this.sc = sc;
    }

    public void sendWord(String word) throws IOException {
        out.clear();
        out.putInt(word.length());
        out.put(Charset.forName("UTF-8").encode(word));
        out.flip();
        sc.write(out);
    }
}
