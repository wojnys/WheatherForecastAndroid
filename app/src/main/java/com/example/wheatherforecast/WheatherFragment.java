package com.example.wheatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WheatherFragment extends Fragment {

    private TextView temperatureText;
    private TextView descriptionText;
    private ImageView iconImage;
    private Button submitButton;
    private EditText inputCityName;

    private SharedPreferencesHelper sharedPreferencesHelper;

    public WheatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wheather, container, false);

        // Initialize UI elements
        inputCityName = view.findViewById(R.id.inputCityName);
        temperatureText = view.findViewById(R.id.temperatureText);
        descriptionText = view.findViewById(R.id.descriptionText);
        iconImage = view.findViewById(R.id.IconImageForecast);
        submitButton = view.findViewById(R.id.submitButton);

        // Initialize SharedPreferencesHelper
        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        // Optional: Retrieve saved city and pre-fill the input field
        String savedCity = sharedPreferencesHelper.getCity();
        System.out.println("city: " + savedCity);
        if (savedCity != null) {
            inputCityName.setText(savedCity);
            this.submitCity();
        }

        // Set up button click listener
        submitButton.setOnClickListener(v -> {
            this.submitCity();
        });

        return view;
    }

    private void submitCity() {
        String city = inputCityName.getText().toString().trim();

        if (city.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter a city name", Toast.LENGTH_SHORT).show();
        } else {
            sharedPreferencesHelper.saveCity(city);
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=9c29437b06080dd19b7cccfd88f38670&units=metric";
            new FetchWeatherData().execute(url);
        }
    }

    // AsyncTask to fetch weather data
    private class FetchWeatherData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlString = strings[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject main = jsonResponse.getJSONObject("main");
                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);

                // Extract data
                String temperature = main.getString("temp") + "°C";
                String description = weather.getString("description");
                String icon = weather.getString("icon");

                double temp = main.getInt("temp");
                int color = Color.rgb(1,2,1);
                if (temp < 10) {
                    color = Color.rgb(30,144,255);
                } else if (temp > 10 && temp < 25) {
                    color = Color.rgb(255,166,0);
                } else {
                    color = Color.RED;
                }

                temperatureText.setText(temperature);
                temperatureText.setTextColor(color);

                descriptionText.setText(description);
                descriptionText.setTextColor(color);

                iconImage.setImageResource(getResources().getIdentifier("icon_" + icon, "drawable", getActivity().getPackageName()));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
