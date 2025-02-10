package com.mc3699.surge.base;

public interface IElectricalLogic {

    float getVoltage();
    float getCurrent();
    float getResistance();

    void setVoltage(float voltage);
    void setCurrent(float current);
    void setResistance(float resistance);

}
