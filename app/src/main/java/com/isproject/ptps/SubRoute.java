package com.isproject.ptps;

public class SubRoute extends DataObject implements DataModels {

    private String subrouteStart;
    private String subrouteFinish;
    private String subroutePrice;



    public SubRoute() {
        //Constructor required for FireBase
    }

    public String getSubrouteStart() {
        return subrouteStart;
    }

    public void setSubrouteStart(String subrouteStart) {
        this.subrouteStart = subrouteStart;
    }

    public String getSubrouteFinish() {
        return subrouteFinish;
    }

    public void setSubrouteFinish(String subrouteFinish) {
        this.subrouteFinish = subrouteFinish;
    }

    public String getSubroutePrice() {
        return subroutePrice;
    }

    public void setSubroutePrice(String subroutePrice) {
        this.subroutePrice = subroutePrice;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_SUBROUTES;
    }
}
