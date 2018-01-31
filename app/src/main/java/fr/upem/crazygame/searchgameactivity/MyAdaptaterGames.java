package fr.upem.crazygame.searchgameactivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import fr.upem.crazygame.R;

/**
 * Created by myfou on 31/01/2018.
 */

public class MyAdaptaterGames extends ArrayAdapter<String> {

    private List<String> nameGames;

    public MyAdaptaterGames(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.nameGames = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_search_games, null);

        return v;
    }


}
