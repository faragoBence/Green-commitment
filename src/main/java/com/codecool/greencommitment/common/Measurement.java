package com.codecool.greencommitment.common;

public abstract class  Measurement {

    long currentTime;
    int unit;
    String unitOfMeasurement;

    public Measurement(long currentTime, int unit, String unitOfMeasurement){
        this.currentTime = currentTime;
        this.unit = unit;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public int getUnit() {
        return unit;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }
    public long getCurrentTime() {
        return currentTime;
    }
}
