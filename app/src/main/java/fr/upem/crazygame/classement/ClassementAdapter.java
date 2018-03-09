package fr.upem.crazygame.classement;

import android.content.Context;
import android.graphics.Typeface;
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

public class ClassementAdapter extends ArrayAdapter<Classement> {

    private List<Classement> classements;

    public ClassementAdapter(@NonNull Context context, int resource, @NonNull List<Classement> classements) {
        super(context, resource, classements);
        this.classements = classements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Classement classement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_classement, parent, false);
        }

        TextView txNameGame = (TextView) convertView.findViewById(R.id.nameGame);
        TextView txTotal = (TextView) convertView.findViewById(R.id.nbPartyTotal);

        txNameGame.setText(classement.getName());
        txTotal.setText(classement.getNbGameTotal() + "");

        Typeface comic_book = Typeface.createFromAsset(this.getContext().getAssets(),"font/comic_book.otf");
        txNameGame.setTypeface(comic_book);
        txTotal.setTypeface(comic_book);

        return convertView;
    }
}
