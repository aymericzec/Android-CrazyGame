package fr.upem.crazygame.game.morpion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import fr.upem.crazygame.R;
import fr.upem.crazygame.morpion.GameMorpionView;
import fr.upem.crazygame.morpion.PlayerMorpionView;
import fr.upem.crazygame.morpion.Players;

public class MorpionActivity extends AppCompatActivity {
    private final static int NUMBER_CELL = 3;

    private Players player;
    private HandlerMorpion handlerMorpion;
    private SocketChannel sc;
    private Button[][] cases = new Button[NUMBER_CELL][NUMBER_CELL];

    private TextView playerLeft;
    private TextView playerRight;
    private TextView messageBottom;

    private boolean isTurn = false; //It's the turn of current player

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion);

        Intent intent = getIntent();
        //Player begin, Player current
        Players begin = (Players) intent.getSerializableExtra("playerBegin");
        Players current = (Players) intent.getSerializableExtra("playerCurrent");

        //Init UI
        initButton();

        playerLeft = findViewById(R.id.joueur1);
        playerRight = findViewById(R.id.joueur2);
        messageBottom = findViewById(R.id.turn);

        isTurn = begin.equals(current);

        handlerMorpion = new HandlerMorpion (sc, this, new Morpion(begin, current));
    }

    /**
     * When the player click on a cell of gameboard
     * @param view
     */
    public void clickCell (View view) {
        if (isTurn) {
            Button b = (Button) view;

            for (int i = 0; i < MorpionActivity.NUMBER_CELL; i++) {
                for (int j = 0; j < MorpionActivity.NUMBER_CELL; j++) {
                    if (b.equals(cases[i][j])) {
                        this.handlerMorpion.playAround(i, j);
                        isTurn = false;
                        waitOtherPlayer();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Wait other player
     */
    public void waitOtherPlayer () {
        if (!isTurn) {
            this.handlerMorpion.waitOther();
        }
    }

    public void isYourTurn () {
        isTurn = true;
    }

    /**
     * Init the gameBoard
     */
    public void initButton () {
        cases[0][0] = ((Button)findViewById(R.id.button0));
        cases[0][1] = ((Button)findViewById(R.id.button1));


        cases[1][0] = ((Button)findViewById(R.id.button3));
        cases[1][1] = ((Button)findViewById(R.id.button4));
        cases[1][2] = ((Button)findViewById(R.id.button5));

        cases[2][0] = ((Button)findViewById(R.id.button6));
        cases[2][1] = ((Button)findViewById(R.id.button7));
        cases[2][2] = ((Button)findViewById(R.id.button8));
    }

    public Button[][] getCases() {
        return cases;
    }

    public Players getPlayer() {
        return player;
    }

    public SocketChannel getSc() {
        return sc;
    }

    public TextView getPlayerLeft() {
        return playerLeft;
    }

    public TextView getPlayerRight() {
        return playerRight;
    }

    public TextView getMessageBottom() {
        return messageBottom;
    }
}
