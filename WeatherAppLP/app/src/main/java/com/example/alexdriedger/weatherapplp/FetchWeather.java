package com.example.alexdriedger.weatherapplp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fetch the weather for Houston Texas using the
 * Open Weather Map API. Code is adapted from Udacity.com
 * Note: Defined as an abstract class so that the textview can
 * be updated from MainActivity
 */
abstract class FetchWeather extends AsyncTask<Void, Void, Double> {

    private final String LOG_TAG = FetchWeather.class.getSimpleName();

    @Override
    protected Double doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Houston,us,uk&units=metric&appid=67e21d4598e815694409fec740a424b6");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            // Close the URL Connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close the buffer reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getWeatherDataFromJSON(forecastJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Finds the temperture data for Houston within the JSON query
     *
     * @param forcast String to get data from
     * @return tempture (degrees Celcius) for Houston, TX
     * @throws JSONException
     */
    private double getWeatherDataFromJSON(String forcast) throws JSONException {
        JSONObject weather = new JSONObject(forcast);
        JSONObject main = weather.getJSONObject("main");
        return main.getDouble("temp");
    }

}
