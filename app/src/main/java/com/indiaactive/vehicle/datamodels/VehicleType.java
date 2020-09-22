package com.indiaactive.vehicle.datamodels;

public class VehicleType {
    public int id;
    public int master_id;
    public String vehicle_type_name;
    public String vehicle_master;
    public boolean status;
    public String modified_on;
    public String added_on;
    public String ip_address;

    VehicleType(){ }

    public int getId() {
        return id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public String getVehicle_type_name() {
        return vehicle_type_name;
    }

    public String getVehicle_master() {
        return vehicle_master;
    }

    public boolean isStatus() {
        return status;
    }

    public String getModified_on() {
        return modified_on;
    }

    public String getAdded_on() {
        return added_on;
    }

    public String getIp_address() {
        return ip_address;
    }
}
