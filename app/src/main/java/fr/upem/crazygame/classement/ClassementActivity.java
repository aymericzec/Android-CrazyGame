package fr.upem.crazygame.classement;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import fr.upem.crazygame.R;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;

/**
 * Created by myfou on 04/03/2018.
 */

public class ClassementActivity extends Activity {
    private float initialX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classement);

        Log.d("----------", "Creation Classement");

        initGraphic();

        ArrayList<Classement> classements = new ArrayList<>();

        String [] columns = {GameCrazyGameColumns.NAME_GAME, GameCrazyGameColumns.GAME_WIN, GameCrazyGameColumns.GAME};
        Cursor cursor = getContentResolver().query(ProviderDataGame.CONTENT_URI, columns, null, null, null);

        /*
        if (cursor.moveToFirst()) {
            do {
                String nameGame = cursor.getString(cursor.getColumnIndex(GameCrazyGameColumns.NAME_GAME));
                int nbGameTotal = 3; //= cursor.getInt(cursor.getColumnIndex(GameCrazyGameColumns.TOTAL_GAME));

                classements.add(new Classement(nameGame, nbGameTotal));
            } while (cursor.moveToNext());
        }
        */
        Classement c1 = new Classement("CrazyGame", 12);
        Classement c2 = new Classement("Morpion", 56);
        Classement c3 = new Classement("MixWord", 2);

        classements.add(c1);
        classements.add(c2);
        classements.add(c3);

        Collections.sort(classements);

        ClassementAdapter adapter = new ClassementAdapter(this, R.layout.row_layout_classement, classements);
        ListView listView=(ListView)findViewById(R.id.listClassement);
        listView.setAdapter(adapter);
    }

    private void initGraphic(){
        Typeface comic_book = Typeface.createFromAsset(getAssets(),"font/comic_book.otf");
        Typeface heros = Typeface.createFromAsset(getAssets(),"font/nightmachine.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(heros);

        TextView descriptionClassement = (TextView) findViewById(R.id.descriptionClassement);
        descriptionClassement.setTypeface(comic_book);

        TextView back = (TextView) findViewById(R.id.back);
        back.setTypeface(comic_book);
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

                if (initialX > finalX + 400) {
                    this.finish();
                    overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                }
                break;
        }
        return ClassementActivity.super.onTouchEvent(event);
    }
}
