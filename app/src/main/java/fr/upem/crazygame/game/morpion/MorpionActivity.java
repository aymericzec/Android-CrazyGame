package fr.upem.crazygame.game.morpion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import fr.upem.crazygame.R;
import fr.upem.crazygame.game.Players;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;
import fr.upem.crazygame.searchgameactivity.SearchGameActivity;
import fr.upem.crazygame.searchgameactivity.SocketHandler;

public class MorpionActivity extends Activity {

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

        ProviderDataGame.addGame(GameCrazyGameColumns.NAME_MORPION, this);
        initGraphic();
        initButton();
        getInformation();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initGraphic() {
        Typeface nightFont = Typeface.createFromAsset(getAssets(), "font/nightmachine.otf");
        Typeface comicFont = Typeface.createFromAsset(getAssets(), "font/comic_book.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(nightFont);

        playerLeft = (TextView) findViewById(R.id.playerLeft);
        playerLeft.setTypeface(comicFont);

        playerRight = (TextView) findViewById(R.id.playerRight);
        playerRight.setTypeface(comicFont);

        messageBottom = (TextView) findViewById(R.id.messageBottom);
        messageBottom.setTypeface(comicFont);

        if (currentPlayer == Players.PLAYER1) {
            playerLeft.setText(R.string.player1);
            playerRight.setText(R.string.player2);
        } else {
            playerRight.setText(R.string.player1);
            playerLeft.setText(R.string.player2);
        }
    }

    /**
     * Init and launch the game
     */
    public void getInformation() {
        Intent i = getIntent();

        sc = SocketHandler.getSocket();
        int begin = i.getIntExtra("begin", 0);
        if (begin == 1) {
            isTurn = true;
            isBegin = true;
            myTurnGraphic();
            currentPlayer = Players.PLAYER1;
            handlerMorpion = new HandlerMorpion(sc, this, new Morpion(Players.PLAYER1, currentPlayer));
        } else {
            isBegin = false;
            currentPlayer = Players.PLAYER2;
            notMyTurnGraphic();
            handlerMorpion = new HandlerMorpion(sc, this, new Morpion(Players.PLAYER2, currentPlayer));
            handlerMorpion.waitOther();
        }
    }


    /**
     * Return the player who must play choose by the server
     * When the player click on a cell of gameboard
     */
    public void clickCell(View view) {
        Log.d("Click boutton", "ok");

        if (handlerMorpion.isWinner() || handlerMorpion.isEgality()) {
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
                    Log.d("Click boutton ok", b + " " + cases[i][j] + "");
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
                                if (handlerMorpion.isWinner()) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Vous avez gagné";
                                    ProviderDataGame.addWinGame(GameCrazyGameColumns.NAME_MORPION, this);
                                    int duration = Toast.LENGTH_LONG;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                } else if (handlerMorpion.isEgality()) {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Egalité";
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
            notMyTurnGraphic();
        }
    }

    public void setYourTurn() {
        isTurn = true;
    }

    public void putClickAdvsersary(int i, int j) {
        if (isBegin) {
            cases[i][j].setText("O");
        } else {
            cases[i][j].setText("X");
        }
    }

    public void myTurnGraphic() {
        playerLeft.setTextColor(Color.parseColor("#0489B1"));
        playerRight.setTextColor(getResources().getColor(R.color.material_blue_grey_900));
        messageBottom.setText(R.string.messageBottomPlay);
    }

    public void notMyTurnGraphic() {
        playerRight.setTextColor(Color.parseColor("#0489B1"));
        playerLeft.setTextColor(getResources().getColor(R.color.material_blue_grey_900));
        messageBottom.setText(R.string.messageBottomWait);
    }

    /**
     * Init the gameBoard
     */
    public void initButton() {
        cases[0][0] = (Button) findViewById(R.id.button0);
        cases[0][1] = (Button) findViewById(R.id.button1);
        cases[0][2] = (Button) findViewById(R.id.button2);

        cases[1][0] = (Button) findViewById(R.id.button3);
        cases[1][1] = (Button) findViewById(R.id.button4);
        cases[1][2] = (Button) findViewById(R.id.button5);

        cases[2][0] = (Button) findViewById(R.id.button6);
        cases[2][1] = (Button) findViewById(R.id.button7);
        cases[2][2] = (Button) findViewById(R.id.button8);
    }

    public Players getCurrentPlayer() {
        return currentPlayer;
    }
}
