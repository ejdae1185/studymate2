package com.example.studymate2;

public class MarkerItem {
    double latitute;
    double longitute;
    String name;

    public MarkerItem(double latitute, double longitute, String name) {
        this.latitute = latitute;
        this.longitute = longitute;
        this.name = name;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


