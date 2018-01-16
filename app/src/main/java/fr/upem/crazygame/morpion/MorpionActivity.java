package fr.upem.crazygame.morpion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fr.upem.crazygame.R;

public class MorpionActivity extends Activity {
    private Players player;
    private Map<Button, Integer> cases = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpion_p);
        //showpopupwin();
        //launchMorpion();
    }

    public boolean winGame() {
        return false;
    }

    /**
     * Launch the game
     */
    public void launchMorpion() {

        cases.put((Button)findViewById(R.id.button0), 0);
        cases.put((Button)findViewById(R.id.button1), 0);
        cases.put((Button)findViewById(R.id.button2), 0);
        cases.put((Button)findViewById(R.id.button3), 0);
        cases.put((Button)findViewById(R.id.button4), 0);
        cases.put((Button)findViewById(R.id.button5), 0);
        cases.put((Button)findViewById(R.id.button6), 0);
        cases.put((Button)findViewById(R.id.button7), 0);
        cases.put((Button)findViewById(R.id.button8), 0);

        player = Players.PLAYER1;

        /*
        while(!winGame()) {

        }
        */
    }

    // Appeler quand on clique sur la page
    // Pas encore terminer
    public void showpopupwin(View view){
        LayoutInflater inflater = (LayoutInflater) MorpionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popupwin,
                (ViewGroup) findViewById(R.id.popup_1));

        Log.d("DEBUG", layout.toString());
        PopupWindow pw = new PopupWindow( layout, 3*(view.getWidth()/4), view.getHeight()/2, true);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }
}
