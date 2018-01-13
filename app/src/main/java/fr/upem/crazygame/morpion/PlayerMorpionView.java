package fr.upem.crazygame.morpion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class PlayerMorpionView extends View {

    private Players player;
    private float startXBoard; // x coordinate of the start board
    private float startYBoard; // y coordinate of the start board
    private float stopXBoard; // x coordinate of the stop board
    private float stopYBoard; // y coordinate of the stop board

    private GameMorpionView morpionView;
    private PlayerMorpionDrawable playerDrawable;

    public PlayerMorpionView(Context context) {
        super(context);
    }

    public PlayerMorpionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerMorpionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGameMorpionView(GameMorpionView morpionView) {
        this.morpionView = morpionView;
    }

    public void setPlayer(Players player) {
        this.player = player;
    }

    public void setPositionBoard(float startXBoard, float startYBoard, float widthBoard, float heightBoard) {
        this.startXBoard = startXBoard;
        this.startYBoard = startYBoard;
        this.stopXBoard = startXBoard + widthBoard;
        this.stopYBoard = startYBoard + heightBoard;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        playerDrawable.draw(canvas);
    }

    /**
     * User clic
     */
    private final OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN :
                    float x = motionEvent.getX(motionEvent.getActionIndex());
                    float y = motionEvent.getY(motionEvent.getActionIndex());

                    if((x < startXBoard || x > stopXBoard) && (y < startXBoard || y > startYBoard)) {
                        return false;
                    }

                    playerDrawable = new PlayerMorpionDrawable(morpionView.getMorpionBoard().getWidthBox(), morpionView.getMorpionBoard().getHeightBox(), startXBoard, startYBoard, stopXBoard, stopYBoard, x, y, player);


                case MotionEvent.ACTION_MOVE:
                    return false;


            }
            return false;
        }
    };
}
