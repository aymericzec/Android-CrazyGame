package fr.upem.crazygame.game.mixwords;

import android.widget.Button;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.charset.CharsetServer;

/**
 * Created by myfou on 06/02/2018.
 */

public class HandlerMixWords {

    private final SocketChannel sc;
    private ByteBuffer out = ByteBuffer.allocate(128);
    private MixWords mixWords;
    private boolean waitResult = false;
    private AsyncTaskWaitResult asyncTaskWaitOtherPlayerMixWords;


    public HandlerMixWords(SocketChannel sc, MixWords mixWords, MixWordActivity mixWordActivity) {
        this.sc = sc;
        this.mixWords = mixWords;
        this.asyncTaskWaitOtherPlayerMixWords = new AsyncTaskWaitResult(sc, this, mixWordActivity);
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

    public void addLetter(Button keypadTop[], Button click) {
        String letter = click.getText().toString();
        int i = mixWords.addCaracter(letter);
        keypadTop[i].setText(letter);
        keypadTop[i].setClickable(true);
        click.setText("");
        click.setClickable(false);
    }

    public void removeLetter(Button keypadBottom[], Button click, int i) {
        mixWords.removeCaracter(i);
        keypadBottom[i].setText(click.getText());
        keypadBottom[i].setClickable(true);
        click.setText("");
        click.setClickable(false);
    }
}
