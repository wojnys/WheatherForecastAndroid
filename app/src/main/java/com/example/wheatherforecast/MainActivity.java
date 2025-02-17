package com.example.wheatherforecast;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wheatherforecast.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding biding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add biding for bottom view menu
        biding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());

        replaceFragment(new WheatherFragment());

        biding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.weather) {
                replaceFragment(new WheatherFragment());
            }
            if (item.getItemId() == R.id.forecast) {
                replaceFragment(new ForecastFragment());
            }
            if(item.getItemId() == R.id.chart){
                replaceFragment(new FragmentGraph());
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}
