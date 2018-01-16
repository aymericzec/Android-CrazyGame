package fr.upem.crazygame.game.morpion;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import fr.upem.crazygame.R;
import fr.upem.crazygame.morpion.Players;

public class MorpionActivity extends AppCompatActivity {

    private Players player;
    private Morpion morpionGame;
    private ClientMorpion clientMorpion;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion);
        getInformation();
    }

    /**
     * Init and launch the game
     */
    public void getInformation() {

        Players p = getStartPlayer();
        morpionGame = new Morpion(p);
    }

    /**
     * Return the player who must play choose by the server
     */
    private Players getStartPlayer () {
        return Players.PLAYER1;
    }

    /**
     * Return a Client which have the information about the player that we are and the player which begin and information about the oponent
     * @return
     */
    private ClientMorpion getData () {
        return new ClientMorpion();
    }
}
