package fr.upem.crazygame.searchgameactivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.upem.crazygame.R;

/**
 * Created by myfou on 15/01/2018.
 */

public class SearchGameActivity extends ListActivity {
    private SearchGameSocketManager searchGameSocketManager;
    private ListView listView;

    String[] games ={
            "Morpion",
            "LuckyEgg"
    };

    Integer[] img = {
            R.drawable.sad1,
            R.drawable.sad2
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
        initGraphic();

        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            searchGameSocketManager.connectSocket("192.168.1.13", 8086);

            listView = (ListView)findViewById(android.R.id.list);
            Log.d("test", listView + "");

            CustomListAdapter adapter = new
                    CustomListAdapter(this, games, img);
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


}

