package com.example.spreadsheettest;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.example.spreadsheettest.R.id.chronometer;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    Button btnView;
    EditText editTextItemName,editTextBrand;
    Button buttonAddItem;
    Button buttonEnd;
    SensorManager sensorManager;
    Sensor accelerometer;
    private Chronometer chronometer;
    private long pauseOffset;
    boolean running = false;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextItemName = findViewById(R.id.et_item_name);
        editTextBrand = findViewById(R.id.et_brand);

        buttonAddItem = findViewById(R.id.btn_start);
        buttonAddItem.setOnClickListener(this);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
      //  btnView = (Button) findViewById(R.id.viewentry);
        myDB = new DatabaseHelper(this);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ViewListContents.class);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startActivity(View v){
        if (!running) {
            running = true;
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
        }
    }

    public void pauseActivity(View v){
        if (running){
            running = false;
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        }
    }

    public void resetActivity(View v){
        Intent intent = new Intent(getApplicationContext(),AddItem.class);
        startActivity(intent);
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        myDB.deleteData();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            String newEntry_time = SystemClock.elapsedRealtime() + "";
            String newEntry_x = String.valueOf(sensorEvent.values[0]);
            String newEntry_y = String.valueOf(sensorEvent.values[1]);
            String newEntry_z = String.valueOf(sensorEvent.values[2]);

            AddData(newEntry_time, newEntry_x, newEntry_y, newEntry_z);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void AddData(String newEntry_time, String newEntry_x, String newEntry_y, String newEntry_z) {

        myDB.addData(newEntry_time, newEntry_x, newEntry_y, newEntry_z);
    }
    private void   addItemToSheet() {


        final String name = editTextItemName.getText().toString().trim();
        final String brand = editTextBrand.getText().toString().trim();
        buttonEnd = findViewById(R.id.btn_end);
        buttonEnd.setOnClickListener(this);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyNIrkysaHg99dADyPTWm26nU4EwbVrUfogIPSBZE9rMKhrV46Y/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   loading.dismiss();
                      /*  Toast.makeText(AddItem.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                      startActivity(intent);
*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("itemName",name);
                parmas.put("brand",brand);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// times out code

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }
    @Override
    public void onClick(View v) {
        while ((v==buttonAddItem) && (!(v==buttonEnd))){
            addItemToSheet();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}







/*public class MainActivity extends AppCompatActivity {








       Button buttonAddItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddItem = (Button)findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddItem.class);
                startActivity(intent);
            }
        });

    }

}*/
