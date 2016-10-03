package com.example.alexdriedger.weatherapplp;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set onClickListerner for the button
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchWeather(){
                    @Override
                    protected void onPostExecute(Double temperature) {
                        super.onPostExecute(temperature);

                        Log.v("Fetch Weather", "" + temperature);

                        // Prepare String to be displayed
                        Resources res = getResources();
                        String weatherMessage = String.format(res.getString(R.string.weather_message), temperature);

                        // Update textview with the updated weather
                        TextView textView = (TextView) findViewById(R.id.temp_text_view);
                        textView.setText(weatherMessage);

                    }
                }.execute();
                Toast.makeText(getApplicationContext(), R.string.toast_message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
