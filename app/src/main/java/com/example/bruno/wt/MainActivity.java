package com.example.bruno.wt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // Declaring the TextViews used in the layout
    TextView tempratureTextView, windForceTextView, locationTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Attribuing values to the declared TextView
        tempratureTextView = (TextView) findViewById(R.id.temperature_force_text_view);
        windForceTextView = (TextView) findViewById(R.id.wind_force_text_view);
        locationTextView = (TextView) findViewById(R.id.location_text_view);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
