package fr.upem.crazygame.searchgameactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        try {
            searchGameSocketManager = SearchGameSocketManager.createSearchGameSocketManager(this);
            //searchGameSocketManager.connectSocket("192.168.1.13", 8086);
            searchGameSocketManager.connectSocket("90.3.251.211", 1002);
            searchGameSocketManager.connectSocket("192.168.1.13", 8086);

            listView = (ListView) findViewById(R.id.list_games);
            listView = findViewById(android.R.id.list);
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

