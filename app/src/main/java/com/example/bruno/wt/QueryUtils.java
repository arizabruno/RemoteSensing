package com.example.bruno.wt;

import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static Dados fetchDados (String requestUrl) {


        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        Dados dados = extractDados(jsonResponse);

        return dados;



    }

    private static Dados extractDados(String dadosJson) {
        if (TextUtils.isEmpty(dadosJson)) {
            return null;
        }

        try {
            JSONObject baseResponse = new JSONObject(dadosJson);
            double latitude = baseResponse.getDouble("latitude");
            double longitude = baseResponse.getDouble("longitude");
            JSONObject currently = baseResponse.getJSONObject("currently");
            double temp = currently.getDouble("temperature");
            double windSpeed = currently.getDouble("windSpeed");


            String location = "Lat: " + latitude + "Long:" + longitude + "";


            Dados jsonDados = new Dados(windSpeed, temp, location, latitude, longitude);

            return jsonDados;
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results.", e);
        }

        return null;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrievieng the earthquake JSON results", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }

        return output.toString();
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL", e);
        }
        return url;
    }


}
