package fr.upem.crazygame.game.morpion;

import android.os.Handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.game.Players;


/**
 * Created by myfou on 15/01/2018.
 * This class use to represent the connexion with the server and share information with the other player for morpion
 */

public class HandlerMorpion extends Handler {
    private final SocketChannel sc;
    private final Morpion morpion;
    private final MorpionActivity morpionActivity;
    ByteBuffer in = ByteBuffer.allocate(2048);
    ByteBuffer out = ByteBuffer.allocate(2048);



    public HandlerMorpion (SocketChannel sc, MorpionActivity morpionActivity, Morpion morpion) {
        this.sc = sc;
        this.morpionActivity = morpionActivity;
        this.morpion = morpion;
    }

    /**
     * Play around if it's possible
     * @param x
     * @param y
     * @return true if the cell is free false else
     */
    public boolean playAround (int x, int y) {
        return this.morpion.playAround(morpionActivity.getCurrentPlayer(), x, y);
    }

    public void waitOther () {
        AsyncTaskWaitOtherPlayer asyncTaskWaitOtherPlayer = new AsyncTaskWaitOtherPlayer(sc, this, morpionActivity);
        asyncTaskWaitOtherPlayer.execute();
    }

    public void playOtherPlayer (int x, int y) {
        morpion.playAround(((this.morpionActivity.getCurrentPlayer() == Players.PLAYER1) ? Players.PLAYER2 : Players.PLAYER1),x, y);
    }

    public boolean isWinner () {
        return morpion.winner() != null;
    }

    public boolean isEgality () {
        return morpion.isFinish();
    }

    /**
     * Put in the out buffer the data and send to the server
     * @param i
     * @param j
     * @throws IOException if write loose
     */
    public void sendCell(int i, int j) throws IOException {
        out.clear();

        if (morpion.isFinish()) {
            out.putInt(2);
        }
         else {
            out.putInt(1);
        }

        out.putInt(i);
        out.putInt(j);
        out.flip();

        sc.write(out);
    }

    public void looseConnexion() {

    }
}
