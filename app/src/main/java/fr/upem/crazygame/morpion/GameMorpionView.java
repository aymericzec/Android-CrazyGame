package fr.upem.crazygame.morpion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public class GameMorpionView extends View {

    /**
     * Contains the board drawing of the game
     */
    private final GameMorpionDrawable morpionBoard = new GameMorpionDrawable(200, 200);

    public GameMorpionView(Context context) {
        super(context);
    }

    public GameMorpionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameMorpionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        morpionBoard.draw(canvas);
    }

    /**
     * Getter of morpionBoard
     * @return The instance of GameMorpionDrawable
     */
    public GameMorpionDrawable getMorpionBoard() {
        return morpionBoard;
    }


}
