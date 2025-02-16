package com.mc3699.surge.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;

public class PolarityEnabledBlockEntity extends BlockEntity {
    public double resistance; // only used when an electron is passing through it
    protected EnumMap<Direction, ElectricalPolarity> polaritySides;

    protected static final String polaritySidesTagName = "polarity";
    protected static final String resistanceTagName = "resistance";

    public PolarityEnabledBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public ElectricalPolarity getPolarity(Direction direction) {
        return polaritySides.getOrDefault(direction, ElectricalPolarity.NEUTRAL);
    }
    public void initializePolaritySides(EnumMap<Direction, ElectricalPolarity> polaritySides) {
        this.polaritySides = polaritySides;
    }

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

        if(pTag.contains(ElectricalSourceBlockEntity.resistanceTagName)) {
            this.resistance = pTag.getDouble(ElectricalSourceBlockEntity.resistanceTagName);
        }
    }
}
