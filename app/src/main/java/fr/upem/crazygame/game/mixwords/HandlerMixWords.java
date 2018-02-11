package fr.upem.crazygame.game.mixwords;

import android.util.Log;
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
    private AsyncTaskWaitResult asyncTaskWaitResult;


    public HandlerMixWords(SocketChannel sc, MixWords mixWords, MixWordActivity mixWordActivity) {
        this.sc = sc;
        this.mixWords = mixWords;
        this.asyncTaskWaitResult = new AsyncTaskWaitResult(sc, this, mixWordActivity);
        this.asyncTaskWaitResult.execute();
    }

    public void sendWord() throws IOException {
        if (!waitResult) {
            out.clear();

            ByteBuffer byteBuffer = CharsetServer.CHARSET_UTF_8.encode(mixWords.getWord());

            out.putInt(byteBuffer.limit());
            out.put(byteBuffer);
            out.flip();
            Log.d("Envoie d'un mot", mixWords.getWord());
            sc.write(out);
            waitResult = true;
        }
    }

    public void receiveResult () {
        waitResult = false;
    }

    public void waitRequest () {
        asyncTaskWaitResult.execute();
    }

    public void addLetter(Button clickBottom, Button [] topButtons) {
        String letter = clickBottom.getText().toString();
        int i = mixWords.addCaracter(letter);
        topButtons[i].setText(letter);
        topButtons[i].setClickable(true);
        clickBottom.setText("");
        clickBottom.setClickable(false);
    }

    public void removeLetter(Button clickButton, Button[] bottomButtons, int i) {
        mixWords.removeCaracter(i);
        int j;

        for (j = 0; j < bottomButtons.length; j++) {
            if (bottomButtons[j].getText().equals("")) {
                break;
            }
        }
        bottomButtons[j].setText(clickButton.getText());
        bottomButtons[j].setClickable(true);
        clickButton.setText("");
        clickButton.setClickable(false);
    }
}
