package com.indiaactive.vehicle.datamodels;

import java.util.List;

public class ListVehicles {
    List<Vehicles> vehicles;

    public ListVehicles(List<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }
}
