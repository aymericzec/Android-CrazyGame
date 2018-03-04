package fr.upem.crazygame.Score;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.ArrayList;
import fr.upem.crazygame.R;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;


/**
 * Created by myfou on 04/03/2018.
 */

public class ScoreActivity extends Activity {
    private float initialX;

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

        ScoresAdapter adapter = new ScoresAdapter(this, scores);
        ListView listView=(ListView) findViewById(R.id.listScores);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_MOVE:break;
            case MotionEvent.ACTION_CANCEL:break;
            case MotionEvent.ACTION_OUTSIDE:break;
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                if (initialX < 400 + finalX) {
                    this.finish();
                }
                break;
        }
        return ScoreActivity.super.onTouchEvent(event);
    }
}
