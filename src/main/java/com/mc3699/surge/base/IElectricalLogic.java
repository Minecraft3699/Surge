package com.mc3699.surge.base;

public interface IElectricalLogic {

    float getVoltage();
    float getAmperage();
    float getResistance();

    void setVoltage(float voltage);
    void setAmperage(float amperage);
    void setResistance(float resistance);

}
