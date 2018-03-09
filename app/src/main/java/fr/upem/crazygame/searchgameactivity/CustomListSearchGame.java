package fr.upem.crazygame.searchgameactivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.upem.crazygame.R;

public class CustomListSearchGame extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListSearchGame(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.row_layout_searchgame, itemname);
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

        rowView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        return rowView;
    };

    /*
    private void createLoadingPanel(int position){
        // Creating a new RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(this.getContext());

        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);


        ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);

        //params.addRule(RelativeLayout.CENTER_IN_PARENT);

        relativeLayout.addView(progressBar);
        setContentView(relativeLayout, rlp);
    }
    */
}