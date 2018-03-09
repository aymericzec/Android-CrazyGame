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

import fr.upem.crazygame.classement.ClassementActivity;
import fr.upem.crazygame.connectivityReceiver.ConnectivityReceiver;
import fr.upem.crazygame.R;
import fr.upem.crazygame.score.ScoreActivity;
import fr.upem.crazygame.service.statistical.ServiceStatistical;


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
    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
        startService();
        initGraphic();
        initListView();

        registerReceiver(connectivityReceiver, connectivityReceiver.getIntentFilter());

        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startService() {
        Intent i = new Intent(this, ServiceStatistical.class);
        this.startService(i);
    }

    private void initListView(){
        // init Game List
        games[0] = getResources().getString(R.string.morpion_name);
        games[1] = getResources().getString(R.string.mixWord_name);

        // init Image List
        img[0] = R.drawable.sad1;
        img[1] = R.drawable.sad1;

        listView = (ListView) findViewById(android.R.id.list);

        CustomListSearchGame adapter = new
                CustomListSearchGame(this, games, img);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                clickSearchGame((String) adapterView.getItemAtPosition(i));
            }
        });
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

        TextView classement = (TextView) findViewById(R.id.classement);
        classement.setTypeface(comic_book);
    }

    /**
     * Init and send the bytebuffer for found a game
     *
     * @param nameGame
     */
    public void clickSearchGame(String nameGame) {
        SearchGameManager searchGameManager = searchGameSocketManager.isConnected();

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
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                Intent intent;

                if (initialX > 400 + finalX) {
                    intent = new Intent(SearchGameActivity.this, ScoreActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                }else if (initialX < finalX - 400) {
                    intent = new Intent(SearchGameActivity.this, ClassementActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.transition.slide_from_left, R.transition.slide_to_right);
                    break;
                }
        }
        return SearchGameActivity.super.onTouchEvent(event);
    }
}

