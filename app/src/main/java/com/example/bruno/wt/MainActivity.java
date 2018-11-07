package com.example.bruno.wt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.Permission;
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


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {

            ativarLocalizacao.isPressed();



        }



        ativarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
                        .permission
                        .ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);

            }
        });




        darkSkyUrl = "https://api.darksky" +
                ".net/forecast/e383ecf5828fc78c327e8d8cff4be13e/27.964157,-82.452606";

        taskLocation = mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new
                        OnSuccessListener<Location>() {
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

                                    darkSkyUrl = "https://api.darksky.net/forecast/e383ecf5828fc78c327e8d8cff4be13e/" +
                                            latitude + "," + longitude;
                                }




                            }


                        });



    }

    private class DadosAsyncTask extends AsyncTask<String, Void, Dados> {
        @Override
        protected Dados doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            Dados finalDados = QueryUtils.fetchDados(urls[0]);

            finalDados.setLocation(urls[1]);

            return finalDados;
        }

        @Override
        protected void onPostExecute(Dados dados) {

            int fTemp = (int) (dados.getTemperature());

            int mphSpeed = (int) (dados.getSpeedForce());

            String locationString = dados.getLocation();

            String temperature = "" + fTemp + " °F";
            String windSpeed = "" + mphSpeed + " mph";

            temperatureTextView.setText(temperature);
            windForceTextView.setText(windSpeed);
            locationTextView.setText(locationString);

        }


    }



    public boolean checkingPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
//                    .ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
//                    .ACCESS_COARSE_LOCATION}, FINE_LOCATION_PERMISSION);



            return false;

        } else {

            return true;
        }
    }

}
