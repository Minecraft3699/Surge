package com.mc3699.surge.transfer.copperWire;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CopperWireBlockEntity extends BlockEntity implements CircuitPart, ElectricalConductor {
    private float temperature = 20;

    public CopperWireBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.COPPER_WIRE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public double getResistance() {
        return 0.0000002688 * (1 + 0.00386 * (temperature - 20));
    }

    @Override
    public int getMaxAmperage() {
        return 375000;
    }

    /*
    @Override
    public void tick() {

        if(level.isClientSide()) return;

        boolean foundAWire = false;
        // this entire thing prolly needs a rewrite :sob:
        for(Direction dir : Direction.values())
        {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if(neighbor instanceof BasicWireBlockEntity targetWire)
            {
                foundAWire = true;
                ElectricalLogic targetLogic = targetWire.electricalLogic;

                // sharing polarities
                if (targetWire.isCompletelyNeutral() && !this.isCompletelyNeutral())
                { // share this wires polarity with the next
                    this.setPolarity(dir, ElectricalPolarity.POSITIVE);
                    targetWire.setPolarity(dir.getOpposite(), ElectricalPolarity.NEGATIVE);
                }

                if (this.getPolarity(dir) != ElectricalPolarity.NEGATIVE) { continue; }
                // electrical math bs
                float localVoltage = electricalLogic.getVoltage();
                float localCurrent = electricalLogic.getAmperage();
                float localResistance = electricalLogic.getResistance();

                float targetVoltage = targetLogic.getVoltage();
                float targetCurrent = targetLogic.getAmperage();
                float targetResistance = targetLogic.getResistance();

                float totalResist = localResistance + targetResistance;

                if(totalResist == 0) continue;
                float totalCurrent = (localVoltage - targetVoltage) / totalResist;

                float currentActual = Math.min(electricalLogic.getAmperage(), Math.abs(totalCurrent));
                float voltageAdjust = (currentActual * totalResist);

                if(localVoltage > targetVoltage)
                {
                    targetLogic.setAmperage(targetLogic.getAmperage() + currentActual);
                    targetLogic.setVoltage(targetVoltage + voltageAdjust);

                    electricalLogic.setAmperage(electricalLogic.getAmperage() - currentActual);
                    electricalLogic.setVoltage(electricalLogic.getVoltage() - voltageAdjust);
                } else {
                    targetLogic.setAmperage(targetLogic.getAmperage() - currentActual);
                    targetLogic.setVoltage(targetVoltage - voltageAdjust);

                    electricalLogic.setAmperage(electricalLogic.getAmperage() + currentActual);
                    electricalLogic.setVoltage(electricalLogic.getVoltage() + voltageAdjust);
                }

                if(localVoltage > 0)
                {
                    level.setBlock(worldPosition.above(), Blocks.IRON_BLOCK.defaultBlockState(), 3);
                } else {
                    level.setBlock(worldPosition.above(), Blocks.AIR.defaultBlockState(), 3);
                }
            }
            else if (neighbor instanceof ElectricalBlockEntity sourceOfElec)
            { // ElectricalBlockEntity should always be a source of electricity
                ElectricalPolarity sourcePolarity = sourceOfElec.getPolarity(dir.getOpposite());
                if (sourcePolarity == ElectricalPolarity.POSITIVE) {
                    this.setPolarity(dir, ElectricalPolarity.NEGATIVE);
                }
            }
        }

        if (!foundAWire) {
            for (Direction dir : Direction.values()) {
                this.setPolarity(dir, ElectricalPolarity.NEUTRAL);
            }
        }
    }
     */
}
