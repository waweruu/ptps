package com.isproject.ptps;

public class NumberPlate extends DataObject implements DataModels {

    private String numberPlate;

    public NumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public NumberPlate() {
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_OWNER_VEHICLES;
    }
}
