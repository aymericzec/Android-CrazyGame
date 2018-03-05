package fr.upem.crazygame.searchgameactivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.upem.crazygame.R;

public class CustomListSearchGame extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListSearchGame(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.row_layout_searchgame, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.row_layout_searchgame, null,true);
        Typeface comic_book = Typeface.createFromAsset(this.getContext().getAssets(),"font/comic_book.otf");

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.label);
        extratxt.setTypeface(comic_book);

        imageView.setImageResource(imgid[position]);
        extratxt.setText(itemname[position]);
        return rowView;
    };


}