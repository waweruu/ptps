package com.isproject.ptps;

public interface DataModels {
    int MODEL_VEHICLE = 101;
    int MODEL_OPERATOR = 102;
    int MODEL_FARE_CHART = 103;
    int MODEL_SUBROUTES = 104;
    int MODEL_LIST = 105;
    int MODEL_PAYMENT_RECEIPTS = 106;
  
    int MODEL_NUMBER_PLATE = 107;
    int MODEL_REVIEW = 108;

    int MODEL_OWNER_VEHICLES=109;
    int MODEL_OWNER_PAYMENTS=110;

    int getModelType();
}
