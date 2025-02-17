package com.example.wheatherforecast;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


public class FragmentGraph extends Fragment {

    LineChart lineChart;

    public FragmentGraph() {
        // Required empty public constructo
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        lineChart = view.findViewById(R.id.chart);

        SharedPreferencesHelper sharedPreferencesHelper;
        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());

        // Get city from arguments
        String city = sharedPreferencesHelper.getCity();

        if (city != null) {
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=9c29437b06080dd19b7cccfd88f38670&units=metric";
            new FragmentGraph.FetchWeatherData().execute(url);
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

            int day = 0;
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray list = jsonResponse.getJSONArray("list");


                // Create a list of entries
                ArrayList<Entry> entries = new ArrayList<>();

                for (int i = 0; i < list.length(); i++) {
                    JSONObject forecast = list.getJSONObject(i);
                    String dateTime = forecast.getString("dt_txt");

                    JSONObject main = forecast.getJSONObject("main");
                    JSONArray weatherArray = forecast.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);

                    String temp = main.getString("temp");
                    String description = weather.getString("description");
                    String icon = weather.getString("icon");


                    String hour = dateTime.substring(11,12);
                    if(i % 3 == 0) {
                        // add data to chart
                        entries.add(new Entry(i, (float)main.getDouble("temp")));
                    }
                }


                // redrwa graph
                // Create a LineDataSet and customize its appearance
                LineDataSet lineDataSet = new LineDataSet(entries, "3 days Forecast");
                lineDataSet.setColor(android.graphics.Color.BLUE);
//                lineDataSet.setValueTextColor(android.graphics.Color.BLACK);
                lineDataSet.setLineWidth(2f);

                LineData lineData = new LineData(lineDataSet);

                lineChart.setData(lineData);

                // Optional: Customize X-Axis and Y-Axis
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);

                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false); // Hide right Y-Axis

                // Refresh the chart
                lineChart.invalidate();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
