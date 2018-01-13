package fr.upem.crazygame.morpion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.upem.crazygame.R;

public class MorpionActivity extends AppCompatActivity {

    private Players player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion);

        launchMorpion();
    }

    public boolean winGame() {
        return false;
    }

    /**
     * Launch the game
     */
    public void launchMorpion() {

        GameMorpionView morpionView = findViewById(R.id.morpionView);

        // Get the position of the board in the xml layout
        float startXBoard = morpionView.getX();
        float startYBoard = morpionView.getY();
        float widthBoard = morpionView.getWidth();
        float heightBoard = morpionView.getHeight();

        player = Players.PLAYER1;

        while(!winGame()) {

            PlayerMorpionView playerView = new PlayerMorpionView(getApplicationContext());

            playerView.setGameMorpionView(morpionView);
            playerView.setPositionBoard(startXBoard, startYBoard, widthBoard, heightBoard);
            playerView.setPlayer(player);
        }
    }
}
