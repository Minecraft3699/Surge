package com.mc3699.surge.base;

public interface ElectricalConductor {
    void setTemperature(float temperature);
    float getTemperature();

    double getResistance();
    int getMaxAmperage();
}
