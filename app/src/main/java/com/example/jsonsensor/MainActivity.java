package com.example.jsonsensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static  final int REQUEST_LOCATION=1;
    SensorManager mSensorManager;
    TextView valorSensor0;
    TextView valorSensor1;
    TextView valorSensor2;
    TextView valorSensor3;
    Sensor sensor0;
    Sensor sensor1;

    Button getlocationBtn;
    TextView showLocationTxt;
    LocationManager locationManager;
    Double latitude,longitude;
    Integer umidade;
    Integer temperatura = -11;
    Float lightSensor;
    Float proxSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valorSensor0 = findViewById(R.id.sensor0);
        valorSensor1 = findViewById(R.id.sensor1);
        valorSensor2 = findViewById(R.id.sensor2);
        valorSensor3 = findViewById(R.id.sensor3);



        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        showLocationTxt=findViewById(R.id.show_location);
        getlocationBtn=findViewById(R.id.getLocation);

        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                proximidade();
                light();
                temperatura();
                umidade();



                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);


                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    OnGPS();
                }
                else
                {
                    getLocation();
                }

                showLocationTxt.setText("latitude " + latitude + " longitude" + longitude);
            }
        });
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude = lat;
                longitude = longi;
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude = lat;
                longitude = longi;
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude = lat;
                longitude = longi;
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Habilitar GPS").setCancelable(false).setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    public class proxSensor implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            proxSensor = event.values[0];

            valorSensor0.setText("A " + proxSensor);
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

    public class lightSensor implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            lightSensor = event.values[0];

            valorSensor1.setText("C " + lightSensor);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    public void light(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor1 = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(new lightSensor(), sensor1, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void temperatura(){
        Random aleatorio = new Random();
        temperatura = aleatorio.nextInt((40 - 0) + 1) + 0;

        valorSensor2.setText(temperatura + "°C");
    }

    public void umidade(){
        Random aleatorio = new Random();
        umidade = aleatorio.nextInt((120 - 0) + 1) + 0;

        valorSensor3.setText(umidade + "% de umidade");
    }

    public void enviaWebservice(View view) throws IOException {
        if (temperatura == -11){
            Context contexto = getApplicationContext();
            String texto = "Clique em 'LISTAR SENSORES'";
            int duracao = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();
        }
        else {
            com.example.jsonsensor.Sensor sensor = new com.example.jsonsensor.Sensor(umidade, new Position(latitude, longitude), lightSensor.doubleValue(), proxSensor.doubleValue(), temperatura);
            String json = new Gson().toJson(sensor);

            new Thread(new WebConnection(json)).start();

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://android-sensor-front.herokuapp.com/"));
            startActivity(browserIntent);
        }
    }


}
