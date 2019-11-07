package com.isproject.ptps;

public class NumberPlate extends DataObject implements DataModels {
    private String vehicle;

    public NumberPlate(String vehicle) {
        this.vehicle = vehicle;
    }

    public NumberPlate() {
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;

    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_NUMBER_PLATE;

    }
}
