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
public class ElectricalSourceBlockEntity extends BlockEntity {
    private EnumMap<Direction, ElectricalPolarity> polaritySides;

    public float outputVoltage; // output voltage
    public float resistance; // only used when an electron is passing through it

    private static final String polaritySidesTagName = "polarity";
    private static final String outputVoltageTagName = "voltage";
    private static final String resistanceTagName = "resistance";

    public ElectricalSourceBlockEntity (BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public void initializePolaritySides(EnumMap<Direction, ElectricalPolarity> polaritySides) {
        this.polaritySides = polaritySides;
    }

    public ElectricalPolarity getPolarity(Direction direction) {
        return polaritySides.getOrDefault(direction, ElectricalPolarity.NEUTRAL);
    }

    // saving and tags

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        // Polarity
        CompoundTag polarityTag = new CompoundTag();
        for (Direction direction : Direction.values())
        {
            polarityTag.putString(direction.getName(), polaritySides.getOrDefault(
                    direction,
                    ElectricalPolarity.NEUTRAL
            ).name());
        }

        pTag.put(ElectricalSourceBlockEntity.polaritySidesTagName, polarityTag);
        pTag.putFloat(ElectricalSourceBlockEntity.outputVoltageTagName, outputVoltage);
        pTag.putFloat(ElectricalSourceBlockEntity.resistanceTagName, resistance);
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
            this.outputVoltage = pTag.getFloat(ElectricalSourceBlockEntity.outputVoltageTagName);
        }
        if(pTag.contains(ElectricalSourceBlockEntity.resistanceTagName)) {
            this.resistance = pTag.getFloat(ElectricalSourceBlockEntity.resistanceTagName);
        }
    }
}
