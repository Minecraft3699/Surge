package com.mc3699.surge.base;

import net.minecraft.core.BlockPos;

public interface CircuitPart {
    double getResistance();
    BlockPos getBlockPos();
}
