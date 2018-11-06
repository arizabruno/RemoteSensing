package com.example.bruno.wt;

public class Dados {

    private double mSpeedForce;
    private double mTemperature;
    private String mLocation;

    public Dados(double speedForce, double temperature, String location) {
        mSpeedForce = speedForce;
        mTemperature = temperature;
        mLocation = location;
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
