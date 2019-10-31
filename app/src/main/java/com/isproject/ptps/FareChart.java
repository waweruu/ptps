package com.isproject.ptps;

public class FareChart extends DataObject implements DataModels {

    private String routeStart, routeFinish, routeNumber;
    private boolean isExpanded;

    public FareChart() {
    }

    public FareChart(String routeStart, String routeFinish, String routeNumber) {
        this.routeStart = routeStart;
        this.routeFinish = routeFinish;
        this.routeNumber = routeNumber;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getRouteFinish() {
        return routeFinish;
    }

    public void setRouteFinish(String routeFinish) {
        this.routeFinish = routeFinish;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_FARE_CHART;
    }
}
