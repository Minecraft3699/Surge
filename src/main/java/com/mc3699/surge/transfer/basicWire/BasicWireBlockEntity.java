package com.mc3699.surge.transfer.basicWire;

import com.mc3699.surge.ModBlockEntities;
import com.mc3699.surge.Surge;
import com.mc3699.surge.base.*;
import com.mc3699.surge.world.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import oshi.util.platform.unix.solaris.KstatUtil;

import java.util.EnumMap;
import java.util.Map;

// class for every type of wire
public class BasicWireBlockEntity extends ElectricalBlockEntity implements TickableBlockEntity {

    private final LazyOptional<IElectricalLogic> electricalOptional = LazyOptional.of(() -> electricalLogic);

    public BasicWireBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BASIC_WIRE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("voltage", electricalLogic.getVoltage());
        pTag.putFloat("amperage", electricalLogic.getAmperage());
        pTag.putFloat("resistance", electricalLogic.getResistance());
    }

    @Override
    public void tick() {

        if(level.isClientSide()) return;

        for(Direction dir : Direction.values())
        {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if(neighbor instanceof BasicWireBlockEntity targetWire)
            {
                IElectricalLogic targetLogic = targetWire.electricalLogic;

                // sharing polarities
                if (!targetWire.isCompletelyNeutral() && !this.isCompletelyNeutral())
                { // share this wires polarity with the next
                    // FIXME: this should not work like this
                    this.setPolarity(dir, ElectricalPolarity.POSITIVE);
                    targetWire.setPolarity(dir.getOpposite(), ElectricalPolarity.NEGATIVE);
                }

                // electrical math bs
                // TODO: make polarity matter
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
                if (sourcePolarity == ElectricalPolarity.NEGATIVE)
                {
                    this.setPolarity(dir, ElectricalPolarity.POSITIVE);
                } else if (sourcePolarity == ElectricalPolarity.POSITIVE) {
                    this.setPolarity(dir, ElectricalPolarity.NEGATIVE);
                }
            }
        }

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == ModCapabilities.ELECTRICAL_CAPABILITY)
        {
            return electricalOptional.cast();
        } else {
            return LazyOptional.empty();
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        electricalOptional.invalidate();
    }
}
