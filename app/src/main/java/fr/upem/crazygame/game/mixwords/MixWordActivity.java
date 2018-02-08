package fr.upem.crazygame.game.mixwords;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import fr.upem.crazygame.R;
import fr.upem.crazygame.searchgameactivity.SocketHandler;

public class MixWordActivity extends Activity {
    private Button[] keypad;
    private Button[] keypadTop;
    private HandlerMixWords handlerMixWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_word);

        String word = "abcdef";

        initKeypadTop(word.length());
        initKeypadBottom(word);

        MixWords mixWords = new MixWords(word);
        this.handlerMixWords = new HandlerMixWords(SocketHandler.getSocket(), mixWords, this);
    }

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

                    MixWordActivity.this.handlerMixWords.removeLetter(keypad, b, i);
                }
            });
            linearKeyPadTop.addView(button);
        }
    }

    public void initKeypadBottom (String word) {
        LinearLayout linearKeyPad = (LinearLayout) findViewById(R.id.lettersAvailaible);
        keypad = new Button[word.length()];
        for (int i = 0; i < word.length(); i++) {
            Button button = new Button(this);
            button.setText(word.charAt(i) + "");
            keypad[i] = button;
            linearKeyPad.addView(button);
        }
    }
}
