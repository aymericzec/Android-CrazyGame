package fr.upem.crazygame.searchgameactivity;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import fr.upem.crazygame.R;


/**
 * Created by myfou on 15/01/2018.
 */

public class SearchGameActivity extends ListActivity {
    private SearchGameSocketManager searchGameSocketManager;
    private ListView listView;
    private String TAG = SearchGameActivity.class.getSimpleName();
    float initialX, initialY;

    String[] games ={
            //getResources().getString(R.string.morpion_name),
            //getResources().getString(R.string.mixWord_name)
            "Morpion",
            "MixWord"
    };

    Integer[] img = {
            R.drawable.sad1,
            R.drawable.sad2,
            R.drawable.sad3
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);

        initGraphic();

        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);

            listView = (ListView)findViewById(android.R.id.list);
            Log.d("test", listView + "");

            CustomListSearchGame adapter = new
                    CustomListSearchGame(this, games, img);
            listView=(ListView)findViewById(android.R.id.list);
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

    public void initGraphic (){
        Typeface comic_book = Typeface.createFromAsset(getAssets(),"font/comic_book.otf");
        Typeface adventure = Typeface.createFromAsset(getAssets(),"font/adventure.otf");
        Typeface heros = Typeface.createFromAsset(getAssets(),"font/nightmachine.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(heros);

        TextView description = (TextView) findViewById(R.id.description);
        description.setTypeface(comic_book);
    }

    /**
     * Init and send the bytebuffer for found a game
     * @param nameGame
     */
    public void clickSearchGame (String nameGame) {
        SearchGameManager searchGameManager = searchGameSocketManager.isConnected();

        if (null != searchGameManager) {
            try {
                searchGameManager.sendSearchGame(nameGame);
                //ecran de chargement
                searchGameManager.waitFoundGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Connecté", nameGame + " est lancé");
        }
    }

    public void launchGameActivity (Intent intent) {
        Log.d("Socket envoye ", SocketHandler.getSocket() + "");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mGestureDetector.onTouchEvent(event);

        int action = event.getActionMasked();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();

                Log.d(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();

                Intent intent = new Intent(SearchGameActivity.this, ScoreActivity.class);
                Log.d(TAG, "Action was UP");

                if (initialX < finalX) {
                    Log.d(TAG, "Left to Right swipe performed");
                    startActivity(intent);
                }

                if (initialX > finalX) {
                    Log.d(TAG, "Right to Left swipe performed");
                    startActivity(intent);
                }

                if (initialY < finalY) {
                    Log.d(TAG, "Up to Down swipe performed");
                }

                if (initialY > finalY) {
                    Log.d(TAG, "Down to Up swipe performed");
                }

                break;

            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                Log.d(TAG, "Movement occurred outside bounds of current screen element");
                break;
        }
        return SearchGameActivity.super.onTouchEvent(event);
    }
}

