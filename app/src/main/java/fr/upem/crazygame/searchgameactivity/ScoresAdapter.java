package fr.upem.crazygame.searchgameactivity;

import android.content.Context;
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

    public ScoresAdapter(@NonNull Context context, int resource, @NonNull List<Score> scores) {
        super(context, resource, scores);
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_score, parent, false);
        }
        // Lookup view for data population
        TextView txNameGame = (TextView) convertView.findViewById(R.id.nameGame);
        TextView txScore = (TextView) convertView.findViewById(R.id.gameTotal);
        TextView txScoreWin = (TextView) convertView.findViewById(R.id.gameWin);

        txNameGame.setText(score.getName());
        txScore.setText(score.getGame() + "");
        txScoreWin.setText(score.getGameWin() + "");

        // Return the completed view to render on screen
        return convertView;
    }


}
