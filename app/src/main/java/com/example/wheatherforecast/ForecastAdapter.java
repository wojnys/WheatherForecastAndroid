package com.example.wheatherforecast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    Context context;
    ArrayList<ForecastRow> forecastModel;

    public ForecastAdapter(Context context, ArrayList<ForecastRow> forecastModel) {
        this.context = context;
        this.forecastModel = forecastModel;
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {
        holder.iconImageView.setImageResource(context.getResources().getIdentifier("icon_" + forecastModel.get(position).getIcon(), "drawable", context.getPackageName()));

        holder.dateTimeTextView.setText(forecastModel.get(position).getDateTime());

        double temp = Double.parseDouble(forecastModel.get(position).getTemp());
        int color = Color.rgb(1,2,1);
        if (temp < 10) {
            color = Color.rgb(30,144,255);
        } else if (temp > 10 && temp < 25) {
            color = Color.rgb(255,166,0);
        } else {
            color = Color.RED;
        }


        holder.tempTextView.setText(forecastModel.get(position).getTemp() +"°C");
        holder.tempTextView.setTextColor(color);
        holder.descriptionTextView.setText(forecastModel.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return forecastModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView tempTextView;
        TextView descriptionTextView;
        TextView dateTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImageView = itemView.findViewById(R.id.IconImageForecast);
            tempTextView = itemView.findViewById(R.id.tempText);
            descriptionTextView = itemView.findViewById(R.id.descText);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeText);
        }
    }
}
