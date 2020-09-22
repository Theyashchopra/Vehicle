package com.indiaactive.vehicle.datamodels;

public class MasterVehicle {
    public int id;
    public String name;
    public boolean status;
    public String added_on;
    public String modified_on;
    public String ip_address;
    public String image;
    MasterVehicle(){}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public String getAdded_on() {
        return added_on;
    }

    public String getModified_on() {
        return modified_on;
    }

    public String getIp_address() {
        return ip_address;
    }

    public String getImage() {
        return image;
    }
}
