 package fr.upem.crazygame.game.morpion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;
import fr.upem.crazygame.game.Players;

 public class MorpionActivity extends AppCompatActivity {
    private final static int NUMBER_CELL = 3;

    private HandlerMorpion handlerMorpion;
    private SocketChannel sc;
    private Button[][] cases = new Button[NUMBER_CELL][NUMBER_CELL];

    private TextView playerLeft;
    private TextView playerRight;
    private TextView messageBottom;

    private Players currentPlayer = null;

    private boolean isTurn = false; //It's the turn of current player

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion_p);
        getInformation();
    }

    /**
     * Init and launch the game
     */
    public void getInformation() {
        Intent intent = getIntent();
        Players begin = (Players) intent.getSerializableExtra("playerBegin");
        currentPlayer = (Players) intent.getSerializableExtra("playerCurrent");

        //Init UI
        initButton();

        playerLeft = findViewById(R.id.player1);
        playerRight = findViewById(R.id.player2);
        messageBottom = findViewById(R.id.round);

        isTurn = begin.equals(currentPlayer);

        handlerMorpion = new HandlerMorpion (sc, this, new Morpion(begin, currentPlayer));
    }

    /**
     * Return the player who must play choose by the server
     * When the player click on a cell of gameboard
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
     * @return
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

    public Players getCurrentPlayer() {
        return currentPlayer;
    }
}