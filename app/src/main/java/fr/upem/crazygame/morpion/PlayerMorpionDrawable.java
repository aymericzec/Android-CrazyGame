package fr.upem.crazygame.morpion;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;


/**
 * This class draws a cross if it's the player 1 who
 * has to play, else it's a circle
 */
public class PlayerMorpionDrawable extends Drawable {

    private final Paint paint;
    private final float widthBox;
    private final float heightBox;
    private Players player;

    public PlayerMorpionDrawable(float widthBox, float heightBox) {
        this.widthBox = widthBox;
        this.heightBox = heightBox;
        this.paint = new Paint();
    }

    public void setPlayer(Players player) {
        this.player = player;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {}

    @Override
    public void setColorFilter(ColorFilter cf) {}
}
