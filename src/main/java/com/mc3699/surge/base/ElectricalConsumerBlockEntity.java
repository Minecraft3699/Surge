package com.mc3699.surge.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ElectricalConsumerBlockEntity extends PolarityEnabledBlockEntity {
    public ElectricalConsumerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public abstract void power(double voltage, double amperage);
}
