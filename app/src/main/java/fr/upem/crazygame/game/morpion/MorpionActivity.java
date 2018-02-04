 package fr.upem.crazygame.game.morpion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.searchgameactivity.SearchGameActivity;
import fr.upem.crazygame.searchgameactivity.SocketHandler;

 public class MorpionActivity extends AppCompatActivity {
    private final static int NUMBER_CELL = 3;

    private HandlerMorpion handlerMorpion;
    private SocketChannel sc;
    private Button[][] cases = new Button[NUMBER_CELL][NUMBER_CELL];

    private TextView playerLeft;
    private TextView playerRight;
    private TextView messageBottom;

    private Players currentPlayer;

    private boolean isTurn = false; //It's the turn of current player

     private boolean isBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion);
        //getInformation();

        Intent i = getIntent();

        int begin = i.getIntExtra("begin", 0);

        if (begin == 1) {
            isTurn = true;
            isBegin = true;
            currentPlayer = Players.PLAYER1;
        } else {
            isBegin = false;
            currentPlayer = Players.PLAYER2;
        }
        sc = SocketHandler.getSocket();


        initButton();

        if (begin == 1) {
            handlerMorpion = new HandlerMorpion (sc, this, new Morpion(Players.PLAYER1, currentPlayer));
        } else {
            handlerMorpion = new HandlerMorpion (sc, this, new Morpion(Players.PLAYER2, currentPlayer));
        }


        Log.d("Test ", begin + " " + SocketHandler.getSocket());
    }

     @Override
     protected void onResume() {
         super.onResume();
         handlerMorpion.waitOther();
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

        isTurn = begin.equals(1);

        handlerMorpion = new HandlerMorpion (sc, this, new Morpion(begin, currentPlayer));
    }

    /**
     * Return the player who must play choose by the server
     * When the player click on a cell of gameboard
     */
    public void clickCell (View view) {
        Log.d("Click boutton", "ok");

        if (handlerMorpion.isFinish()) {
            Intent activity = new Intent(new Intent(this, SearchGameActivity.class));
            setResult(RESULT_OK, activity);
            finish();
        }

        if (isTurn) {
            Button b = (Button) view;

            Log.d("Click boutton ok", "ok");
            //Check the button click
            for (int i = 0; i < MorpionActivity.NUMBER_CELL; i++) {
                for (int j = 0; j < MorpionActivity.NUMBER_CELL; j++) {
                    Log.d("Click boutton ok", b + " " + cases[i][j]  + "");
                    if (b.equals(cases[i][j])) {

                        Log.d("Click boutton", "i + j" + " " + i + " " + j);

                        //check if the button is empty
                        if (this.handlerMorpion.playAround(i, j)) {
                            try {
                                //send data to server
                                this.handlerMorpion.sendCell(i, j);
                                isTurn = false;
                                if (isBegin) {
                                    b.setText("X");
                                } else {
                                    b.setText("O");
                                }

                                //turn become true when receive data

                                //if the player win with this game
                                if (handlerMorpion.isFinish()) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Vous avez gagné";
                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                } else {
                                    this.handlerMorpion.waitOther();
                                }


                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("Echec d'écriture", "Retour à l'écran d'accueil");
                                Intent activity = new Intent(new Intent(this, SearchGameActivity.class));
                                setResult(RESULT_CANCELED, activity);
                                finish();
                            }

                        }

                    }
                }
            }
        }
    }

     public void isYourTurn () {
        isTurn = true;
    }

    public void putClickAdvsersary (int i, int j) {
        if (isBegin) {
            cases[i][j].setText("O");
        } else {
            cases[i][j].setText("X");
        }

    }


    /**
     * Init the gameBoard
     */
    public void initButton () {
        cases[0][0] = findViewById(R.id.button0);
        cases[0][1] = findViewById(R.id.button1);
        cases[0][2] = findViewById(R.id.button2);

        cases[1][0] = findViewById(R.id.button3);
        cases[1][1] = findViewById(R.id.button4);
        cases[1][2] = findViewById(R.id.button5);

        cases[2][0] = findViewById(R.id.button6);
        cases[2][1] = findViewById(R.id.button7);
        cases[2][2] = findViewById(R.id.button8);
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