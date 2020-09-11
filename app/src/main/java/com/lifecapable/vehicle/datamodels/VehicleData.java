package com.lifecapable.vehicle.datamodels;

public class VehicleData {
    int vehicleImage;
    String vehicleName;

    public VehicleData(int vehicleImage, String name) {
        this.vehicleImage = vehicleImage;
        this.vehicleName = name;
    }
    public int getVehicleImage() {
        return vehicleImage;
    }
    public void setVehicleImage(int vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
    public String getVehicleName() {
        return vehicleName;
    }
    public void setVehicleName(String name) {
        this.vehicleName = name;
    }
}
