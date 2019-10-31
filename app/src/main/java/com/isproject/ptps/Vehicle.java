package com.isproject.ptps;

public class Vehicle extends DataObject implements DataModels {

    private String vehicleCapacity;
    private String vehicleType;

    private boolean isExpanded;

    public Vehicle() {}

    public Vehicle(String vehicleCapacity, String vehicleType) {
        this.vehicleCapacity = vehicleCapacity;
        this.vehicleType = vehicleType;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_VEHICLE;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
