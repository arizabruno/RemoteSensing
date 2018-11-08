package com.example.bruno.wt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Declaring the TextViews used in the layout
    TextView temperatureTextView, windForceTextView, locationTextView;

    private final int COARSE_LOCATION_PERMISSION = 9999;

    private static final String OPEN_WEATHER_URL = "https://api.darksky.net/forecast/e383ecf5828fc78c327e8d8cff4be13e/27.964157,-82.452606";

    private FusedLocationProviderClient mFusedLocationClient;

    private double latitude, longitude;

    private String localizacaoFinal = "teste";


    String darkSkyUrl;

    private boolean verificacaoConcluida = true;

    List<Address> localizacao;

    private CheckBox checkBoxLocation;

    private Task<Location> taskLocation;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case COARSE_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    //mFusedLocationClient = getFusedLocaion(MainActivity.this);\

                    FusedLocationProviderClient xFusedLocationClient = getFusedLocaion
                            (MainActivity.this);

//                    String locals = taskLocationFunction(xFusedLocationClient, geocoder,
//                            MainActivity.this);



                    darkSkyUrl = "https://api.darksky.net/forecast/e383ecf5828fc78c327e8d8cff4be13e/" +
                                latitude + "," + longitude;


                    DadosAsyncTask task = new DadosAsyncTask();
                    task.execute(darkSkyUrl, localizacaoFinal);
                }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Geocoder geocoder = new Geocoder(this);

        // Attribuing values to the declared TextView
        temperatureTextView = (TextView) findViewById(R.id.temperature_force_text_view);
        windForceTextView = (TextView) findViewById(R.id.wind_force_text_view);
        locationTextView = (TextView) findViewById(R.id.location_text_view);

        Button ativarLocalizacao = (Button) findViewById(R.id.ativar_localizacao);


        FusedLocationProviderClient mFusedLocationClient = LocationServices
                .getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {


        }



        ativarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
                        .permission
                        .ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);

            }
        });




        darkSkyUrl = "https://api.darksky" + "" +
                ".net/forecast/e383ecf5828fc78c327e8d8cff4be13e/43.208487,%20-71.536788";

        taskLocationFunction(mFusedLocationClient, geocoder, MainActivity.this);


    }



    public FusedLocationProviderClient getFusedLocaion(Context context) {
        FusedLocationProviderClient f1 = LocationServices.getFusedLocationProviderClient(context);

        return f1;
    }

    @SuppressLint("MissingPermission")
    public String taskLocationFunction (FusedLocationProviderClient fusedLocationProviderClient,
                                      final Geocoder geocoder, Activity activity) {


        final String[] local = {"oi"};

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {


            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();


                    try {
                        localizacao = geocoder.getFromLocation(latitude, longitude,
                                1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    localizacaoFinal = localizacao.get(0).getLocality() + ", " + localizacao.get(0)
                            .getAdminArea();

                    local[0] = localizacao.get(0).getLocality() + ", " + localizacao.get(0)
                            .getAdminArea();;

                    darkSkyUrl = "https://api.darksky.net/forecast/e383ecf5828fc78c327e8d8cff4be13e/" +
                            latitude + "," + longitude;
                }
            }
        });

        return local[0].toString();

    }

    private class DadosAsyncTask extends AsyncTask<String, Void, Dados> {

        @Override
        protected Dados doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            Dados finalDados = QueryUtils.fetchDados(urls[0]);

            Geocoder geo = new Geocoder(MainActivity.this);


            finalDados.setLocation(urls[1]);

            return finalDados;
        }

        @Override
        protected void onPostExecute(Dados dados) {

            int fTemp = (int) (dados.getTemperature());

            int mphSpeed = (int) (dados.getSpeedForce());

            String locationString = dados.getLocation();

            String locationLatLon = dados.getLatitude() + ", " + dados.getLongitude();

            String temperature = "" + fTemp + " °F";
            String windSpeed = "" + mphSpeed + " mph";

            temperatureTextView.setText(temperature);
            windForceTextView.setText(windSpeed);
            locationTextView.setText(locationLatLon);

        }


    }

}