package fr.upem.crazygame.game.morpion;

import android.os.Handler;

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
        boolean clickOk = this.morpion.playAround(morpionActivity.getCurrentPlayer(), x, y);

        if (clickOk) {
            this.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        return clickOk;
    }

    public void waitOther () {
        AsyncTaskWaitOtherPlayer asyncTaskWaitOtherPlayer = new AsyncTaskWaitOtherPlayer(sc, this);
    }

    public void playOtherPlayer (int x, int y) {
        morpion.playAround(((this.morpionActivity.getCurrentPlayer() == Players.PLAYER1) ? Players.PLAYER2 : Players.PLAYER1),x, y);

        //Other player have play
        this.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}
