package com.example.firebaseyo;

public class Location {
    private double Longitude;
    private double Latitude;

    public Location(double longitude,double latitude) {
        Longitude=longitude;
        Latitude=latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
