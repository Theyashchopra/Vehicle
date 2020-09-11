package com.lifecapable.vehicle.datamodels;

public class DriverData {
    String driverName;
    String organizationName;

    public DriverData(String driverName, String organizationName) {
        this.driverName = driverName;
        this.organizationName = organizationName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
