package com.example.jsonsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SensorManager mSensorManager;
    TextView valorSensor0;
    TextView valorSensor1;
    TextView valorSensor2;
    TextView tres;
    Sensor sensor0;
    Sensor sensor1;
    Sensor sensor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valorSensor0 = findViewById(R.id.sensor0);
        valorSensor1 = findViewById(R.id.sensor1);
        valorSensor2 = findViewById(R.id.sensor2);
        tres = findViewById(R.id.tres);
    }

    public void listarSensores(View view) {

        proximidade();
        acelerometro();
        light();
    }

    public class proxSensor implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float valor0 = event.values[0];

            valorSensor0.setText("A " + valor0);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public void proximidade(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor0 = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mSensorManager.registerListener(new proxSensor(), sensor0, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public class acelSensor implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float valor1 = event.values[0];

            valorSensor1.setText("B " + valor1);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public void acelerometro(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(new acelSensor(), sensor1, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public class lightSensor implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float valor2 = event.values[0];

            valorSensor2.setText("C " + valor2);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public void light(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor2 = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(new lightSensor(), sensor2, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void enviaWebservice(View view) {
        CharSequence valorA;
        CharSequence valorB;
        CharSequence valorC;
        CharSequence resultado;

        valorA = valorSensor0.getText();
        valorB = valorSensor1.getText();
        valorC = valorSensor2.getText();

        resultado = "";

        tres.setText(resultado);
    }


}
