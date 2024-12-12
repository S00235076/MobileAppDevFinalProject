package com.example.mobileappfinalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GamePlay extends AppCompatActivity implements SensorEventListener {
    int sequenceCount = 4, n = 0;

    private int BLUE = 1;
    public int RED = 2;
    private int YELLOW = 3;
    private int GREEN = 4;
    int score;

    boolean match = true;
    int[] gameSequence = new int[100];
    public int clickCount = 0, highScore = 0, k = 0;
    TextView tvDirection, tvScore;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    Button red, blue, green, yellow;
    private boolean isFlat = false;
    int arrayIndex = 0;
    Random r = new Random();
    View view;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tvScore = findViewById(R.id.tvScore);

        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);
        green = findViewById(R.id.green);
        yellow = findViewById(R.id.yellow);

        // Get data passed from previous activity
        Intent i = getIntent();
        score = getIntent().getIntExtra("score", -1);
        sequenceCount = getIntent().getIntExtra("sequenceCount", -1);
        gameSequence = getIntent().getIntArrayExtra("seqArray");

        // Setup accelerometer sensor
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        view = new View(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    // Accelerometer
    @SuppressLint("SetTextI18n")
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Check if the phone is flat
            if (Math.abs(x) < 1 && Math.abs(y) < 1) {
                if (!isFlat) {
                    isFlat = true;
                }
            }

            // Tilt detection to perform corresponding button clicks
            int maxTilt = 3;
            if (isFlat) {
                if (y < -maxTilt) {
                    handleButtonPress(blue, BLUE);  // Blue button for left tilt
                } else if (y > maxTilt) {
                    handleButtonPress(red, RED);  // Red button for right tilt
                } else if (x < -maxTilt) {
                    handleButtonPress(green, GREEN);  // Green button for up tilt
                } else if (x > maxTilt) {
                    handleButtonPress(yellow, YELLOW); // Yellow button for down tilt
                }
            }


            if (clickCount == gameSequence.length) {
                handleGameOver();
            }
        }
    }

    private void handleButtonPress(Button button, int color) {

        button.performClick();
        button.setPressed(true);
        button.invalidate();
        button.setPressed(false);
        button.invalidate();

        clickCount++;
        checkAnswer(color);
    }

    private void handleGameOver() {
        Intent intent = new Intent(view.getContext(), GameOver.class);
        intent.putExtra("score", score);
        startActivity(intent);
        Toast.makeText(this, "yay", Toast.LENGTH_SHORT).show();
    }


    public void checkAnswer(int colorIndex) {
        if (arrayIndex < sequenceCount) {
            if (gameSequence[arrayIndex] == colorIndex) {
                score++;
                tvScore.setText(String.valueOf(score));
                arrayIndex++;
            } else {
                handleGameOver();
            }
        }

        if (arrayIndex == sequenceCount) {

            Intent next = new Intent(GamePlay.this, MainActivity.class);
            next.putExtra("score", score);
            next.putExtra("sequenceCount", sequenceCount + 2);
            startActivity(next);
            finish();
        }
    }


    public void highscore(View view) {
        Intent i = new Intent(GamePlay.this, GameOver.class);
        i.putExtra("score", String.valueOf(clickCount));
        GamePlay.this.startActivity(i);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
