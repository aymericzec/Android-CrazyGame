package fr.upem.crazygame.game.shakeGame;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.upem.crazygame.R;
import fr.upem.crazygame.provider.GameCrazyGameColumns;
import fr.upem.crazygame.provider.ProviderDataGame;
import fr.upem.crazygame.searchgameactivity.SocketHandler;


public class ShakeGameActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private TextView chrono;
    private TextView messageBottom;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private float speed = 0;
    private int val = 0;
    private AsyncTaskWaitShakeResult waitResult;

    private boolean volum;
    private boolean vibrate;
    private MediaPlayer applause;

    private CountDownTimer countDown = new CountDownTimer(10 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            chrono.setText("" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            SocketChannel socketChannel = SocketHandler.getSocket();
            ByteBuffer sendScore = ByteBuffer.allocate(4);
            int score = Math.round(speed/val);
            sendScore.putInt(score);
            sendScore.flip();

            try {
                socketChannel.write(sendScore);
                //chrono.setText(getString(R.string.yourScore) + "" +  score);
                Log.d("score", String.valueOf(score));
                //messageBottom.setText(getString(R.string.great));
                chrono.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ShakeGameActivity.this.finish();
                    }
                });
                waitResult = new AsyncTaskWaitShakeResult(socketChannel, ShakeGameActivity.this);

                waitResult.execute(score);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private CountDownTimer countDownBeforeStart = new CountDownTimer(5 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            chrono.setText(getString(R.string.timeBeforeStart) + "" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            messageBottom.setText(getString(R.string.shake));
            countDown.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initGraphic();

        this.volum = getIntent().getBooleanExtra("volum", true);
        this.vibrate = getIntent().getBooleanExtra("vibrate", true);
        this.applause = MediaPlayer.create(this, R.raw.applause);
        countDownBeforeStart.start();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        ProviderDataGame.addGame(GameCrazyGameColumns.NAME_SHAKEGAME, this);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initGraphic(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Typeface nightFont = Typeface.createFromAsset(getAssets(), "font/nightmachine.otf");
        Typeface comicFont = Typeface.createFromAsset(getAssets(), "font/comic_book.otf");

        TextView nameGame = (TextView) findViewById(R.id.nameGame);
        nameGame.setTypeface(nightFont);

        messageBottom = (TextView) findViewById(R.id.messageBottom);
        messageBottom.setText(R.string.waitBeforeStart);
        messageBottom.setTypeface(comicFont);

        chrono = (TextView) findViewById(R.id.chrono);
        chrono.setTypeface(comicFont);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                this.speed += speed;
                this.val++;

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void endGame(int result, int score, int scoreAdversary) {

        messageBottom.setText(result);

        String me = getResources().getString(R.string.yourScore);
        String adversary = getResources().getString(R.string.scoreAdversary);

        StringBuilder sb = new StringBuilder();
        sb.append(me + " " + score);
        sb.append("\n");
        sb.append(adversary + " " + scoreAdversary);

        chrono.setTextSize(25);
        chrono.setSingleLine(false);
        chrono.setText(sb.toString());

        if (score > scoreAdversary && this.volum){
            applause.start();
        }else if (score < scoreAdversary && this.vibrate){
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(1000);
        }
    }
}
