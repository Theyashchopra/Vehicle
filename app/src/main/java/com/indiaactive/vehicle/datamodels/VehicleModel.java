package com.indiaactive.vehicle.datamodels;

public class VehicleModel {
    public int id;
    public int vehicle_type_id;
    public String vehicle_type_name;
    public String model_name;
    public boolean status;
    public String added_on;
    public String modified_on;
    public String ip_address;

    VehicleModel(){}

    public int getId() {
        return id;
    }

    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public String getVehicle_type_name() {
        return vehicle_type_name;
    }

    public String getModel_name() {
        return model_name;
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
}
