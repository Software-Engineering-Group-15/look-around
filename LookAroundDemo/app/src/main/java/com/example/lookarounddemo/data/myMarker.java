package com.example.lookarounddemo.data;

public class myMarker {
    private double latitude;//纬度
    private double longitude;//经度
    private String title;
    private boolean ifnew = false;

    public myMarker(double latitude, double longitude, String title){
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public myMarker(double latitude, double longitude, String title, boolean ifnew){
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.ifnew = ifnew;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public boolean getIfnew() {
        return ifnew;
    }
}
