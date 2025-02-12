package com.mc3699.surge.base;

public class ElectricalLogic implements IElectricalLogic {

    private float voltage, maxVoltage;
    private float amperage, maxAmperage;
    private float resistance;

    public ElectricalLogic(float maxVoltage, float maxAmperage, float resistance)
    {
        this.maxVoltage = maxVoltage;
        this.maxAmperage = maxAmperage;
        this.resistance = resistance;
    }


    @Override
    public float getVoltage() {
        return voltage;
    }

    @Override
    public float getAmperage() { return amperage; }

    @Override
    public float getResistance() {
        return resistance;
    }

    @Override
    public void setVoltage(float voltage) {
        this.voltage = Math.min(voltage, maxVoltage);
    }

    @Override
    public void setAmperage(float amperage) {
        this.amperage = Math.min(amperage, maxAmperage);
    }

    @Override
    public void setResistance(float resistance) {
        this.resistance = resistance;
    }
}
