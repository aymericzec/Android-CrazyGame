package fr.upem.crazygame.game.checkGame;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import fr.upem.crazygame.R;

/**
 * Created by dagama on 08/03/18.
 */

public class ShakeGameActivity extends Activity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private TextView chrono;
    private TextView messageBottom;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private float maxSpeed = 0;

    private CountDownTimer countDown = new CountDownTimer(15 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            chrono.setText("" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            chrono.setText(getString(R.string.yourScore) + "" +  maxSpeed);
            messageBottom.setText(getString(R.string.great));
            chrono.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ShakeGameActivity.this.finish();
                }
            });
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

        countDownBeforeStart.start();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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

                if (speed > this.maxSpeed){
                    this.maxSpeed = speed;
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
