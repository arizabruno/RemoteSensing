package com.example.bruno.wt;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Declaring the TextViews used in the layout
    TextView temperatureTextView, windForceTextView, locationTextView;

    private static final String OPEN_WEATHER_URL = "https://api.darksky.net/forecast/e383ecf5828fc78c327e8d8cff4be13e/27.964157,-82.452606";


    LocationManager locationManager;
    String provider;
    static double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attribuing values to the declared TextView
        temperatureTextView = (TextView) findViewById(R.id.temperature_force_text_view);
        windForceTextView = (TextView) findViewById(R.id.wind_force_text_view);
        locationTextView = (TextView) findViewById(R.id.location_text_view);

        DadosAsyncTask task = new DadosAsyncTask();
        task.execute(OPEN_WEATHER_URL);

    }

    private class DadosAsyncTask extends AsyncTask<String , Void, Dados> {
        @Override
        protected Dados doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            Dados finalDados = QueryUtils.fetchDados(urls[0]);

            return finalDados;
        }

        @Override
        protected void onPostExecute(Dados dados) {

            int fTemp = (int) (dados.getTemperature());

            int mphSpeed = (int) (dados.getSpeedForce());

            String temperature = "" + fTemp + " °F";
            String windSpeed = "" + mphSpeed + " mph";

            temperatureTextView.setText(temperature);
            windForceTextView.setText(windSpeed);

        }
    }



}
