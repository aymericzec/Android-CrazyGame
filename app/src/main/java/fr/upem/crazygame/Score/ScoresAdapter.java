package fr.upem.crazygame.Score;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.upem.crazygame.R;

/**
 * Created by myfou on 31/01/2018.
 */

public class ScoresAdapter extends ArrayAdapter<Score> {
    private List<Score> scores;
    private final Activity context;

    public ScoresAdapter(@NonNull Activity context, @NonNull List<Score> scores) {
        super(context, R.layout.row_layout_score, scores);
        this.scores = scores;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = getItem(position);

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_layout_score, null,true);

        // Lookup view for data population
        TextView txNameGame = (TextView) rowView.findViewById(R.id.nameGame);
        TextView txScore = (TextView) rowView.findViewById(R.id.nbGamePlay);
        TextView txScoreWin = (TextView) rowView.findViewById(R.id.nbGameWin);

        txNameGame.setText(score.getName());
        txScore.setText(score.getGame() + "");
        txScoreWin.setText(score.getGameWin() + "");

        // Return the completed view to render on screen
        return rowView;
    }
}
