package fr.upem.crazygame.morpion;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class PlayerMorpionView extends View {

    private Players player;

    public PlayerMorpionView(Context context) {
        super(context);
    }

    public PlayerMorpionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerMorpionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlayer(Players player) {
        this.player = player;
    }
}
