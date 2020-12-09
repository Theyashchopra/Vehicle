package com.indiaactive.vehicle.datamodels;

import com.google.gson.annotations.SerializedName;

public class Vehicles {
    public int v_id;
    public String name;
    public String added_on;
    public String modified_on;
    public int vehicle_model_id;
    public String model_name;
    public String year_of_man;
    public int total_run_hrs;
    public int run_km_hr;
    public int fuel_consumption;
    public int average_fuel_consumption;
    public int rent_per_day_with_fuel;
    public int rent_per_hour_with_fuel;
    public int rent_per_hour_without_fuel;
    public int rent_per_day_without_fuel;
    public int owner_id;
    public String owner_name;
    public String owner_mobile;
    public boolean availibility;
    public int driver_id;
    public double lat;
    @SerializedName("long")
    public double lon;
    public float rotation;
    public String plate_no;
    public String busy_start;
    public String busy_end;
    public boolean isDocument;
    public boolean isImage;
    private String rent_cost;

    public Vehicles(){}

    public int getV_id() {
        return v_id;
    }

    public String getName() {
        return name;
    }

    public String getAdded_on() {
        return added_on;
    }

    public String getModified_on() {
        return modified_on;
    }

    public int getVehicle_model_id() {
        return vehicle_model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getYear_of_man() {
        return year_of_man;
    }

    public int getTotal_run_hrs() {
        return total_run_hrs;
    }

    public int getRun_km_hr() {
        return run_km_hr;
    }

    public int getFuel_consumption() {
        return fuel_consumption;
    }

    public int getAverage_fuel_consumption() {
        return average_fuel_consumption;
    }

    public int getRent_per_day_with_fuel() {
        return rent_per_day_with_fuel;
    }

    public int getRent_per_hour_with_fuel() {
        return rent_per_hour_with_fuel;
    }

    public int getRent_per_hour_without_fuel() {
        return rent_per_hour_without_fuel;
    }

    public int getRent_per_day_without_fuel() {
        return rent_per_day_without_fuel;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public boolean isAvailibility() {
        return availibility;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public float getRotation() {
        return rotation;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public String getBusy_start() {
        return busy_start;
    }

    public String getBusy_end() {
        return busy_end;
    }

    public boolean isDocument() {
        return isDocument;
    }

    public boolean isImage() {
        return isImage;
    }

    public String getRent_cost() {
        return rent_cost;
    }

    public void setRent_cost(String rent_cost) {
        this.rent_cost = rent_cost;
    }
}

