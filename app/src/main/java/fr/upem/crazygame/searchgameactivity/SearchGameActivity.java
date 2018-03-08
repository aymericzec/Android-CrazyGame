package fr.upem.crazygame.searchgameactivity;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import fr.upem.crazygame.R;
import fr.upem.crazygame.Score.ScoreActivity;


/**
 * Created by myfou on 15/01/2018.
 */

public class SearchGameActivity extends ListActivity {
    private SearchGameSocketManager searchGameSocketManager;
    private ListView listView;
    private final int nbGames = 2;
    private String[] games = new String[nbGames];
    private Integer[] img = new Integer[nbGames];
    private float initialX;


    IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            if(isAirplaneModeOn){
                Toast.makeText(SearchGameActivity.this,
                        getString(R.string.cantPlay), Toast.LENGTH_LONG).show();
            } else {
                // handle Airplane Mode off
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
       // mUsbReceiver.
        initGames();
        initImg();
        initGraphic();

        this.registerReceiver(receiver, intentFilter);


        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);

            listView = (ListView) findViewById(android.R.id.list);
            Log.d("test", listView + "");

            CustomListSearchGame adapter = new
                    CustomListSearchGame(this, games, img);
            listView = (ListView) findViewById(android.R.id.list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    clickSearchGame((String) adapterView.getItemAtPosition(i));
                }


            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGames() {
        games[0] = getResources().getString(R.string.morpion_name);
        games[1] = getResources().getString(R.string.mixWord_name);
    }

    private void initImg() {
        img[0] = R.drawable.sad1;
        img[1] = R.drawable.sad1;
    }

    private void initGraphic() {
        Typeface comic_book = Typeface.createFromAsset(getAssets(), "font/comic_book.otf");
        Typeface heros = Typeface.createFromAsset(getAssets(), "font/nightmachine.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(heros);

        TextView description = (TextView) findViewById(R.id.description);
        description.setTypeface(comic_book);

        TextView score = (TextView) findViewById(R.id.score);
        score.setTypeface(comic_book);

        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    /**
     * Init and send the bytebuffer for found a game
     *
     * @param nameGame
     */
    public void clickSearchGame(String nameGame) {
        SearchGameManager searchGameManager = searchGameSocketManager.isConnected();
        //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        if (null != searchGameManager) {
            try {
                searchGameManager.sendSearchGame(nameGame);
                searchGameManager.waitFoundGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Connecté", nameGame + " est lancé");
        }
    }

    public void launchGameActivity(Intent intent) {
        Log.d("Socket envoye ", SocketHandler.getSocket() + "");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();

                Intent intent = new Intent(SearchGameActivity.this, ScoreActivity.class);

                if (initialX > 400 + finalX) {
                    startActivity(intent);
                }
                break;
        }
        return SearchGameActivity.super.onTouchEvent(event);
    }
}

