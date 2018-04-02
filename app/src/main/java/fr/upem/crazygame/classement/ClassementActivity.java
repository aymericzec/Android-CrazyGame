package fr.upem.crazygame.classement;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import fr.upem.crazygame.R;


/**
 * Représente la vue des classements Mondiaux
 */

public class ClassementActivity extends Activity {
    private float initialX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classement);
        initGraphic();
        new AsyncTaskWaitScoreWorld(this).execute();
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

    public void initList (List<Classement> classements) {
        Collections.sort(classements);
        ClassementAdapter adapter = new ClassementAdapter(ClassementActivity.this, R.layout.row_layout_classement, classements);
        ListView listView=(ListView)findViewById(R.id.listClassement);
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

                if (initialX > finalX + 400) {
                    this.finish();
                    overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                }
                break;
        }
        return ClassementActivity.super.onTouchEvent(event);
    }
}
