package com.mc3699.surge.base;

public class ElectricalLogic implements IElectricalLogic {

    private float voltage, maxVoltage;
    private float current, maxCurrent;
    private float resistance;

    public ElectricalLogic(float maxVoltage, float maxCurrent, float resistance)
    {
        this.maxVoltage = maxVoltage;
        this.maxCurrent = maxCurrent;
        this.resistance = resistance;
    }


    @Override
    public float getVoltage() {
        return voltage;
    }

    @Override
    public float getCurrent() {
        return current;
    }

    @Override
    public float getResistance() {
        return resistance;
    }

    @Override
    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    @Override
    public void setCurrent(float current) {
        this.current = Math.min(current, maxCurrent);
    }

    @Override
    public void setResistance(float resistance) {
        this.resistance = resistance;
    }
}
