package fr.upem.crazygame.game.morpion;

import android.os.AsyncTask;

import fr.upem.crazygame.morpion.Players;

/**
 * Created by myfou on 15/01/2018.
 */

public class ClientMorpion extends AsyncTask <Void, Void, Void> {
    private Players beginPlayer;
    private Players currentPlayer;

    private boolean isFinish = false;


    public void initData () {

    }

    //Get the players who must begin
    public Players getBeginPlayer() {
        return beginPlayer;
    }

    //Get the number of player of this activity
    public Players getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (!isFinish) {

        }

        return null;
    }
}
