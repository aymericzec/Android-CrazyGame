package fr.upem.crazygame.game.mixwords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


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
    private MediaPlayer applause= new MediaPlayer();

    private Boolean volum;
    private Boolean vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_word);

        ProviderDataGame.addGame(GameCrazyGameColumns.NAME_MIXWORD, this);
        String word = getIntent().getStringExtra("wordSearch");

        this.volum = getIntent().getBooleanExtra("volum", true);
        this.vibrate = getIntent().getBooleanExtra("vibrate", true);

        applause.create(this,R.raw.applause);
        applause.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        Log.d("DURATION" , applause.getDuration()+"");

        initGraphic();
        initKeypadTop(word.length());
        initKeypadBottom(word);

        MixWords mixWords = new MixWords(word);
        this.handlerMixWords = new HandlerMixWords(SocketHandler.getSocket(), mixWords, this);
    }

    private void initGraphic (){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Typeface comic_book = Typeface.createFromAsset(getAssets(),"font/comic_book.otf");
        Typeface heros = Typeface.createFromAsset(getAssets(),"font/nightmachine.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(heros);

        TextView actionMixWord = (TextView) findViewById(R.id.actionMixWord);
        actionMixWord.setTypeface(comic_book);
    }

    /**
     * Initialize the top button
     * @param letters number caracter of word
     */
    public void initKeypadTop(int letters) {
        LinearLayout linearKeyPadTop = (LinearLayout) findViewById(R.id.wordOrder);
        keypadTop = new Button[letters];

        LinearLayout row = new LinearLayout(this);

        for (int i = 0; i < letters; i++) {
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            button.setBackgroundResource(R.drawable.my_buttons);
            button.setTextColor(Color.parseColor("#D8D8D8"));
            keypadTop[i] = button;
            keypadTop[i].setClickable(false);
            keypadTop[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    if (!b.getText().equals("")) {
                        int i = 0;
                        for (; i < keypadTop.length; i++) {
                            if (b.equals(keypadTop[i])) {
                                break;
                            }
                        }
                        int j = 0;
                        for (; j < keypadBottom.length; j++) {
                            if (keypadBottom[j].getText().toString().equals("")) {
                                break;
                            }
                        }
                        MixWordActivity.this.handlerMixWords.removeLetter(b, keypadBottom, i);
                    }
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
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            button.setBackgroundColor(Color.parseColor("#D8D8D8"));
            button.setBackgroundResource(R.drawable.my_buttons_2);

            button.setTextColor(Color.parseColor("#00AD97"));
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

    public void endGame(Boolean win, String word) {
        for (int i = 0; i < keypadTop.length; i++) {
            keypadTop[i].setClickable(false);
            keypadBottom[i].setClickable(false);
        }

        Button action = (Button) findViewById(R.id.actionMixWord);

        if (win) {
            action.setText(getText(R.string.winBack));
            applause.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("'''''''''''", "START");
                    mp.start();
                }
            });
        } else {
            action.setText(getText(R.string.loseBack));

            if (this.vibrate) {
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(1000);
            }
            for (int i = 0; i < word.length(); i++) {
                keypadTop[i].setText(String.valueOf(word.charAt(i)));
                keypadBottom[i].setText("");
            }
        }

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
