package com.indiaactive.vehicle.datamodels;

public class Vehicles {

    private String name, kms, rentPerDay, rentPerHour, year, plateNumber;
    private double latitude, longitude;

    public Vehicles(String plateNumber, String name, String kms, String rentPerDay, String rentPerHour, String year, double latitude, double longitude) {
        this.name = name;
        this.kms = kms;
        this.rentPerDay = rentPerDay;
        this.rentPerHour = rentPerHour;
        this.year = year;
        this.plateNumber = plateNumber;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}

