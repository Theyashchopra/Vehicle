package com.indiaactive.vehicle.datamodels;

import java.util.List;

public class ListOfVehicle {
    List<String> names;

    public ListOfVehicle(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
