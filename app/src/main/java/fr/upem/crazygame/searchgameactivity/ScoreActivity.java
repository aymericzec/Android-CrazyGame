package fr.upem.crazygame.searchgameactivity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.widget.ListView;

import java.util.ArrayList;

import fr.upem.crazygame.R;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;

/**
 * Created by myfou on 04/03/2018.
 */

public class ScoreActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        ArrayList<Score> scores = new ArrayList<>();

        String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_WIN, GameCrazyGameColumns.GAME};
        Cursor cursor = getContentResolver().query(ProviderDataGame.CONTENT_URI, columns, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String nameGame = cursor.getString(cursor.getColumnIndex(GameCrazyGameColumns.NAME_GAME));
                int scoreWin = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME_WIN));
                int scoreTotal = cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.GAME));

                scores.add(new Score(nameGame, scoreTotal, scoreWin));
            } while (cursor.moveToNext());
        }

        ScoresAdaptater adapter = new ScoresAdaptater(this, R.layout.row_layout_chat, scores);
        ListView listView=(ListView)findViewById(R.id.listScores);
        listView.setAdapter(adapter);
    }
}
