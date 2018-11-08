package com.example.bruno.wt;

public class Dados {

    private double mSpeedForce;
    private double mTemperature;
    private double mLatitude;
    private double mLongitude;
    private String mLocation;

    public Dados(double speedForce, double temperature, String location, double latitude, double
            longitde) {
        mSpeedForce = speedForce;
        mTemperature = temperature;
        mLocation = location;
        mLatitude = latitude;
        mLongitude = longitde;
    }


    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitde(double longitude) {
        this.mLongitude = longitude;
    }

    public double getSpeedForce() {
        return mSpeedForce;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }


}
