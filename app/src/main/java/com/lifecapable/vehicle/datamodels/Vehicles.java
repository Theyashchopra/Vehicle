package com.lifecapable.vehicle.datamodels;

public class Vehicles {

    private String name,number,contact;
    private double latitude,longitude;

    public Vehicles(String name, String number, String contact, double latitude, double longitude) {
        this.name = name;
        this.number = number;
        this.contact = contact;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getContact() {
        return contact;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
