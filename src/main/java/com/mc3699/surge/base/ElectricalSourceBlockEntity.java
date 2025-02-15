package com.mc3699.surge.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.EnumMap;

// 🥕
public class ElectricalSourceBlockEntity extends PolarityEnabledBlockEntity implements CircuitPart {
    public double outputVoltage; // output voltage

    private static final String outputVoltageTagName = "voltage";

    public ElectricalSourceBlockEntity (BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    // saving and tags
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        // Polarity
        CompoundTag polarityTag = new CompoundTag();
        for (Direction direction : Direction.values()) {
            polarityTag.putString(direction.getName(), polaritySides.getOrDefault(
                    direction,
                    ElectricalPolarity.NEUTRAL
            ).name());
        }

        pTag.put(PolarityEnabledBlockEntity.polaritySidesTagName, polarityTag);
        pTag.putDouble(PolarityEnabledBlockEntity.resistanceTagName, resistance);

        pTag.putDouble(ElectricalSourceBlockEntity.outputVoltageTagName, outputVoltage);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains(ElectricalSourceBlockEntity.polaritySidesTagName, Tag.TAG_COMPOUND))
        {
            CompoundTag polarityTag = pTag.getCompound(ElectricalSourceBlockEntity.polaritySidesTagName);
            for (Direction dir : Direction.values())
            {
                polaritySides.put(dir, ElectricalPolarity.valueOf(polarityTag.getString(dir.getName())));
            }
        }

        if(pTag.contains(ElectricalSourceBlockEntity.outputVoltageTagName)) {
            this.outputVoltage = pTag.getDouble(ElectricalSourceBlockEntity.outputVoltageTagName);
        }
        if(pTag.contains(ElectricalSourceBlockEntity.resistanceTagName)) {
            this.resistance = pTag.getDouble(ElectricalSourceBlockEntity.resistanceTagName);
        }
    }

    public double getOutputVoltage() {
        return outputVoltage;
    }

    @Override
    public double getResistance() {
        return resistance;
    }
}
