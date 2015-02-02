package com.jonhassall.lab5_accel_vibr_writing_drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;

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
/*
        try {
            outputStream = openFileOutput(filename, Context.MODE_WORLD_READABLE);
            outputStream.write(string.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.toString());
            e.printStackTrace();
        }*/


        /*try {


            //String path = context.getFilesDir().getAbsolutePath(); //Internal storage
            //String path = context.getExternalFilesDir(null).getAbsolutePath(); //For SD card/external storage



            //FileOutputStream fos = new FileOutputStream("helloworld.txt");

            //File file = new File(path + "/helloworld.txt");
            //FileOutputStream stream = new FileOutputStream(file);

            String filename = "helloworld.txt";
            String path = context.getFilesDir().getAbsolutePath(); //Internal storage
            //String path = context.getExternalFilesDir(null).getAbsolutePath(); //For SD card/external storage

            Log.d(TAG, "Output filename: " + path + "/" + filename);

            Toast toast = Toast.makeText(context, "Output filename: " + path + "/" + filename, Toast.LENGTH_SHORT);
            toast.show();


            String string = "Hello world";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(path + "/" + filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Exception: " + e.toString());
            }


            //FileOutputStream fos = openFileOutput(path + "/helloworld.txt", Context.MODE_PRIVATE);
            //fos.write("Hello world".getBytes());
            //fos.flush();
            //fos.close();


            //stream.write("Hello world".getBytes());
            //stream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception: " + e.toString());
        }*/




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

    public void write_internal_btn(View view) {

        Context context = getApplicationContext();

        //Write to a file
        String filename = "myfile.txt";
        String string = "Hello internal world!";
        FileOutputStream outputStream;

        //Internal storage
        try {
            //String filepath = Environment.getExternalStorageDirectory().getPath();
            //File file = new File(filepath, "MyStorageDirectory");

            File file = new File(context.getFilesDir(), filename);

            //Recursively make any necessary directories
            if (!file.exists())
            {
                file.mkdirs();
            }
            //file = new File(filepath, "myfile.txt");
            outputStream = new FileOutputStream(file, false);
            outputStream.write(string.getBytes());
            outputStream.flush();
            outputStream.close();

            Toast toast = Toast.makeText(context, "Wrote to file", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception ex)
        {
            Log.d(TAG, "Exception: " + ex.toString());
        }

    }

    public void write_external_btn(View view) {

        //Write to a file
        String filename = "myfile.txt";
        String string = "Hello external world!";
        FileOutputStream outputStream;

        //External storage
        try {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filepath, filename);
            //Recursively make any necessary directories
            if (!file.exists())
            {
                file.mkdirs();
            }

            outputStream = new FileOutputStream(file, false);
            outputStream.write(string.getBytes());
            outputStream.flush();
            outputStream.close();

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Wrote to file", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception ex)
        {
            Log.d(TAG, "Exception: " + ex.toString());
        }

    }

    public void read_internal_btn(View view) {
        //Read from a file
        String filename = "myfile.txt";
        String string = "Hello world!";

        //Read from file
        try {
            FileInputStream fin = openFileInput(filename);
            int c;
            String temp = "";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //string temp contains all the data of the file.
            fin.close();
            Log.d(TAG, "File read result: " + temp);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Read contents: " + temp, Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void read_external_btn(View view) {
        //Read from a file
        String filename = "myfile.txt";

        //Read from file
        try {

            String filepath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filepath, filename);

            //FileInputStream fin = openFileInput(filepath + "/" + filename);
            FileInputStream fin = new FileInputStream(file);

            int c;
            String temp = "";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            //string temp contains all the data of the file.
            fin.close();
            Log.d(TAG, "File read result: " + temp);

            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Read contents: " + temp, Toast.LENGTH_SHORT);
            toast.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show_datetime(View view) {
        Log.d(TAG, "Show date/time");

        final Calendar d = Calendar.getInstance();
        String datetime = d.get(Calendar.YEAR) + " " + d.get(Calendar.MONTH) + " " + d.get(Calendar.DAY_OF_MONTH) + " " + d.get(Calendar.HOUR_OF_DAY) + " " + d.get(Calendar.MINUTE) + " " + d.get(Calendar.SECOND);

        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, datetime, Toast.LENGTH_SHORT);
        toast.show();
    }

}
