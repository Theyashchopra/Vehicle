package com.indiaactive.vehicle.datamodels;

public class VehicleDetailsData {
    String name;
    int v_id;
    int vehicle_model_id;
    String year_of_man;
    int total_run_hrs;
    int run_km_hr;
    int fuel_consumption_rate;
    int fuel_average_consumption_rate;
    int rent_per_day_with_fuel;
    int rent_per_hour_with_fuel;
    int rent_per_hour_without_fuel;
    int rent_per_day_without_fuel;
    String ip_address;
    String plate_no;
    boolean availibility;
    int owner_id;
    String model_name;
    int driver_id;

    public VehicleDetailsData(String name, int vehicle_model_id, String yom, int total_run_hrs, int run_km_hr, int fuel_consumption_rate, int fuel_average_consumption_rate, int rent_per_day_with_fuel, int rent_per_hour_with_fuel, int rent_per_hour_without_fuel, int rent_per_day_without_fuel, String ip_address, String plate_no, boolean availibility, int owner_id) {
        this.name = name;
        this.vehicle_model_id = vehicle_model_id;
        this.year_of_man = yom;
        this.total_run_hrs = total_run_hrs;
        this.run_km_hr = run_km_hr;
        this.fuel_consumption_rate = fuel_consumption_rate;
        this.fuel_average_consumption_rate = fuel_average_consumption_rate;
        this.rent_per_day_with_fuel = rent_per_day_with_fuel;
        this.rent_per_hour_with_fuel = rent_per_hour_with_fuel;
        this.rent_per_hour_without_fuel = rent_per_hour_without_fuel;
        this.rent_per_day_without_fuel = rent_per_day_without_fuel;
        this.ip_address = ip_address;
        this.plate_no = plate_no;
        this.availibility = availibility;
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVehicle_model_id() {
        return vehicle_model_id;
    }

    public void setVehicle_model_id(int vehicle_model_id) {
        this.vehicle_model_id = vehicle_model_id;
    }

    public String getYom() {
        return year_of_man;
    }

    public void setYom(String yom) {
        this.year_of_man = yom;
    }

    public int getTotal_run_hrs() {
        return total_run_hrs;
    }

    public void setTotal_run_hrs(int total_run_hrs) {
        this.total_run_hrs = total_run_hrs;
    }

    public int getRun_km_hr() {
        return run_km_hr;
    }

    public void setRun_km_hr(int run_km_hr) {
        this.run_km_hr = run_km_hr;
    }

    public int getFuel_consumption_rate() {
        return fuel_consumption_rate;
    }

    public void setFuel_consumption_rate(int fuel_consumption_rate) {
        this.fuel_consumption_rate = fuel_consumption_rate;
    }

    public int getFuel_average_consumption_rate() {
        return fuel_average_consumption_rate;
    }

    public void setFuel_average_consumption_rate(int fuel_average_consumption_rate) {
        this.fuel_average_consumption_rate = fuel_average_consumption_rate;
    }

    public int getRent_per_day_with_fuel() {
        return rent_per_day_with_fuel;
    }

    public void setRent_per_day_with_fuel(int rent_per_day_with_fuel) {
        this.rent_per_day_with_fuel = rent_per_day_with_fuel;
    }

    public int getRent_per_hour_with_fuel() {
        return rent_per_hour_with_fuel;
    }

    public void setRent_per_hour_with_fuel(int rent_per_hour_with_fuel) {
        this.rent_per_hour_with_fuel = rent_per_hour_with_fuel;
    }

    public int getRent_per_hour_without_fuel() {
        return rent_per_hour_without_fuel;
    }

    public void setRent_per_hour_without_fuel(int rent_per_hour_without_fuel) {
        this.rent_per_hour_without_fuel = rent_per_hour_without_fuel;
    }

    public int getRent_per_day_without_fuel() {
        return rent_per_day_without_fuel;
    }

    public void setRent_per_day_without_fuel(int rent_per_day_without_fuel) {
        this.rent_per_day_without_fuel = rent_per_day_without_fuel;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public boolean isAvailibility() {
        return availibility;
    }

    public void setAvailibility(boolean availibility) {
        this.availibility = availibility;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public int getV_id() {
        return v_id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public String getYear_of_man() {
        return year_of_man;
    }
}
