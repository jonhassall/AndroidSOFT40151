package com.jonhassall.lab5_accel_vibr_writing_drawing;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    public Vibrator v;

    private static final String TAG = "lab5_accel_vibr_writing_drawing";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        Context context = getApplicationContext();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
            // success! we have an accelerometer

            Toast toast = Toast.makeText(context, "Success! We have an accelerometer", Toast.LENGTH_SHORT);
            toast.show();

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fail! we dont have an accelerometer!
            Toast toast = Toast.makeText(context, "Fail. We don't have an accelerometer", Toast.LENGTH_SHORT);
            toast.show();
        }

        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        /*long[] vibratePattern;
        vibratePattern = new long[5];
        vibratePattern[0] = 0;
        vibratePattern[1] = 100;
        vibratePattern[2] = 50;
        vibratePattern[3] = 50;
        vibratePattern[4] = 50;*/
        v.vibrate(1000);
        //v.vibrate(vibratepattern, 5);

        try {
            Toast toast = Toast.makeText(context, "Writing to file", Toast.LENGTH_SHORT);
            toast.show();
            //FileOutputStream fos = openFileOutput("helloworld.txt", Context.MODE_PRIVATE);
            //FileOutputStream fos = new FileOutputStream("helloworld.txt");

            FileOutputStream fOut = openFileOutput("helloworld.txt",MODE_WORLD_READABLE);
            String str = "data";
            fOut.write(str.getBytes());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "Error writing to file", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values
        displayMaxValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below, it is just plain noise
        if (deltaX < 1)
            deltaX = 0;
        if (deltaY < 1)
            deltaY = 0;
        if (deltaZ < 1)
            deltaZ = 0;

        //Log.d(TAG, "deltaX: " + deltaX);
        //Log.d(TAG, "deltaX: " + deltaY);

        if (deltaX > vibrateThreshold || deltaY > vibrateThreshold || deltaZ > vibrateThreshold) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Vibrate", Toast.LENGTH_SHORT);
            toast.show();
            v.vibrate(150);
        }
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
}
