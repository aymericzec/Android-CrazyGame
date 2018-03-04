package fr.upem.crazygame.game.mixwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import java.io.IOException;

import fr.upem.crazygame.R;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;
import fr.upem.crazygame.searchgameactivity.SearchGameActivity;
import fr.upem.crazygame.searchgameactivity.SocketHandler;

public class MixWordActivity extends Activity {
    private Button[] keypadBottom;
    private Button[] keypadTop;
    private HandlerMixWords handlerMixWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_word);

        ProviderDataGame.addGame(GameCrazyGameColumns.NAME_MIXWORD, this);
        String word = getIntent().getStringExtra("wordSearch");

        initKeypadTop(word.length());
        initKeypadBottom(word);

        MixWords mixWords = new MixWords(word);
        this.handlerMixWords = new HandlerMixWords(SocketHandler.getSocket(), mixWords, this);
    }

    /**
     * Initialize the top button
     * @param letters number caracter of word
     */
    public void initKeypadTop(int letters) {
        LinearLayout linearKeyPadTop = (LinearLayout) findViewById(R.id.wordOrder);
        keypadTop = new Button[letters];
        for (int i = 0; i < letters; i++) {
            Button button = new Button(this);
            keypadTop[i] = button;
            keypadTop[i].setClickable(false);
            keypadTop[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    int i = 0;
                    for (;i < keypadTop.length; i++) {
                        if (b.equals(keypadTop[i])) {
                            break;
                        }
                    }
                    int j = 0;
                    for (;j < keypadBottom.length; j++) {
                        if (keypadBottom[j].getText().toString().equals("")) {
                            break;
                        }
                    }

                    MixWordActivity.this.handlerMixWords.removeLetter(b, keypadBottom, i);
                }
            });
            linearKeyPadTop.addView(button);
        }
    }

    /**
     * Initialize the bottom button with letters of words
     * @param word
     */
    public void initKeypadBottom (String word) {
        LinearLayout linearKeyPad = (LinearLayout) findViewById(R.id.lettersAvailaible);
        keypadBottom = new Button[word.length()];
        for (int i = 0; i < word.length(); i++) {
            Button button = new Button(this);
            button.setText(word.charAt(i) + "");
            Log.d("lettre", word.charAt(i) + "");
            keypadBottom[i] = button;

            keypadBottom[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    int i = 0;
                    for (;i < keypadBottom.length; i++) {
                        if (b.equals(keypadBottom[i])) {
                            break;
                        }
                    }

                    MixWordActivity.this.handlerMixWords.addLetter(b, keypadTop);
                }
            });

            linearKeyPad.addView(button);
        }
    }

    public void sendWord(View v) {

        for (int i = 0; i < keypadTop.length; i++) {
            if (keypadTop[i].getText().equals("")) {
                return;
            }
        }

        try {
            this.handlerMixWords.sendWord();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endGame() {
        for (int i = 0; i < keypadTop.length; i++) {
            keypadTop[i].setClickable(false);
            keypadBottom[i].setClickable(false);
        }

        Button action = (Button) findViewById(R.id.actionMixWord);
        action.setText("Retourner au Menu");
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity = new Intent(new Intent(MixWordActivity.this, SearchGameActivity.class));
                setResult(RESULT_OK, activity);
                finish();
            }
        });
    }
}