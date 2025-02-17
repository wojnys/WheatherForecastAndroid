package com.example.wheatherforecast;

public class ForecastRow
{
    private String dateTime;
    private String temp;
    private String description;
    private String icon;

    public ForecastRow(String dateTime, String temp, String description, String icon) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.description = description;
        this.icon = icon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTemp() {
        return temp;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
