package fr.upem.crazygame.searchgameactivity;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;

import fr.upem.crazygame.classement.ClassementActivity;
import fr.upem.crazygame.config.Config;
import fr.upem.crazygame.connectivityReceiver.ConnectivityReceiver;
import fr.upem.crazygame.R;
import fr.upem.crazygame.score.ScoreActivity;
import fr.upem.crazygame.service.statistical.ServiceStatistical;


public class SearchGameActivity extends ListActivity {
    private SearchGameSocketManager searchGameSocketManager;
    private ListView listView;
    private final int nbGames = 3;
    private String[] games = new String[nbGames];
    private Integer[] img = new Integer[nbGames];
    private float initialX;
    private ConnectivityReceiver connectivityReceiver;
    private static View lastClickSearch = null;
    private static final int DIALOG_INTERNET_CONNECTION_FAIL = 10;
    private Config config;
    private Button vibrate;
    private Button volum;
    private MediaPlayer applause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);

        config = Config.createConfig(this);
        startService();
        initGraphic();
        initListView();

        applause = MediaPlayer.create(this, R.raw.applause);
        Log.d("test TEST", R.raw.applause + "");

        applause.start();

        Log.d("DURATION" , applause.getDuration()+"");

        connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, connectivityReceiver.getIntentFilter());

        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //applause.start();
    }

    private void startService() {
        Intent i = new Intent(this, ServiceStatistical.class);
        this.startService(i);
    }

    private void initListView(){
        // init Game List
        games[0] = getResources().getString(R.string.morpion_name);
        games[1] = getResources().getString(R.string.mixWord_name);
        games[2] = getResources().getString(R.string.shake_name);

        // init Image List
        img[0] = R.drawable.morpion;
        img[1] = R.drawable.word;
        img[2] = R.drawable.shake;

        listView = (ListView) findViewById(android.R.id.list);

        CustomListSearchGame adapter = new
                CustomListSearchGame(this, games, img);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                SearchGameActivity.this.lastClickSearch = view;
                clickSearchGame((String) adapterView.getItemAtPosition(i), view);
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

        this.volum = (Button) findViewById(R.id.volum);
        this.vibrate = (Button) findViewById(R.id.vibrate);



        setConfiGraphic();
    }


    public void setConfiGraphic(){
        if (this.config.getVibrate()){
            this.vibrate.setBackground(getDrawable(R.drawable.smartphone_1));

        }else{
            this.vibrate.setBackground(getDrawable(R.drawable.smartphone));
        }

        if (this.config.getVolum()){
            this.volum.setBackground(getDrawable(R.drawable.speaker));
        }else {
            this.volum.setBackground(getDrawable(R.drawable.speaker_1));
        }
    }

    public void clickConfigVolum(View view){
        Log.d("TOTO", !this.config.getVolum() + "");
        this.config.setVolum(!this.config.getVolum());
        setConfiGraphic();
    }

    public void clickConfigVibrate(View view){
        this.config.setVibrate(!this.config.getVibrate());
        setConfiGraphic();
    }

    public Config getConfig(){
        return this.config;
    }


    /**
     * Init and send the bytebuffer for found a game
     *
     * @param nameGame
     */
    public void clickSearchGame(String nameGame, View view) {
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

        if (lastClickSearch != null) {
            lastClickSearch.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
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

    private boolean isServiceStart (Class<?> service) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceInfo.service.getClassName().equals(service.getName())) {
                return true;
            }
        }
        return false;
    }
}