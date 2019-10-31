package com.isproject.ptps;

public class SubRoutesList extends DataObject implements DataModels {

    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public int getModelType() {
        return DataModels.MODEL_LIST;
    }
}
