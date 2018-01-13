package fr.upem.crazygame.morpion;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;


/**
 * This class draws the board of the game
 */
public class GameMorpionDrawable extends Drawable {

    private final Paint paint;
    private final float widthBox;
    private final float heightBox;

    /**
     * Constructor of this class
     *
     * @param widthBox Width of a box in the board
     * @param heightBox Height of a box in the board
     */
    public GameMorpionDrawable(float widthBox, float heightBox) {
        this.widthBox = widthBox;
        this.heightBox = heightBox;
        this.paint = new Paint();
    }

    /**
     * Getter of widthBox
     * @return Width of a box in the board
     */
    public float getWidthBox() {
        return widthBox;
    }

    /**
     * Getter of heightBox
     * @return Height of a box in the board
     */
    public float getHeightBox() {
        return heightBox;
    }

    /**
     * Draw the board of the game
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {

        float startX = 0;
        float startY = 0;
        float stopX = 0;
        float stopY = 0;

        stopX = 3 * widthBox;

        for(int i = 0; i <= 3; i++) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
            startY += heightBox;
            stopY += heightBox;
        }

        startX = 0;
        startY = 0;
        stopX = 0;
        stopY = 3 * heightBox;

        for(int j = 0; j <= 3; j++) {
            canvas.drawLine(startX, startY, stopX, stopY, paint);
            startX += widthBox;
            stopX += widthBox;
        }
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
