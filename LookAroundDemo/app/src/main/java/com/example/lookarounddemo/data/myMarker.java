package com.example.lookarounddemo.data;

public class myMarker {
    private double latitude;//纬度
    private double longitude;//经度

    public myMarker(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
