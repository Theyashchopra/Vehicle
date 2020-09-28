package com.indiaactive.vehicle.datamodels;

import com.google.gson.annotations.SerializedName;

public class Vehicles {

    private String name, kms, rentPerDay, rentPerHour, year, plateNumber;
    private double lat;
    @SerializedName("long")
    private double lon;

    public Vehicles(String name, String kms, String rentPerDay, String rentPerHour, String year, String plateNumber, double lat, double lon) {
        this.name = name;
        this.kms = kms;
        this.rentPerDay = rentPerDay;
        this.rentPerHour = rentPerHour;
        this.year = year;
        this.plateNumber = plateNumber;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(String rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public String getRentPerHour() {
        return rentPerHour;
    }

    public void setRentPerHour(String rentPerHour) {
        this.rentPerHour = rentPerHour;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

