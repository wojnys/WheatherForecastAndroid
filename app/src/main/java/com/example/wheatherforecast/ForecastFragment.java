package com.example.wheatherforecast;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    private ArrayList<ForecastRow> forecastModel = new ArrayList<>();
    private RecyclerView recyclerView;

    private SharedViewModel sharedViewModel;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.myRecyclerView);

        SharedPreferencesHelper sharedPreferencesHelper;
        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        // Get city from arguments
        String city = sharedPreferencesHelper.getCity();

        if (city != null) {
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=9c29437b06080dd19b7cccfd88f38670&units=metric";
            new FetchWeatherData().execute(url);
        } else {
            Toast.makeText(getActivity(), "City not provided", Toast.LENGTH_SHORT).show();
        }

        return view;
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
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray list = jsonResponse.getJSONArray("list");

                forecastModel.clear();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject forecast = list.getJSONObject(i);
                    String dateTime = forecast.getString("dt_txt");

                    JSONObject main = forecast.getJSONObject("main");
                    JSONArray weatherArray = forecast.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);

                    String temp = main.getString("temp");
                    String description = weather.getString("description");
                    String icon = weather.getString("icon");

                    forecastModel.add(new ForecastRow(dateTime, temp, description, icon));
                }

                // Set up RecyclerView
                ForecastAdapter adapter = new ForecastAdapter(getActivity(), forecastModel);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static ForecastFragment newInstance(String city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        fragment.setArguments(args);
        return fragment;
    }
}
